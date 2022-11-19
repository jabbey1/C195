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

public class ConsultantReport implements Initializable {

    @FXML
    private TextField greyTxt;
    @FXML
    private TextField plumTxt;
    @FXML
    private TextField brownTxt;
    @FXML
    private TextField greenTxt;
    @FXML
    private TextField whiteTxt;

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        try {
            DBQuery.setPreparedStatement("SELECT COUNT(type) FROM appointment WHERE contact = ?;");
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs;
            ps.setString(1, "Dr. White");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            whiteTxt.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Dr. Green");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            greenTxt.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Dr. Brown");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            brownTxt.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Dr. Plum");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            plumTxt.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Dr. Grey");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            greyTxt.setText(String.valueOf(rs.getInt(1)));
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
