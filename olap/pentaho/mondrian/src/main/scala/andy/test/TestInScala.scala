package andy.test

import scala.collection.immutable._
import java.io.{ByteArrayOutputStream, PrintWriter}
import java.lang.String
import java.util.Properties
import java.sql.Connection
import mondrian.olap.Util
import org.olap4j._
import org.apache.commons.configuration.XMLConfiguration
import org.apache.log4j.Logger

// http://stackoverflow.com/questions/7539831/scala-draw-table-to-console
object Tabulator {
  def format(table: Seq[Seq[Any]]) = table match {
    case Seq() => ""
    case _ =>
      val sizes = for (row <- table) yield (for (cell <- row) yield if (cell == null) 0 else cell.toString.length)
      val colSizes = for (col <- sizes.transpose) yield col.max
      val rows = for (row <- table) yield formatRow(row, colSizes)
      formatRows(rowSeparator(colSizes), rows)
  }

  def formatRows(rowSeparator: String, rows: Seq[String]): String = (
    rowSeparator ::
      rows.head ::
      rowSeparator ::
      rows.tail.toList :::
      rowSeparator ::
      List()).mkString("\n")

  def formatRow(row: Seq[Any], colSizes: Seq[Int]) = {
    val cells = (for ((item, size) <- row.zip(colSizes)) yield if (size == 0) "" else ("%" + size + "s").format(item))
    cells.mkString("|", "|", "|")
  }

  def rowSeparator(colSizes: Seq[Int]) = colSizes map {
    "-" * _
  } mkString("+", "+", "+")
}

object TESTINSCALA {
  def pretty_print_cellset(cellSet: CellSet, rowTitle: String): String = {
    val cellSetAxes = cellSet.getAxes
    System.out.println("")
    val columnsAxis: CellSetAxis = cellSetAxes.get(Axis.COLUMNS.axisOrdinal)
    System.out.print("\t")
    import scala.collection.JavaConversions._

    var tbl = scala.collection.mutable.ListBuffer[List[String]]()
    var heading = scala.collection.mutable.ListBuffer[String](rowTitle)
    columnsAxis.getPositions.zipWithIndex.foreach {
      case (position, i) => {
        val m = position.getMembers.get(0)
        //val dimension = m.getDimension.getName
        //val name = m.getName
        //heading.append("[%s].[%s]".format(dimension, name))
        heading.append(m.getUniqueName)
      }
    }
    tbl.append(heading.toList)
    val rowsAxis: CellSetAxis = cellSetAxes.get(Axis.ROWS.axisOrdinal)

    var cellOrdinal: Int = 0
    import scala.collection.JavaConversions._
    for (rowPosition <- rowsAxis.getPositions) {
      val member = rowPosition.getMembers.get(0)
      var row = scala.collection.mutable.ListBuffer[String]()
      row.append(member.getUniqueName)
      //      row.append(member.getName)

      for (columnPosition <- columnsAxis.getPositions) {
        val cell: Cell = cellSet.getCell(cellOrdinal)
        val coordList: java.util.List[Integer] = cellSet.ordinalToCoordinates(cellOrdinal)
        cellOrdinal += 1
        row.append(cell.getFormattedValue)
      }
      tbl.append(row.toList)
    }
    val ot = Tabulator.format(tbl.toList)
    return "\n" + ot;
  }
}

class TestInScala {
  var logger = Logger.getLogger(classOf[TestInScala])

  def test_connection_execute() {
    println(classOf[TestInScala] + "." + "test_connection_execute");
    val xmlPath = getClass.getResource("/test.xml").toString;
    println(xmlPath);
    val mdxPath = getClass.getResource("/test.mdx").toString;
    println(mdxPath)
    val mdxString = scala.io.Source.fromURI(new java.net.URI(mdxPath)).mkString
    println(mdxString)

    val config = new XMLConfiguration();
    config.setDelimiterParsingDisabled(true);
    config.load("app.xml")
    var list = new Util.PropertyList
    list.put("Provider", "mondrian");
    list.put("Jdbc", config.getString("database.Jdbc"));
    list.put("JdbcDrivers", "com.mysql.jdbc.Driver");
    list.put("JdbcUser", config.getString("database.JdbcUser"))
    println(config.getString("database.JdbcPassword"))
    list.put("JdbcPassword", config.getString("database.JdbcPassword"))
    list.put("Catalog", xmlPath);
    var connection = mondrian.olap.DriverManager.getConnection(list, null)
    var query = connection.parseQuery(mdxString)
    val rs = connection.execute(query);
    var baos = new ByteArrayOutputStream()
    // var pw = new PrintWriter(System.out);
    var pw = new PrintWriter(baos);
    rs.print(pw);
    pw.flush();
    var s = ""
    pw.write(s)
    logger.info(baos.toString)
  }


  def test_olap4j() {
    Class.forName("mondrian.olap4j.MondrianOlap4jDriver")
    val xmlPath: String = getClass.getResource("/test.xml").toString
    val config = new XMLConfiguration();
    config.setDelimiterParsingDisabled(true);
    config.load("app.xml")
    val prop: Properties = new Properties
    prop.put("JdbcUser", config.getString("database.JdbcUser"))
    prop.put("JdbcPassword", config.getString("database.JdbcPassword"))
    val url: String = "jdbc:mondrian:Jdbc=" + config.getString("database.Jdbc") +
      ";Catalog=" + xmlPath +
      ";JdbcDrivers=com.mysql.jdbc.Driver"
    println(url)
    val connection: Connection = java.sql.DriverManager.getConnection(url, prop)
    val wrapper: OlapWrapper = connection.asInstanceOf[OlapWrapper]
    val olapConnection: OlapConnection = wrapper.unwrap(classOf[OlapConnection])
    val statement: OlapStatement = olapConnection.createStatement


    //    val mdxString = """SELECT
    //      {
    //        [Measures].[server_id],
    //        [Measures].[os_version_id],
    //        [Measures].[os_version]
    //      } ON AXIS(0),
    //      {
    //        [idc].[province].[1013],
    //        [idc].[province].[1014]
    //      } ON AXIS(1)
    //      FROM [server]""";

    //    val mdxString = """SELECT
    //      {
    //        [Measures].[server_id],
    //        [Measures].[os_version_id],
    //        [Measures].[os_version]
    //      } ON AXIS(0),
    //      {
    //        [idc].[province].[1013],
    //        [idc].[province].[1014]
    //      } ON AXIS(1)
    //      FROM [server]""";

    val mdxPath = getClass.getResource("/test.mdx").toString;
    println(mdxPath)
    val mdxString = scala.io.Source.fromURI(new java.net.URI(mdxPath)).mkString
    println(mdxString)

    val cellSet: CellSet = statement.executeOlapQuery(mdxString)
    val ot = TestInScala.pretty_print_cellset(cellSet, "[idc].[province]")
    logger.info(ot)
  }


}