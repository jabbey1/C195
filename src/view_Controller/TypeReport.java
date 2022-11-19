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

public class TypeReport implements Initializable {
    @FXML
    private TextField interventionText;
    @FXML
    private TextField routineText;
    @FXML
    private TextField consultationText;
    @FXML
    private TextField debriefingText;

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        try {
            DBQuery.setPreparedStatement("SELECT COUNT(type) FROM appointment WHERE type = ?;");
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs;
            ps.setString(1, "Intervention");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            interventionText.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Routine");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            routineText.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Consultation");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            consultationText.setText(String.valueOf(rs.getInt(1)));
            ps.setString(1, "Debriefing");
            ps.executeQuery();
            rs = ps.getResultSet();
            rs.next();
            debriefingText.setText(String.valueOf(rs.getInt(1)));
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
