package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //jdbc URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05qsf";
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn= null;
    //username
    private static final String username = "U05qsf";
    //password
    private static String password = "53688581077";

    public static Connection getConnection()
    {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection()
    {
        try {
            conn.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
