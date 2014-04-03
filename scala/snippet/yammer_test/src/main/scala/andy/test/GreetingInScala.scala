package andy.test

import com.google.common.collect.Range

class GreetingInScala {
  val ns_of_s = 1000000000L

  def timed[T](name: String)(f: => T): T = {
    val t = System.nanoTime
    val r = f
    val d = System.nanoTime - t
    println("  %s=%d".format(name, r))
    printf("\t%,d ns\n", d)
    printf("\t%,d ms\n", d / 1000)
    printf("\t%,d ops/s\n", ns_of_s / d)
    return r
  }

  def range_until(): Long = {
    val v = 0 until 100000
    return v.size
  }

  def test_for_until(): Long = {
    //    var start = System.currentTimeMillis();
    var total = 0;
    for (i <- 0 until 100000) {
      total += i
    };
    return total
    //    var end = System.currentTimeMillis();
    //    println(end-start);
    //    println(total);
  }

  def test_while(): Long = {
    var total = 0;
    var i = 0;
    while (i < 100000) {
      total += i;
      i = i + 1;
    };
    return total
  }

  def test_while_range():Long = {
    import java.lang.{Integer => jI}
    // Range.open[jI](3,5)
    val r = Range.closed[java.lang.Integer](0, 100000);
    return 0L
}

  def greet() {
    //    println(classOf[GreetingInScala])
    //    val delegate = new GreetingInJava
    //    delegate.greet()
    //    timed("test_for_until") {
    //      test_for_until
    //    }
    //    timed("test_while") {
    //      test_while
    //    }
    timed("range until") {
      range_until
    }
    timed("test_while_range") {
      test_while_range
    }
  }
}