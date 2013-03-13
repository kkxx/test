#include <sys/syscall.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
// g++ tls.cc -static -lpthread  -ggdb -g

__thread pid_t tls_pid_t = -1;

void set_tls_pid_t() {
  tls_pid_t = pid_t(syscall(__NR_gettid));
}

pid_t linux_gettid(void) {
  return tls_pid_t;
}

void* test(void*) {
  set_tls_pid_t();
  sleep(1);
    printf("%d\n", linux_gettid());
  while(true) {
    sleep(1000000);
  }
  return NULL;
}

int main(int argc, char* argv[]) {
  for(int i=0; i<100; ++i) { 
    pthread_t tid;
    int ret = pthread_create(&tid, NULL, test, NULL);
    if(ret != 0){
      printf("pthread_create error!\n");
    }    
  }
  while(true) {
    sleep(100000);
  }
}
