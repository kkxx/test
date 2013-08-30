val rootzip = new java.util.zip.ZipFile("""D:\down\cz20130825.zip""")
import collection.JavaConverters._
val entries = rootzip.entries.asScala
entries foreach { e =>
  val is = rootzip.getInputStream(e)
  scala.io.Source.fromInputStream(is).getLines().foreach {
    println
  }
}