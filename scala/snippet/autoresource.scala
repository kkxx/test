import java.io._
import scala.language.reflectiveCalls
import scala.language.implicitConversions

trait Managed[T] {
  def onEnter(): T
  def onExit(t: Throwable = null)
  def attempt(block: => Unit) {
    try { block } finally {}
  }
}

def using[T <: Any, R](managed: Managed[T])(block: T => R): R = {
  val resource = managed.onEnter()
  var exception = false
  try {
    block(resource)
  } catch {
    case t: Throwable => {
      exception = true
      managed.onExit(t)
      throw t
    }
  } finally {
    if (!exception) {
      managed.onExit()
    }
  }
}

def using[T <: Any, U <: Any, R](managed1: Managed[T], managed2: Managed[U])(block: T => U => R): R = {
  using[T, R](managed1) { r =>
    using[U, R](managed2) { s => block(r)(s) }
  }
}

class ManagedClosable[T <: { def close(): Unit }, R](closable: T) extends Managed[T] {
  def onEnter(): T = closable
  def onExit(t: Throwable = null) {
    attempt(closable.close())
  }
}

implicit def closable2managed[T <: { def close(): Unit }, R](closable: T): Managed[T] = {
  new ManagedClosable(closable)
}

def readLine() {
  using(new BufferedReader(new FileReader("autoresource.scala"))) {
    file => {
      file.readLine()
    }
  }
}

readLine()



import scala.language.reflectiveCalls
def using[Closeable  <: { def close(): Unit }, R](closeable: Closeable)(f: Closeable => R): R = {
  try {
  	println("%s(%s)".format(f, closeable))
    f(closeable)
  } finally {
  	println("%s.close".format(closeable))
    closeable.close()
  }
}



class Resource private() {
  println("Starting Resource...")
  var x = 0;
  private def clean_up() { println("Ending Resource...") } 
  def op1 = {x+=1;println("op1, %d".format(x));}
  def op2 = {x+=1;println("op2, %d".format(x));}
}

object Resource { 
  def use(code_block: Resource => Unit) { 
    val resource = new Resource
    try { 
      code_block(resource) 
    } 
    finally { 
      resource.clean_up() 
    } 
  } 
} 

Resource.use { res =>
  res.op1;
  res.op2;
}



