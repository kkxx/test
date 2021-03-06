glibc malloc 在线程中预分配大块内存问题调查
===========================================

*Andy (erpingwu@gmail.com)*
*2013/03/09*

问题由一个在数千台机器上跑的一个多线程 agent 引发，主要是大家疑惑为什么这个进程占用的 VIRT 如此之大。问题困惑了不少人，特别是在其它地方内存不足要查问题时常会被追问。这里调查了跟 glibc malloc 有关的一个原因，希望能帮助到大家。

环境: ubuntu 12.04, 64bit

### 一、 boost.thread 为什么比直接使用 pthread 使用更多的内存

对业务代码做减法后，最先发现的是 boost.thread 与直接调用 pthread 内存使用的不一致，下面是相关测试代码

**pthread 测试代码**

~~~~ {.cpp .numberLines}
    #include <stdio.h>
    #include <unistd.h>
    #include <pthread.h>
    // g++ pthread.cc -static -lpthread  -ggdb -g
    
    void* test(void*) {
      while(true) {
        sleep(1000000);
      }
      return NULL;
    }

    int main(int argc, char* argv[]) {
      pthread_t tid;
      int ret = pthread_create(&tid, NULL, test, NULL);
      if(ret != 0){
        printf("pthread_create error!\n");
      }    
      while(true) {
        sleep(100000);
      }
    }
~~~~

**boost.thread 测试代码**

~~~~ {.cpp .numberLines}
    #include <stdio.h>
    #include <unistd.h>
    #include <pthread.h>
    #include "boost/thread.hpp"
    // gcc boost_thread.cc -lboost_thread -lpthread
    
    void test() {
      while(true) {
        sleep(1000000);
      }
    }

    int main(int argc, char* argv[]) {
      boost::thread testThread(test);
      while(true) {
        sleep(100000);
      }
    }
~~~~

**比较两者内存使用**

~~~~ {.cpp}
fab dump_pname:a.out
[localhost] local: pidof a.out > /tmp/pid.tmp
[localhost] local: sudo pmap -d 6916
6916:   ./a.out
Address           Kbytes Mode  Offset           Device    Mapping
0000000000400000     836 r-x-- 0000000000000000 008:00001 a.out
00000000006d0000      12 rw--- 00000000000d0000 008:00001 a.out
00000000006d3000      28 rw--- 0000000000000000 000:00000   [ anon ]
0000000000d36000     140 rw--- 0000000000000000 000:00000   [ anon ]
00007f1eb890d000       4 ----- 0000000000000000 000:00000   [ anon ]
00007f1eb890e000    8192 rw--- 0000000000000000 000:00000   [ anon ]
00007fffc93e6000     132 rw--- 0000000000000000 000:00000   [ stack ]
00007fffc9595000       4 r-x-- 0000000000000000 000:00000   [ anon ]
ffffffffff600000       4 r-x-- 0000000000000000 000:00000   [ anon ]
mapped: 9352K    writeable/private: 8504K    shared: 0K
~~~~

~~~~ {.cpp}
fab dump_pname:a.out
[localhost] local: pidof a.out > /tmp/pid.tmp
[localhost] local: sudo pmap -d 7059
7059:   ./a.out
Address           Kbytes Mode  Offset           Device    Mapping
0000000000400000    1432 r-x-- 0000000000000000 008:00001 a.out
0000000000765000      16 rw--- 0000000000165000 008:00001 a.out
0000000000769000     108 rw--- 0000000000000000 000:00000   [ anon ]
00000000012ce000     140 rw--- 0000000000000000 000:00000   [ anon ]
00007f92c4000000     132 rw--- 0000000000000000 000:00000   [ anon ]
00007f92c4021000   65404 ----- 0000000000000000 000:00000   [ anon ]
00007f92c95a0000       4 ----- 0000000000000000 000:00000   [ anon ]
00007f92c95a1000    8192 rw--- 0000000000000000 000:00000   [ anon ]
00007fff42e32000     132 rw--- 0000000000000000 000:00000   [ stack ]
00007fff42ea7000       4 r-x-- 0000000000000000 000:00000   [ anon ]
ffffffffff600000       4 r-x-- 0000000000000000 000:00000   [ anon ]
mapped: 75568K    writeable/private: 8720K    shared: 0K
~~~~

简单可见，两者都有给 thread 的 8 M stack, 但 boost.thread 版本的代码多出了近 64 M 内存：

00007f92c4021000 **65404** ----- 0000000000000000 000:00000 [ anon ]

利用 **strace -F ./a.out** 可见 boost.thread 版本多出了几行系统调用

~~~~ {.cpp .numberLines}
[pid  7060] mmap(NULL, 134217728, PROT_NONE, MAP_PRIVATE|MAP_ANONYMOUS|MAP_NORESERVE, -1, 0) = 0x7f92c15a0000
[pid  7060] munmap(0x7f92c15a0000, 44433408) = 0
[pid  7060] munmap(0x7f92c8000000, 22675456) = 0
[pid  7060] mprotect(0x7f92c4000000, 135168, PROT_READ|PROT_WRITE) = 0
~~~~

这里解释一下前面三句做了什么

1.  **`mmap(NULL, 134217728`**, 分配 `hex(134217728) = 0x8000000 = 128M`，　内存范围是：0x7f92c15a0000 - 0x7f92c95a0000,

2.  **`munmap(0x7f92c15a0000, 44433408)`** , 去掉刚分配的128M内存的前面一部分 `hex(0x7f92c15a0000 + 44433408)`, 内存范围变成 0x7f92c4000000 - 0x7f92c95a0000

3.  **`munmap(0x7f92c8000000, 22675456)`** , 从 0x7f92c8000000 开始去掉 22675456 bytes 的内存， 而 `hex(0x7f92c8000000+22675456) = 0x7f92c95a0000`， 实际上就是将刚分配的128M内存再去掉尾部，内存范围变成 0x7f92c4000000 - 0x7f92c8000000. 最后保留了 64M 内存，`hex(0x7f92c8000000 - 0x7f92c4000000) = 0x4000000 = 64M`

~~~~ {.cpp}
  +----------+ <---+ 0x7f92c15a0000 // 1. mmap(NULL, 134217728)

  |          |
  +----------+ <---+ 0x7f92c4000000 // 2. munmap(0x7f92c15a0000, 44433408)
  |          |
  |          |
  |          |
  +----------+ <---+ 0x7f92c8000000 // 3. munmap(0x7f92c8000000, 22675456)
  |          |
  |          |
  +----------+ <---+ 0x7f92c95a0000
~~~~

**我们可以猜想有人预分配了内存，那么现在就是找到谁分配了64M内存**

**用 gdb 跟一下**

~~~~ {.cpp}
gdb ./a.out
b /usr/local/include/boost/thread/detail/thread.hpp:218
c
(gdb) info thread
  Id   Target Id         Frame
  2    Thread 0x7ffff7ffd700 (LWP 7221) "a.out" 0x000000000040ffe0 in boost::detail::get_once_per_thread_epoch() ()
* 1    Thread 0x784880 (LWP 7220) "a.out" boost::thread::thread<void (*)()> (this=0x7fffffffe550, f=0x402574 <test()>)
    at /usr/local/include/boost/thread/detail/thread.hpp:219
(gdb) thread 2
[Switching to thread 2 (Thread 0x7ffff7ffd700 (LWP 7221))]
#0  0x000000000040ffe0 in boost::detail::get_once_per_thread_epoch() ()
(gdb) bt
#0  0x000000000040ffe0 in boost::detail::get_once_per_thread_epoch() ()
#1  0x0000000000407c12 in void boost::call_once<void (*)()>(boost::once_flag&, void (*)()) [clone .constprop.120] ()
#2  0x00000000004082cf in thread_proxy ()
#3  0x000000000041120a in start_thread (arg=0x7ffff7ffd700) at pthread_create.c:308
#4  0x00000000004c5cf9 in clone ()
#5  0x0000000000000000 in ?? ()
~~~~

**可以发现在 get\_once\_per\_thread\_epoch 函数中有一行 `data=malloc(sizeof(boost::uintmax_t));`, 这行 malloc 了 8 bytes**.

~~~~ {.cpp .numberLines}
// boost_1_50_0/libs/thread/src/pthread/once.cpp
boost::uintmax_t& get_once_per_thread_epoch()
{
    BOOST_VERIFY(!pthread_once(&epoch_tss_key_flag,create_epoch_tss_key));
    void* data=pthread_getspecific(epoch_tss_key);
    if(!data)
    {
        data=malloc(sizeof(boost::uintmax_t)); // 相比直接调用 pthread ，多出了这一句内存分配
        BOOST_VERIFY(!pthread_setspecific(epoch_tss_key,data));
        *static_cast<boost::uintmax_t*>(data)=UINTMAX_C(~0);
    }
    return *static_cast<boost::uintmax_t*>(data);
}
~~~~

从 boost\_1\_50\_0/boost/thread/pthread/once.hpp 可以找到一个为什么做这个的注释， 不过这里我们不关心这个

~~~~ {.cpp .numberLines}
// Based on Mike Burrows fast_pthread_once algorithm as described in
// http://www.open-std.org/jtc1/sc22/wg21/docs/papers/2007/n2444.html
~~~~

实际上就这么8节字引发了 64M 的分配

### 二、 pthread 实际上也会使用同样多的内存

继续测试

**再准备一个测试文件 pthread\_malloc\_8byte.cc**

~~~~ {.cpp .numberLines}
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

// g++ pthread_malloc_8byte.cc -static -lpthread -ggdb -g

void* test(void*) {
  malloc(8);
  while(true) {
    sleep(1000000);
  }
  return NULL;
}

int main(int argc, char* argv[]) {
  pthread_t tid;
  int ret = pthread_create(&tid, NULL, test, NULL);
  if(ret != 0){
    printf("pthread_create error!\n");
  }    
  while(true) {
    sleep(100000);
  }
}
~~~~

diff 一下

~~~~ {.diff .numberLines}
git diff pthread.cc pthread_malloc_8byte.cc
 void* test(void*) {
+  malloc(8);
   while(true) {
     sleep(1000000);
   }
~~~~

观察这个进程，可以发现同样有分配大块内存

00007f7e2c021000 **65404** ----- 0000000000000000 000:00000 [ anon ]

说明这份内存的增加与 boost.thread 无关， 继续跟

### 三、 malloc 分配了这 64M

前面 strace 多出来的最后一行是 mprotect, 再 GDB 跟进去

~~~~ {.cpp .numberLines}
Breakpoint 1, 0x000000000043ff10 in mprotect ()
(gdb) bt
#0  0x000000000043ff10 in mprotect ()
#1  0x000000000041a0d3 in new_heap ()
#2  0x000000000041b045 in arena_get2.isra.5.part.6 ()
#3  0x000000000041ed13 in malloc ()
#4  0x0000000000401b1a in test () at pthread_malloc_8byte.cc:9
#5  0x0000000000402d3a in start_thread (arg=0x7ffff7ffd700) at pthread_create.c:308
#6  0x00000000004413d9 in clone ()
#7  0x0000000000000000 in ?? ()
~~~~

简单可猜想，分配应该是在 new\_heap 函数中进行，下一步就是找 malloc 的代码分析

### 四、 new\_heap 相关源码分析

代码跟踪跳转的过程就略过，直接贴相关代码， glibc 用的是 2.15

代码中有大量的宏， 这里贴出来方便阅读

~~~~ {.cpp .numberLines}

#ifndef INTERNAL_SIZE_T
#define INTERNAL_SIZE_T size_t
#endif

/* The corresponding word size */
#define SIZE_SZ                (sizeof(INTERNAL_SIZE_T))

#ifndef DEFAULT_MXFAST
#define DEFAULT_MXFAST     (64 * SIZE_SZ / 4)  // (64 * 8 / 4) = 128
#endif


#define set_max_fast(s) \
  global_max_fast = (((s) == 0)       \
     ? SMALLBIN_WIDTH: ((s + SIZE_SZ) & ~MALLOC_ALIGN_MASK))
#define get_max_fast() global_max_fast


#ifndef DEFAULT_MMAP_THRESHOLD_MIN
#define DEFAULT_MMAP_THRESHOLD_MIN (128 * 1024)
#endif

#ifndef DEFAULT_MMAP_THRESHOLD_MAX
  /* For 32-bit platforms we cannot increase the maximum mmap
     threshold much because it is also the minimum value for the
     maximum heap size and its alignment.  Going above 512k (i.e., 1M
     for new heaps) wastes too much address space.  */
# if __WORDSIZE == 32
#  define DEFAULT_MMAP_THRESHOLD_MAX (512 * 1024)
# else
#  define DEFAULT_MMAP_THRESHOLD_MAX (4 * 1024 * 1024 * sizeof(long))
# endif
#endif

/* Compile-time constants.  */
#define HEAP_MIN_SIZE (32*1024)
#ifndef HEAP_MAX_SIZE
# ifdef DEFAULT_MMAP_THRESHOLD_MAX
#  define HEAP_MAX_SIZE (2 * DEFAULT_MMAP_THRESHOLD_MAX)
# else
#  define HEAP_MAX_SIZE (1024*1024) /* must be a power of two */
# endif
#endif
~~~~

简单注解：

1.  `# define DEFAULT_MMAP_THRESHOLD_MAX (4 * 1024 * 1024 * sizeof(long))` , 这里　`sizeof(long) = 8`，　也就是这个值是 32M, `4 * 1024 * 1024 * sizeof(long) = 0x2000000 = 32M`.

2.  `#  define HEAP_MAX_SIZE (2 * DEFAULT_MMAP_THRESHOLD_MAX)`, 那这里就是 64M `hex(0x2000000*2) = '0x4000000' = 64M`

**了解了宏定久后，直接看 new\_heap, 基本思路就是分配一大块对齐的内存。 注意glibc自身注释与我后加的注释**

*当然要进入到这一份代码， 跟 PER\_THREAD 宏定义有关， 不过这个缺省是定义的，先不用关注。*

~~~~ {.cpp .numberLines}
// glibc-2.15\malloc\arena.c

/* If consecutive mmap (0, HEAP_MAX_SIZE << 1, ...) calls return decreasing
   addresses as opposed to increasing, new_heap would badly fragment the
   address space.  In that case remember the second HEAP_MAX_SIZE part
   aligned to HEAP_MAX_SIZE from last mmap (0, HEAP_MAX_SIZE << 1, ...)
   call (if it is already aligned) and try to reuse it next time.  We need
   no locking for it, as kernel ensures the atomicity for us - worst case
   we'll call mmap (addr, HEAP_MAX_SIZE, ...) for some value of addr in
   multiple threads, but only one will succeed.  */
static char *aligned_heap_area;  

/* Create a new heap.  size is automatically rounded up to a multiple
   of the page size. */

static heap_info *
internal_function
new_heap(size_t size, size_t top_pad)
{
  size_t page_mask = GLRO(dl_pagesize) - 1; 
  char *p1, *p2;
  unsigned long ul;
  heap_info *h;

  if(size+top_pad < HEAP_MIN_SIZE)
    size = HEAP_MIN_SIZE;
  else if(size+top_pad <= HEAP_MAX_SIZE)
    size += top_pad;
  else if(size > HEAP_MAX_SIZE)
    return 0;
  else
    size = HEAP_MAX_SIZE;
  // 按页面大小对齐
  size = (size + page_mask) & ~page_mask; 

  /* A memory region aligned to a multiple of HEAP_MAX_SIZE is needed.
     No swap space needs to be reserved for the following large
     mapping (on Linux, this is the case for all non-writable mappings
     anyway). */
  p2 = MAP_FAILED;
  if(aligned_heap_area) { // 全局静态，初始为0，跳过
    ...
  }
  if(p2 == MAP_FAILED) {
    // 64位系统， HEAP_MAX_SIZE = 64M， <<1 == 128M
    // 分配 HEAP_MAX_SIZE 的两倍，这样最差也能找一块对齐的内存出来
    p1 = (char *)MMAP(0, HEAP_MAX_SIZE<<1, PROT_NONE, 
        MAP_PRIVATE|MAP_NORESERVE); 
    if(p1 != MAP_FAILED) {
      // 将 p1 位置按 HEAP_MAX_SIZE 对齐
      p2 = (char *)(((unsigned long)p1 + (HEAP_MAX_SIZE-1))
        & ~(HEAP_MAX_SIZE-1)); 
      // 对齐后从原来 p1 位置到新对齐的　p2 位置的距离
      ul = p2 - p1; 
      if (ul)
        // 截掉对齐的p2位置之前的这一段
        munmap(p1, ul); 
      else
        aligned_heap_area = p2 + HEAP_MAX_SIZE;
        
      // 从对齐的 p2 开始加上 HEAP_MAX_SIZE，就是想要的大小，将尾部截掉。       
      munmap(p2 + HEAP_MAX_SIZE, HEAP_MAX_SIZE - ul); 
      // 得到一块位置对齐的 HEAP_MAX_SIZE 大小的内存
    } else {
      ...
    }
  }
  // 在对齐 64M 的堆里做想做的
  if(mprotect(p2, size, PROT_READ|PROT_WRITE) != 0) {
    munmap(p2, HEAP_MAX_SIZE);
    return 0;
  }
  h = (heap_info *)p2;
  h->size = size;
  h->mprotect_size = size;
  THREAD_STAT(stat_n_heaps++);
  return h;
}
~~~~

**到这里内存分配源头找到了，那再看看什么时候有了这种预分配 glibc-2.15/ChangeLog.17 有说明**

    glibc-2.15\ChangeLog.17
    2009-03-13  Ulrich Drepper  <drepper@redhat.com>
      * malloc/malloc.c: Implement PER_THREAD and ATOMIC_FASTBINS features.
      * malloc/arena.c: Likewise.
      * malloc/hooks.c: Likewise.

**这个 PER\_THREAD 的分配开关在哪**

~~~~ {.makefile .numberLines}
#glibc-2.15\malloc\Makefile
CPPFLAGS-malloc.c += -DPER_THREAD
~~~~

如果不想要这个分配，可以通过重新编译 glibc 来关闭，不过更简单的办法是使用 tcmalloc 来代替 ptmalloc。 但要注意， tcmalloc 与 malloc 都是内存池的思路， 那只是说预分配的时间点与方式不一样， 仅从这一点而言两者并无优劣之分。反而是要注意不要自己动手写多线程内存池. 要写，至少要理解 malloc(ptmalloc), tcmalloc, jemalloc，不然写出来的基于malloc的内存池只会性能更低，因为有些事做了两次。

### 五、 开10个线程，每个线程都 malloc 8 个字节，虚拟内存会是多少

**按上面的分配，只要在线程内使用了 malloc， 每线程事实上会预分配 8M + 64M = 72M, 如果开10个, 700M的VmData就出来了， 下面是开10线程时的状态**

~~~~ {.cpp}
// fab dump_pname:"pname=a.out,tofile=True"
// cat 3669.status.out
VmPeak:   804692 kB
VmSize:   739156 kB
VmLck:         0 kB
VmPin:         0 kB
VmHWM:       508 kB
VmRSS:       508 kB
VmData:   737572 kB
VmStk:       136 kB
VmExe:      1432 kB
VmLib:         0 kB
VmPTE:        96 kB
VmSwap:        0 kB
Threads:  11
~~~~

### 六、 其它

#### 1. 进程内存查看脚本工具

~~~~ {#mycode .python .numberLines}
#!/usr/bin/env python
#-*-coding:utf-8-*-
from fabric.api import *

#dump_pname:pname
def dump_pname(pname, tofile=False):
  local('''pidof %s > /tmp/pid.tmp''' % (pname))
  with open("/tmp/pid.tmp") as f:
    data = f.read()
    dump_pid(data.strip(), tofile)

# fab dump_pid:pid
def dump_pid(pid, tofile=False):  
  if(tofile):
    local('''sudo cat /proc/%s/smaps > %s.smaps.out''' % (pid, pid))
    local('''sudo cat /proc/%s/maps > %s.maps.out''' % (pid, pid))
    local('''sudo cat /proc/%s/status > %s.status.out''' % (pid, pid))
    local('''sudo pmap -d %s > %s.pmap.out''' % (pid, pid))
  else:
    local('''sudo pmap -d %s''' % (pid))
~~~~

#### 2. 链接

-   [fabric](https://github.com/fabric/fabric)

