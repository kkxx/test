#include <cstdio>
#include <iostream>
#include <map>
#include <string>
#include <algorithm>
using namespace std;

// clang++ --std=c++11 random_shuffle.cc ; ./a.out

std::vector<std::pair<std::string, uint16_t> > g_hosts = {
    {"127.0.0.1", 50000}
    ,{"127.0.0.1", 50001}
    ,{"127.0.0.1", 50002}
    ,{"127.0.0.1", 50003}
    ,{"127.0.0.1", 50003}
    ,{"127.0.0.1", 50004}
    ,{"127.0.0.1", 50005}
};


void print_port_map(const map<uint16_t, int>& port_map) {
  for(auto kv : port_map) {
    printf("\tport=%d, count=%d\n", kv.first, kv.second);
  }
  printf("\n");
}

void test_with_srand() {
  printf("test_with_srand\n");
  map<uint16_t, int> port_map;
  for(int i=0; i<100; ++i) {
    auto hosts = g_hosts;
    std::srand(time(0));
    std::random_shuffle(hosts.begin(), hosts.end());
    uint16_t port = hosts[0].second;
    // cout << port << endl;
    if(port_map.end() == port_map.find(port))
      port_map[port] = 0;
    port_map[port] += 1;
  }
  print_port_map(port_map);
}

void test() {
  printf("test\n");
  map<uint16_t, int> port_map;
  for(int i=0; i<100; ++i) {
    auto hosts = g_hosts;
    // std::srand(time(0));
    std::random_shuffle(hosts.begin(), hosts.end());
    uint16_t port = hosts[0].second;
    // cout << port << endl;
    if(port_map.end() == port_map.find(port))
      port_map[port] = 0;
    port_map[port] += 1;
  }
  print_port_map(port_map);
}



int main() {
  test_with_srand();
  test();
}


