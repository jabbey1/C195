package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQuery {

    private static PreparedStatement statement; //statement reference
    private static Connection conn = DBConnection.getConnection();
    //Create statement object
    public static void setPreparedStatement(String sqlStatement) throws SQLException {

        statement = conn.prepareStatement(sqlStatement);
    }

    //Return statement object
    public static PreparedStatement getPreparedStatement()
    {

        return statement;
    }
}
