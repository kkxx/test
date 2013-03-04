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
  if(pthread_create(&tid, NULL, test, NULL) != 0){
    printf("pthread_create error!\n");
  }    
  while(true) {
    sleep(100000);
  }
}