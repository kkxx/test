#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include "boost/thread.hpp"

// g++ boost_thread.cc -static -lboost_system -lboost_thread -lpthread -ggdb -g

void test() {
  while(true) {
    sleep(1000000);
  }
}

int main(int argc, char* argv[]) {
  boost::thread testThread(test);
  // boost::thread testThread1(test);
  // boost::thread testThread2(test);
  // boost::thread testThread3(test);
  // boost::thread testThread4(test);
  // boost::thread testThread5(test);
  // boost::thread testThread6(test);
  // boost::thread testThread7(test);
  // boost::thread testThread8(test);
  // boost::thread testThread9(test);
  while(true) {
    sleep(100000);
  }
}

