#include <sys/syscall.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include "boost/thread.hpp"
// g++ tls_boost.cc -static -lboost_thread -lpthread  -ggdb -g

__thread pid_t tls_pid_t = -1;

void set_tls_pid_t() {
  tls_pid_t = pid_t(syscall(__NR_gettid));
}

pid_t linux_gettid(void) {
  return tls_pid_t;
}

void test() {
  set_tls_pid_t();
  sleep(1);
  printf("%d\n", linux_gettid());
  while(true) {
    sleep(1000000);
  }
}

int main(int argc, char* argv[]) {
  for(int i=0; i<100; ++i) { 
    boost::thread testThread(test);
  }
  while(true) {
    sleep(100000);
  }
}
