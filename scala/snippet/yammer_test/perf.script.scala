// http://www.infoq.com/news/2011/11/yammer-scala


def timed[T](name: String)(f: => T) = {
  printf("%s :\n", name)
  val t = System.nanoTime
  val r = f
  val d = System.nanoTime - t
  printf("%,d ns\n", d)
  printf("%,d ops/s\n", (1000000000 * 1000000000L) / d)
  printf("%,f ns/op\n", d.toFloat / 1000000000L)
}


def test_for_until() {
  var start = System.currentTimeMillis();
  var total = 0;for(i <- 0 until 100000) { total += i };
  var end = System.currentTimeMillis();
  println(end-start);
  println(total);
}

def test_for_to() {
  var start = System.currentTimeMillis();
  var total = 0;for(i <- 0 to 100000) { total += i };
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

def test_for2() {
//  var start = System.currentTimeMillis();
  var start = System.nanoTime();
  val range = 0 until 100000
  var total = range.sum
  // var total = range.foldLeft(0){(m: Int, n: Int) => m + n};
//  var end = System.currentTimeMillis();
  var end = System.nanoTime();
  println(end-start);
  println(total);
}

def test_while() {
  var start = System.currentTimeMillis();
  var total = 0;var i=0;while(i < 100000) { total += i;i=i+1;};
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

// test_for()
// test_for_until()
// test_while()
// test_for()
// test_for_to
test_for2
// test_for2()

// test_scala_mutable_hashmap()
// test_java_util_hashmap()
// test_java_util_concurrent_concurrent_hashmap()

