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
// #include <gflags/gflags.h>

using namespace std;
using namespace google;
using namespace process;


class MyProcess : public Process<MyProcess> {
public:
  MyProcess() : ProcessBase("__MyProcess__")  {}
  virtual ~MyProcess() {}
  
  void func2(int i) {
    VLOG(2) << "func2 in " << this->self();
    google::FlushLogFiles(google::GLOG_INFO);
    promise.set(i);
  }
private:
  Promise<int> promise;
};

int main(int argc, char** argv) {    
  google::InitGoogleLogging(argv[0]);
  google::SetLogDestination(google::INFO,"./log/");
  // export GLOG_v=2
  // VLOG(2) << "VLOG(2) test";
  MyProcess process;
  
//  
//  PID<MyProcess> pid = spawn(&process);
//  dispatch(pid, &MyProcess::func2, 42);
//  LOG(INFO) << process.self();
//  // VLOG(2) << process.self();
//  google::FlushLogFiles(google::GLOG_INFO);
//  wait(pid);
  
  spawn(process);
  terminate(process);
  wait(process);
  
  return 0;
}
