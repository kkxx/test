#include <iostream>
#include <arpa/inet.h>
#include <gmock/gmock.h>

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

#include <gtest/gtest.h>


using namespace process;
using namespace std;

/*TEST(TimeTest, Output)
{
  EXPECT_EQ("1989-03-02 00:00:00+00:00", stringify(Time::EPOCH + Weeks(1000)));
  EXPECT_EQ("1989-03-02 00:00:00.000000001+00:00",
            stringify(Time::EPOCH + Weeks(1000) + Nanoseconds(1)));
  EXPECT_EQ("1989-03-02 00:00:00.000001000+00:00",
            stringify(Time::EPOCH + Weeks(1000) + Microseconds(1)));
}*/
/*
TEST(test, test) {
  std::cout << stringify(Time::EPOCH + Weeks(1000)) << std::endl;
    Event* event = new TerminateEvent(UPID());
  EXPECT_FALSE(event->is<MessageEvent>());
  EXPECT_FALSE(event->is<ExitedEvent>());
  EXPECT_TRUE(event->is<TerminateEvent>());
  delete event;
}
*/
int main(int argc, char** argv)
{
  // Initialize libprocess.
  process::initialize();
  Event* event = new TerminateEvent(UPID());
  cout << event->is<MessageEvent>() << endl;
  delete event;

}
