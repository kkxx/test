cmake_minimum_required(VERSION 2.8)
PROJECT (t)
#SET(EXECUTABLE_OUTPUT_PATH  "../../")
message(STATUS "src = ${CMAKE_CURRENT_SOURCE_DIR}")
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR} PRJ_SRCS)
add_executable( ${PROJECT_NAME} ${PRJ_SRCS})
set(link_libs
  :libprocess.a
  :libev.a  
  #:libgflags.a
  :libglog.a
  #:libgmock.a
  #:libgtest.a  
  z
  pthread
  #:libunwind.a
  rt
  dl
)
target_link_libraries(${PROJECT_NAME} ${link_libs})
