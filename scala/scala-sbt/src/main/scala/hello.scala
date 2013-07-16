import org.jsoup._
import org.jsoup.nodes._

object Hello {
  def main(args: Array[String]) {
    println("Hello!")
    val html:String = """<html><head><title>Hello jsoup</title></head>"
    <body><p>Parsed HTML into a doc.</p></body></html>"""
    var doc:Document = Jsoup.parse(html)
    var title = doc.select("title")
    println("title = " + title.text())
  }
}
