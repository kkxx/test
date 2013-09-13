import sys.process._
import java.io._
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

