package view_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import model.User;
import utils.DBQuery;
import utils.TableViewOLUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustEdit implements Initializable {
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField address2Txt;
    @FXML
    private Label cityTxt;
    @FXML
    private Label countryTxt;
    @FXML
    private TableView<Customer> custTableView;
    @FXML
    private TextField postalTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private TableColumn<Customer, String> custCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //populate customer table
        try {
            TableViewOLUtil.updateCustList();
            custTableView.setItems(TableViewOLUtil.getCustOL());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        custCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void onActionDelete(ActionEvent actionEvent) {
        Customer selectedCust = custTableView.getSelectionModel().getSelectedItem();
        //Cofirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete customer " + selectedCust.getName() + "?");
        ButtonType b1 = new ButtonType("Delete");
        ButtonType b2 = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(b1, b2);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == b2)
        {
            return;
        }
        //start Delete here
        //include warning for foreign key
        try {
            DBQuery.setPreparedStatement("DELETE FROM customer WHERE customerId = '" + selectedCust.getId() + "';");
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            TableViewOLUtil.updateCustList();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Delete Error");
            alert2.setHeaderText(null);
            alert2.setContentText("You may not delete this customer, as they have an active appointment.  Delete the appointment first to proceed.");
            alert2.showAndWait();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent actionEvent) throws SQLException {
        Customer selectedCust = custTableView.getSelectionModel().getSelectedItem();

        //Check if a text field was left blank
        if (addressTxt.getText().isEmpty() || nameTxt.getText().isEmpty() || phoneTxt.getText().isEmpty() || address2Txt.getText().isEmpty() ||
                postalTxt.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entry error");
            alert.setContentText("Please fill in all text fields");
            alert.showAndWait();
            return;
        }

        //check if it's a new address
        DBQuery.setPreparedStatement("SELECT * FROM address WHERE address = '" + addressTxt.getText() + "';");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        int addId;
        if (!(ps.getResultSet().next())) {
            DBQuery.setPreparedStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES (?, ?, (SELECT cityId FROM city WHERE city =?), ?, ?, NOW(), ?, ?); ");
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, addressTxt.getText());
            ps.setString(2, address2Txt.getText());
            ps.setString(3, cityTxt.getText());
            ps.setString(4, postalTxt.getText());
            ps.setString(5, phoneTxt.getText());
            ps.setString(6, User.getUserName());
            ps.setString(7, User.getUserName());
            ps.execute();
        }
        DBQuery.setPreparedStatement("UPDATE customer SET customerName = ?, addressId = (SELECT addressId FROM address WHERE address = ?), lastUpdateBy = ? WHERE customerId = ?");
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, nameTxt.getText());
        ps.setString(2, addressTxt.getText());
        ps.setString(3, User.getUserName());
        ps.setString(4, String.valueOf(selectedCust.getId()));
        ps.execute();
        TableViewOLUtil.updateCustList();
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void onActionSelectCust(MouseEvent mouseEvent) {
        Customer selectedCust = custTableView.getSelectionModel().getSelectedItem();
        nameTxt.setText(selectedCust.getName());
        addressTxt.setText(selectedCust.getAddress());
        address2Txt.setText(selectedCust.getAddress2());
        cityTxt.setText(selectedCust.getCity());
        countryTxt.setText(selectedCust.getCountry());
        postalTxt.setText(selectedCust.getPostal());
        phoneTxt.setText(selectedCust.getPhone());
    }
}
