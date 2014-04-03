package andy.test

import scala.language.reflectiveCalls
import scala.language.implicitConversions
import scala.Predef._

object ScalaBootstrap {

  def main(args: Array[String]) {
    println(ScalaBootstrap.getClass.toString)
    val testInScala = new TestInScala()
    testInScala.test_connection_execute()
    testInScala.test_olap4j()
//    val testInJava = new TestInJava();
//    testInJava.test();
  }
}