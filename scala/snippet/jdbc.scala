import java.sql.{ DriverManager, Connection, Statement, PreparedStatement, ResultSet }
import javax.sql.DataSource

val init_url = "jdbc:mysql://127.0.0.1:3306?&characterEncoding=utf-8&useUnicode=true"
val url = "jdbc:mysql://127.0.0.1:3306/mytest?&characterEncoding=utf-8&useUnicode=true"
val usr = "root"
val pwd = ""

//classOf[com.mysql.jdbc.Driver]
Class.forName("com.mysql.jdbc.Driver")


def insert_iplib(begin_ip:String, end_ip:String, begin_int_ip:Long, end_int_ip:Long,
	province:String, city:String, czstr:String) = {
	var conn = DriverManager.getConnection(url, usr, pwd)
	try {
		val prep = conn.prepareStatement("""
			insert into mytest.iplib 
			(begin_ip, end_ip, begin_int_ip, end_int_ip, province, city, czstr) 
			values (?, ?, ?, ?, ?, ?, ?)
			""")
		prep.setString(1, begin_ip);
		prep.setString(2, end_ip);
		prep.setLong(3, begin_int_ip);
		prep.setLong(4, end_int_ip);
		prep.setString(5, province);
		prep.setString(6, city);
		prep.setString(7, czstr);
		prep.executeUpdate
	} finally {
		conn.close
	}
}

def init() = {
	var conn = DriverManager.getConnection(init_url, usr, pwd)
	try {
		val db_prep = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS mytest default charset utf8 COLLATE utf8_general_ci")
		db_prep.executeUpdate
		val tbl_prep = conn.prepareStatement("""
				CREATE TABLE IF NOT EXISTS mytest.iplib (
					id int UNSIGNED NOT NULL auto_increment,
					begin_ip varchar(15) NOT NULL,
					end_ip varchar(15) NOT NULL,
					begin_int_ip bigint NOT NULL,
					end_int_ip bigint NOT NULL,
					province varchar(250),
					city varchar(250),
					czstr text,
					PRIMARY KEY  (id),
          UNIQUE KEY _unique_key (begin_int_ip, end_int_ip)
					) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE `utf8_bin`
			""")
		tbl_prep.executeUpdate
	} finally {
		conn.close
	}
}

init()
insert_iplib("222.80.122.50", "222.80.122.255", 3729816114L, 3729817599L, 
	"新疆", "和田地区", "222.80.122.50   222.80.127.255  新疆和田地区 电信ADSL")