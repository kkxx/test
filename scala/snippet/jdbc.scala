import java.sql.{ DriverManager, Connection, Statement, PreparedStatement, ResultSet }
import javax.sql.DataSource

val url = "jdbc:mysql://127.0.0.1:3306/"
val usr = ""
val pwd = ""

//classOf[com.mysql.jdbc.Driver]
Class.forName("com.mysql.jdbc.Driver")
var conn = DriverManager.getConnection(url, usr, pwd)
try {
	val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
	val rs = statement.executeQuery("SELECT * FROM version_content LIMIT 5")
	while (rs.next) {
		println(rs)
	}
} finally {
	conn.close
}
