package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TableViewOLUtil {
    private static ObservableList<Customer> custOL = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appOL = FXCollections.observableArrayList();

    public static void updateCustList() throws SQLException {
        //reset list
        custOL.clear();
        //query DB for all entries from cust table
        DBQuery.setPreparedStatement("SELECT * FROM customer");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        // add rows from DB into OL
        while (rs.next())
        {
            int customerId = rs.getInt(1);
            String customerName = rs.getString(2);
            int addressId = rs.getInt(3);

            //get corresponding address entry
            DBQuery.setPreparedStatement("SELECT * FROM address WHERE addressId = '" + addressId + "';");
            PreparedStatement ps2 = DBQuery.getPreparedStatement();
            ps2.execute();
            ResultSet rs2 = ps2.getResultSet();
            rs2.next();
            String address = rs2.getString("address");
            String address2 = rs2.getString("address2");
            int cityId = rs2.getInt("cityId");
            String postal = rs2.getString("postalCode");
            String phone = rs2.getString("phone");

            //get corresponding city entry
            DBQuery.setPreparedStatement("SELECT * FROM city WHERE cityId = '" + cityId + "';");
            ps2 = DBQuery.getPreparedStatement();
            ps2.execute();
            rs2 = ps2.getResultSet();
            rs2.next();
            String city = rs2.getString("city");
            int countryId = rs2.getInt("countryId");

            //get corresponding country entry
            DBQuery.setPreparedStatement("SELECT * FROM country WHERE countryId = '" + countryId + "';");
            ps2 = DBQuery.getPreparedStatement();
            ps2.execute();
            rs2 = ps2.getResultSet();
            rs2.next();
            String country = rs2.getString("country");

            //use data for Customer constructor
            Customer c = new Customer(customerId, customerName, addressId, address, address2, city, country, postal, phone);

            custOL.add(c);
        }
    }

    public static void updateAppList() throws SQLException {
        //reset list
        appOL.clear();

        //query DB for all entries from cust table
        DBQuery.setPreparedStatement("SELECT * FROM appointment");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        // add rows from DB into OL
        while (rs.next())
        {
            int id = rs.getInt("appointmentId");
            int custId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            LocalDateTime startGMT = rs.getTimestamp("start").toLocalDateTime();
            LocalDateTime endGMT = rs.getTimestamp("end").toLocalDateTime();

            Appointment a = new Appointment(id, custId, userId, title, description, location, contact, type, url, startGMT, endGMT);
            appOL.add(a);
        }
    }

    public static ObservableList<Customer> getCustOL() throws SQLException {
        return custOL;
    }

    public static void setCustOL(ObservableList<Customer> custOL) {
        TableViewOLUtil.custOL = custOL;
    }

    public static ObservableList<Appointment> getAppOL() {
        return appOL;
    }

    public static void setAppOL(ObservableList<Appointment> appOL) {
        TableViewOLUtil.appOL = appOL;
    }
}
