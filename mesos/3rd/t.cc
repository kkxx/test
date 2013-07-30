#include <iostream>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <string>
#include <sstream>
#include <process/async.hpp>
#include <process/collect.hpp>
#include <process/clock.hpp>
#include <process/defer.hpp>
#include <process/delay.hpp>
#include <process/dispatch.hpp>
#include <process/executor.hpp>
#include <process/filter.hpp>
#include <process/future.hpp>
#include <process/gc.hpp>
#include <process/gmock.hpp>
#include <process/gtest.hpp>
#include <process/process.hpp>
#include <process/run.hpp>
#include <process/time.hpp>
#include <stout/duration.hpp>
#include <stout/nothing.hpp>
#include <stout/os.hpp>
#include <stout/stringify.hpp>

#include <gmock/gmock.h>
#include <gtest/gtest.h>

using namespace process;
using namespace std;

class MyProcess : public Process<MyProcess> {
public:
  MyProcess() {}
  virtual ~MyProcess() {}
};

int main(int argc, char** argv) {
  MyProcess process;
  PID<MyProcess> pid = spawn(&process);
  wait(pid);
  return 0;
}
