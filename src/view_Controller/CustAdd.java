package view_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.DBQuery;
import utils.TableViewOLUtil;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustAdd {
    public TextField addressTxt;
    public TextField nameTxt;
    public TextField phoneTxt;
    public TextField address2Txt;
    public TextField postalTxt;
    public TextField cityTxt;
    public TextField countryTxt;

    public void onActionAddCustomer(ActionEvent actionEvent) throws SQLException {
        //Check if a text field was left blank
        if (addressTxt.getText().isEmpty() || nameTxt.getText().isEmpty() || phoneTxt.getText().isEmpty() || address2Txt.getText().isEmpty() ||
            postalTxt.getText().isEmpty() || cityTxt.getText().isEmpty() || countryTxt.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entry error");
            alert.setContentText("Please fill in all text fields");
            alert.showAndWait();
            return;
        }
        // get values from textfields
        String address = addressTxt.getText();
        String name = nameTxt.getText();
        String phone = phoneTxt.getText();
        String address2 = address2Txt.getText();
        String postal = postalTxt.getText();
        String city = cityTxt.getText();
        String country = countryTxt.getText();

        //Check each table for duplicates, by order of dependence
        //country
        DBQuery.setPreparedStatement("SELECT * FROM country WHERE country = '" + country + "';");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        if (!(ps.getResultSet().next())) {
            DBQuery.setPreparedStatement("INSERT INTO country (country, createDate, createdBy, lastUpdateBy) VALUES (?, NOW(), ?, ?);");
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, country);
            ps.setString(2, User.getUserName());
            ps.setString(3, User.getUserName());
            ps.execute();
        }
        //city
        DBQuery.setPreparedStatement("SELECT * FROM city WHERE city = '" + city + "';");
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        if (!(ps.getResultSet().next())) {
            DBQuery.setPreparedStatement("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy) VALUES (?, (SELECT countryId FROM country WHERE country =?), NOW(), ?, ?); ");
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, city);
            ps.setString(2, country);
            ps.setString(3, User.getUserName());
            ps.setString(4, User.getUserName());
            ps.execute();
        }
        //address
        DBQuery.setPreparedStatement("SELECT * FROM address WHERE address = '" + address + "';");
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        if (!(ps.getResultSet().next())) {
            DBQuery.setPreparedStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES (?, ?, (SELECT cityId FROM city WHERE city =?), ?, ?, NOW(), ?, ?); ");
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, address);
            ps.setString(2, address2);
            ps.setString(3, city);
            ps.setString(4, postal);
            ps.setString(5, phone);
            ps.setString(6, User.getUserName());
            ps.setString(7, User.getUserName());
            ps.execute();
        }
        //customer
        DBQuery.setPreparedStatement("SELECT * FROM customer WHERE customerName = '" + name + "';");
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        if (!(ps.getResultSet().next())) {
            DBQuery.setPreparedStatement("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES (?, (SELECT addressId FROM address WHERE address = ?), 1, NOW(), ?, ?);");
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, User.getUserName());
            ps.setString(4, User.getUserName());
            ps.execute();
        }

        TableViewOLUtil.updateCustList();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Added");
        alert.setContentText("Customer Added!");
        alert.showAndWait();

    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
