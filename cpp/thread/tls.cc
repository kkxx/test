#include <sys/syscall.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <map>
#include <unordered_map>
#include "boost/thread/mutex.hpp"
// clang++ tls.cc -std=c++11 -static -lboost_thread -lboost_system -lpthread  -ggdb -g
using namespace std;
std::map<pid_t, int> m;
std::unordered_map<pid_t, int> m2;
boost::mutex tls_mutex;

__thread pid_t tls_pid_t = -1;

void set_tls_pid_t() {
  tls_pid_t = pid_t(syscall(__NR_gettid));
  // boost::mutex::scoped_lock scoped_lock(tls_mutex);
  m[tls_pid_t] = tls_pid_t; 
  m2[tls_pid_t] = tls_pid_t; 
}

pid_t linux_gettid(void) {
  return m[tls_pid_t];
}

pid_t linux_gettid_2(void) {
  return m2[tls_pid_t];
}

void* test(void*) {
  set_tls_pid_t();
  sleep(1);
  printf("map, %d\n", linux_gettid());
  printf("unordered_map, %d\n", linux_gettid_2());
  while(true) {
    sleep(1000000);
  }
  return NULL;
}

int main(int argc, char* argv[]) {
  for(int i=0; i<10; ++i) { 
    pthread_t tid;
    int ret = pthread_create(&tid, NULL, test, NULL);
    if(ret != 0){
      printf("pthread_create error!\n");
    }    
  }
  printf("0=%d\n", m[0]);
  printf("0=%d\n", m2[0]);
  while(true) {
    sleep(100000);
  }
}
