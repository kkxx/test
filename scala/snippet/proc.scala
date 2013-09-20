import sys.process._
import java.io._
import scala.collection.JavaConversions._
import scala.language.postfixOps
import scala.util.matching._

val result = ("ls -al " #| "grep scala").!!
println(result)

val cmdline = """scala -cp ..\jars\commons-compress-1.5.jar tocsv.scala"""
val os_pattern = """.*(win|linux).*""".r
System.getProperty("os.name").toLowerCase match {
	case os_pattern(osname)  => {
		osname match {
			case "win" => ("""cmd.exe /C """ ++ cmdline) #> new File("log.txt") !
			case _ => cmdline !
		}
	}
	case _ => println("os_pattern skip")
}

def list_jars(dir:String) = {new java.io.File(dir).listFiles.filter(_.getName.endsWith(".jar"))}
def jars2cp(dirs:String*) = {
  var x = Array[java.io.File]()
  dirs.foreach(dir => x = x ++ list_jars(dir) )
  x.mkString(":")
}
val cp = jars2cp("/home/hadoop/hbase/hbase-0.94.10", "/home/hadoop/hbase/hbase-0.94.10/lib")
"""scala -cp %s h.scala""".format(cp) !