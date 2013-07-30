cmake_minimum_required(VERSION 2.8)
PROJECT (piano)

# cmake -DCMAKE_BUILD_TYPE=Release ../
# cmake -DCMAKE_BUILD_TYPE=Debug -DPIANO_TRACE_TYPE=true ../
# cmake -DCMAKE_BUILD_TYPE=Debug  -DCMAKE_TOOLCHAIN_FILE=../clang.cmake ../
# cmake -DCMAKE_BUILD_TYPE=Release  -DCMAKE_TOOLCHAIN_FILE=../clang.cmake ../
# cmake -DCMAKE_BUILD_TYPE=Debug -DPIANO_TRACE_TYPE=true  -DCMAKE_TOOLCHAIN_FILE=../gcc.4.8.cmake ../

# http://www.vtk.org/Wiki/CMake_Cross_Compiling
if (CMAKE_COMPILER_IS_GNUCC)
    execute_process(COMMAND ${CMAKE_C_COMPILER} -dumpversion
                    OUTPUT_VARIABLE GCC_VERSION)
    message(STATUS "GNUCC=${GCC_VERSION}")
endif()

# -- CXX FLAGS --
# http://tigcc.ticalc.org/doc/comopts.html
# message(STATUS "CXX_FLAGS = ${CXX_FLAGS} ")
# find * | xargs grep "O2"

if("${PIANO_TRACE_TYPE}" STREQUAL "true")
  set(PIANO_TRACE "-DPIANO_TRACE")
else()
endif()

if("${CMAKE_BUILD_TYPE}" STREQUAL "Debug")
  set(CMAKE_CXX_FLAGS_DEBUG "-O0")
else()
#  set(CMAKE_CXX_FLAGS_RELEASE "-static -O2 -DNDEBUG -finline-limit=1000  -march=x86-64")
  set(CMAKE_CXX_FLAGS_RELEASE "-static -O2 -DNDEBUG -march=x86-64")
endif()

message(STATUS "CMAKE_CXX_FLAGS_RELEASE = ${CMAKE_CXX_FLAGS_RELEASE} ")

set(CXX_FLAGS
    # -pg
      -g3 # Produce debugging information in the operating system's native format
      -gdwarf-2
      -ggdb # Produce debugging information for use by GDB
      
    #  -rdynamic # his instructs the linker to add all symbols, not only used ones, to the dynamic symbol table.
                # http://stackoverflow.com/questions/8623884/gcc-debug-symbols-g-flag-vs-linkers-rdynamic-option
    # -- errors & warnings
      -Werror # Make all warnings into errors.
      -Wall # All of the above '-W' options combined.
      -Wextra
      -Wno-unused-parameter
      -Wno-multichar
      -std=c++11
   )

string(REPLACE  ";"  " "  CMAKE_CXX_FLAGS "${CXX_FLAGS} ${PIANO_TRACE}")
message(STATUS "CMAKE_CXX_FLAGS = ${CMAKE_CXX_FLAGS} ")

# SET(MESOS_LIBPROCESS_PATH "../../../../libprocess")
SET(MESOS_LIBPROCESS_PATH "/home/opsdev/andy/libprocess")
message(STATUS "libprocess dir = ${MESOS_LIBPROCESS_PATH}")

# -- include --
#SET(CMAKE_INCLUDE_PATH ${CMAKE_INCLUDE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/src")
#SET(CMAKE_INCLUDE_PATH ${CMAKE_INCLUDE_PATH} "/usr/local/include")
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${MESOS_LIBPROCESS_PATH}/include/
    ${MESOS_LIBPROCESS_PATH}/3rdparty/stout/include/
    ${MESOS_LIBPROCESS_PATH}/build/3rdparty/boost-1.53.0/
    ${MESOS_LIBPROCESS_PATH}/build/3rdparty/gmock-1.6.0/gtest/include/
    ${MESOS_LIBPROCESS_PATH}/build/3rdparty/gmock-1.6.0/include/
    ${MESOS_LIBPROCESS_PATH}/build/3rdparty/glog/src/
)

# -- library --
LINK_DIRECTORIES (
  /usr/local/lib
  ${MESOS_LIBPROCESS_PATH}/build/.libs/
  ${MESOS_LIBPROCESS_PATH}/build/3rdparty/glog-0.3.3/.libs/
  ${MESOS_LIBPROCESS_PATH}/build/3rdparty/libev-4.15/.libs/   
)

# -- BOOST --
set(Boost_USE_STATIC_LIBS ON)
set(Boost_USE_MULTI_THREADED ON)

# -- OUTPUT --
#SET(PRJ_BINARY_DIR  ${CMAKE_CURRENT_SOURCE_DIR}/build)

message(STATUS ${PRJ_BINARY_DIR})
SET(EXECUTABLE_OUTPUT_PATH  "..")
SET(LIBRARY_OUTPUT_PATH "../../lib")

# -- SUBDIR
ADD_SUBDIRECTORY(3rd)

#execute_process(COMMAND dir RESULT_VARIABLE rv)
#message("rv='${rv}'")
