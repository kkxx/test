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

def time_ns_print(f: => Unit) = {
  val t0 = System.nanoTime()
  f
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

import scala.io._

val province_pattern = new Regex("^(.*?)省$")
val ex_province_pattern = new Regex("^(宁夏|西藏|新疆|广西|北京市|北京|天津市|天津|上海市|上海|重庆市|重庆|香港|澳门)$")
val province_city_pattern = new Regex("(.*?)省(.*?)(州|市|盟).*")
val university_pattern = """(.*大学).*""".r
val ex_city_pattern = """(宁夏|西藏|新疆|广西|内蒙古)(.*?)(市|地区|州|盟).*""".r
val city_area_pattern = """^(.*?)市(.*?)区|县.*""".r
val city_pattern = "^(.*?)市$".r

for (line <- Source.fromFile("data/cz.txt", "utf-8").getLines().filter(_.trim() != "")) {
  val a = line.trim().split(" ").filter(_.trim() != "")
  val (ip_begin, ip_end, address, str2) = (a(0), a(1), a(2), a(3))
  println(line)
  var new_address = address

  address match {    
  	case university_pattern(university) => println("\t%s".format(university))
    case province_city_pattern(province, city, city_title) => println("\t%s省.%s%s".format(province, city, city_title))
    case province_pattern(province) => println("\t%s省".format(province))
    case ex_province_pattern(province) => println("\t%s".format(province))    
    case ex_city_pattern(province_short_name, city, city_title) =>
      	println("\t%s.%s%s".format(province_short_name, city, city_title))
    case city_area_pattern(city,area) => println("\t%s市".format(city))
    case city_pattern(city) => println("\t%s市".format(city))
    case _ => println("\t%s".format(address))
  }
}

