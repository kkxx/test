package andy.test

object ScalaBootstrap {
  def main(args: Array[String]) {
    println(ScalaBootstrap.getClass.toString)
    val java = new GreetingInScala()
    java.greet();
  }
}