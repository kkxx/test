class StringSplitToTuple(s: String) {
  def splitToTuple(ss: String) = {
    s.split(ss) match {
      case Array(str1, str2, str3) => (str1, str2, str3)
      case _ => error("match miss")
    }
  }
}

implicit def splitToTuple(ss: String) = new StringSplitToTuple(ss)

//val (str1, str2, str3) =  "io,println,xxxxxxx".splitToTuple(",")
//println(str1, str2, str3)

val (str1, str2) = x = "io,println,xxxxxxx".split(",") match {
    case Array(str1, str2, _*) => (str1, str2)
}
println(str1, str2)

//  error: bad use of _* (a sequence pattern must be the last pattern)
// val (strb, stre) = x = "io,println,xxxxxxx,yyyyyy".split(",") match {
//     case Array(strb, _*, stre) => (strb, stre)
// }
// println(strb, stre)