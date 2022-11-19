package view_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExtraReport implements Initializable {
    @FXML
    private TextField custTxt;

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        // Create Report
        //count number of customers
        try {
            DBQuery.setPreparedStatement("SELECT COUNT(customerId) FROM customer;");
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();
            custTxt.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


}
