#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include "boost/bind.hpp"
#include "boost/coroutine/all.hpp"

// g++ boost_coroutine.cc -static -lboost_context -ggdb -g

typedef boost::coroutines::coroutine< void() > coroutine_t;

void first(coroutine_t::caller_type & self) {
  std::cout << "started first! ";
  for (int i = 0; i < 10; ++i) {
    self();
    std::cout << "a" << i;
  }
}

void second(coroutine_t::caller_type & self) {
  std::cout << "started second! ";
  for (int i = 0; i < 10; ++i) {
    self();
    std::cout << "b" << i;
  }
}


int main(int argc, char* argv[]) {
  coroutine_t c1(boost::bind(first, _1));
  coroutine_t c2(boost::bind(second, _1));
  while (c1 && c2) {
    c1();
    std::cout << " ";
    c2();
    std::cout << " ";
  }  
  std::cout << "\nDone" << std::endl;
}

