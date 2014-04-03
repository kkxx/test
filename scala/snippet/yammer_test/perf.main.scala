package perf
import java.io._

// http://www.infoq.com/news/2011/11/yammer-scala
// scalac perf.main.scala
// scalac -optimise perf.main.scala
// java -cp .;"%SCALA_HOME%\lib\scala-library.jar" perf.Test

object Test {

  def test_for_until() {
    var start = System.currentTimeMillis();
    var total = 0;for(i <- 0 until 100000) { total += i };
    var end = System.currentTimeMillis();
    println(end-start);
    println(total);
  }

  def test_for() {
    var start = System.currentTimeMillis();
    val range = 0 until 100000
    var total = 0;var i=0; for(i <- range) { total += i };
    var end = System.currentTimeMillis();
    println(end-start);
    println(total);
  }

  def test_while() {
    var start = System.currentTimeMillis();
    var total = 0;var i=0;while(i < 100000) { i=i+1;total += i };
    var end = System.currentTimeMillis();
    println(end-start);
    println(total);
  }
  
    
  def test_scala_mutable_hashmap() {
    val m = new scala.collection.mutable.HashMap[Int,Int]; 
    var i = 0;
    var start = System.currentTimeMillis();
    while(i<100000) { i=i+1;m.put(i,i);};
    var end = System.currentTimeMillis();
    println(end-start);
    println(m.size)
  }

  def test_java_util_hashmap() {
    val m = new java.util.HashMap[Int,Int]; 
    var i = 0;
    var start = System.currentTimeMillis();
    while(i<100000) { i=i+1;m.put(i,i);};
    var end = System.currentTimeMillis();
    println(end-start);
    println(m.size)
  }

  def test_java_util_concurrent_concurrent_hashmap() {
    val m = new java.util.concurrent.ConcurrentHashMap[Int,Int]; 
    var i = 0;
    var start = System.currentTimeMillis();
    while(i<100000) { i=i+1;m.put(i,i);};
    var end = System.currentTimeMillis();
    println(end-start);
    println(m.size)
  }


  def main(args: Array[String]) {
    Test.test_while()
    Test.test_for()
    Test.test_for_until()
    Test.test_while()
    test_scala_mutable_hashmap()
  }
}

