import scala.util.matching.Regex
def printStackFrame() {
  /* Get current stack trace. The first frame is this method call. */
  val st = new RuntimeException().getStackTrace.view.drop(1)

  // st take(5) foreach println /* Print a few frames of interest */

  val name =
    st.map(ste => ste.getClassName + "." + ste.getMethodName)
      .dropWhile(_.matches(""".*anonfun\$\d*\.apply\$mcV\$sp$"""))
      .apply(0)

  println(name)
}

def time_ms(fun: => Unit) = {
  val s = System.currentTimeMillis  
  fun
  System.currentTimeMillis - s
}

def time_ns[R](f: => R): (R, Long) = {
  val t0 = System.nanoTime()
  val r = f
  val t1 = System.nanoTime()
  return (r, t1 - t0)
}

def time_ns_print(fun: => Unit) = {
  val t0 = System.nanoTime()
  fun
  val t1 = System.nanoTime()
  val diff = t1 - t0
  println(" : %d ns".format(diff))
}


def named_group = {
  val pattern = new Regex("""(\w*) (\w*)""", "firstName", "lastName");
  val result = pattern.findFirstMatchIn("ErPing Wu").get;
  println("firstName=%s, lastName=%s".format(result.group("firstName"), result.group("lastName")));
}

println(time_ms(named_group))
time_ns_print(named_group)
