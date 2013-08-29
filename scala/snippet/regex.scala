import scala.util.matching.Regex
val pattern = new Regex("""(\w*) (\w*)""", "firstName", "lastName");
val result = pattern.findFirstMatchIn("ErPing Wu").get;
println("firstName=%s, lastName=%s".format(result.group("firstName"), result.group("lastName")));