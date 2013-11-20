package andy.test

class GreetingInScala {
  def greet() {
    println(classOf[GreetingInScala])
    val delegate = new GreetingInJava
    delegate.greet()
  }
}