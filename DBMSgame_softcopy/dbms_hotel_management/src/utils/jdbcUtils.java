package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbcUtils {
  
  public static ResultSet getResultSet(String url, String user, String password, String query) throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection(url, user, password);
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(query);
  }

  public static void closeConnection(Connection conn) {
    if(conn == null) return;

    try { 
      conn.close(); 
    } catch (SQLException e) {
      e.printStackTrace(); 
    }
  }
}
