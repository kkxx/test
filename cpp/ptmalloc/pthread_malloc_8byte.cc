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