package andy.test;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.common.base.Joiner;
import com.google.common.io.Closer;

import org.olap4j.metadata.Member;
import org.olap4j.*;

public class TestInJava {
  public void test() {
    // http://www.docjar.com/html/api/org/olap4j/sample/SimpleQuerySample.java.html
    System.out.println(TestInJava.class + "test");
    Closer closer = Closer.create();
    try {
      Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
      //Class.forName("mondrian.olap4j.Driver");
      String xmlPath = getClass().getResource("/test.xml").toString();
      //System.out.println(xmlPath);
      java.util.Properties prop = new java.util.Properties();
      prop.put("JdbcUser", "op_cmdb_web");
      prop.put("JdbcPassword", "Tv35Rx6+");
      String url = "jdbc:mondrian" +
              ":Jdbc=jdbc:mysql://183.136.136.166:6208/cmdb?" +
              "&characterEncoding=utf-8&useUnicode=true" +
              ";Catalog=" + xmlPath +
              ";JdbcDrivers=com.mysql.jdbc.Driver";

      String mdxString = new String("select\n" +
              "      {\n" +
              "        [Measures].[server_id],\n" +
              "        [Measures].[os_version_id],\n" +
              "        [Measures].[os_version]\n" +
              "      } ON COLUMNS,\n" +
              "      {\n" +
              "        [idc].[province].[1013],\n" +
              "        [idc].[province].[1014]\n" +
              "      } ON ROWS\n" +
              "      from server");


      Connection connection = DriverManager.getConnection(url, prop);
      OlapWrapper wrapper = (OlapWrapper) connection;
      OlapConnection olapConnection = wrapper.unwrap(OlapConnection.class);
      OlapStatement statement = olapConnection.createStatement();
      CellSet cellSet = statement.executeOlapQuery(mdxString);
      andy.test.TestInScala.pretty_print_cellset(cellSet, "[idc].[province]");

    } catch (ClassNotFoundException
            | SQLException
            | UnsupportedOperationException
            ex) {
      System.out.println(ex.getMessage());
    }
  }
}