import scala.util.control.Breaks._

def time_ns_print(fun: => Unit) = {
  val t0 = System.nanoTime()
  fun
  val t1 = System.nanoTime()
  println(fun)
  println(" : %d ns".format(t1 - t0))
  println(" : %d ms".format((t1 - t0) / 1000 / 1000))
}

def time_ms_print(fun: => Unit) = {
  val t0 = System.currentTimeMillis
  fun
  val t1 = System.currentTimeMillis
  println(" : %d ms".format(t1 - t0))
}

def test_unzip() = {
  val rootzip = new java.util.zip.ZipFile("""D:\down\cz20130825.zip""")
  import collection.JavaConverters._
  val entries = rootzip.entries.asScala
  entries foreach { e =>
    val is = rootzip.getInputStream(e)
    scala.io.Source.fromInputStream(is).getLines().foreach {
      println
    }
  }
}

import scala.collection.mutable.Map
import scala.io._

val large_file_path: String = """E:\ipdb_city_0903.txt"""

def read_file_source(): Unit = {
  val m = Map[Int, String]()
  var i: Int = 0
  // for (line <- Source.fromFile("data/cz.txt", "utf-8").getLines().filter(_.trim() != "")) {
  for (line <- Source.fromFile(large_file_path, "utf-8").getLines()) {
    i += 1
    m += (i -> line)
  }
  println("line=%d".format(i))
}

import java.io.File
import java.io.FileInputStream
import java.nio.channels.FileChannel.MapMode._

def read_file_map = {
	val file = new File(large_file_path)
  val fileSize = file.length
  val stream = new FileInputStream(file)
  val buffer = stream.getChannel.map(READ_ONLY, 0, fileSize)
}

import java.io._
import java.nio._

def read_file_bufferreader = {
	val file = new File(large_file_path);   
	val fis = new BufferedInputStream(new FileInputStream(file));    
	val reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
	val m = Map[Int, String]()
  var i: Int = 0
  breakable {
  while (true) {
	  	val line = reader.readLine()
	  	if(line == null) break
	  	i += 1
	  	m += (i -> line)
	  } 
	}
  println ("line=%d".format(i))
}

time_ns_print(read_file_source)
time_ns_print(read_file_bufferreader)
time_ns_print(read_file_source)
time_ns_print(read_file_bufferreader)
time_ns_print(read_file_source)
time_ns_print(read_file_bufferreader)
