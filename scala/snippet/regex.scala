import scala.util.matching.Regex
val pattern = new Regex("""(\w*) (\w*)""", "firstName", "lastName");
val result = pattern.findFirstMatchIn("ErPing Wu").get;
println("firstName=%s, lastName=%s".format(result.group("firstName"), result.group("lastName")));

val rootzip = new java.util.zip.ZipFile("""D:\down\cz20130825.zip""")
import collection.JavaConverters._
val entries = rootzip.entries.asScala
entries foreach { e =>
  val is = rootzip.getInputStream(e)
  scala.io.Source.fromInputStream(is).getLines().foreach {
    println
  }

}

