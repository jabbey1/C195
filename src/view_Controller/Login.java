package view_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.DBQuery;
import utils.TimeZoneUtil;
import java.io.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login {
    @FXML
    private TextField passTxt;
    @FXML
    private TextField userTxt;
    Stage stage;
    Parent scene;
    @FXML
    private Button exitButton;
    @FXML
    private Label passLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Button loginButton;


    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());


    //@Override
    public void initialize() {
        //Locale.setDefault(new Locale("es")); //Language Testing
        //System.out.println(Locale.getDefault());
        //Switch labels based on localization
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        passLabel.setText(rb.getString("password"));
        userLabel.setText(rb.getString("username"));
        exitButton.setText(rb.getString("exitButton"));
        loginButton.setText(rb.getString("loginButton"));
    }

    public void onActionLogin(ActionEvent actionEvent) throws SQLException, IOException {
        if (userTxt.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("loginerror"));
            alert.setContentText(rb.getString("noname"));
            alert.showAndWait();
            return;
        }
        if (passTxt.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("loginerror"));
            alert.setContentText(rb.getString("nopass"));
            alert.showAndWait();
            return;
        }
        //SELECT STATEMENT IS: SELECT password FROM user WHERE userName = 'admin';
        DBQuery.setPreparedStatement("SELECT * FROM user WHERE userName = '" + userTxt.getText() + "';");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        //Check password
        while (rs.next()) {
            if (rs.getString("password").equals(passTxt.getText())) {
                User.setUserName(userTxt.getText());
                User.setUserId(rs.getInt("userId"));
                //check for appointments happening within 15 minutes
                this.appCheck();
                //Login logging here
                Timestamp loginTime = Timestamp.valueOf(LocalDateTime.now());
                PrintWriter pw = new PrintWriter(new FileWriter("log.txt", true));
                pw.println("");
                pw.append(loginTime.toString());
                pw.close();
                // switch scenes here
                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(rb.getString("loginerror"));
        alert.setContentText(rb.getString("incorrect"));
        alert.showAndWait();
    }

    public void appCheck() throws SQLException {
        DBQuery.setPreparedStatement("SELECT * FROM appointment");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next())
        {
            //gets distance of time in minutes until next meeting
            long time = TimeZoneUtil.toGMT(LocalDateTime.now()).until(rs.getTimestamp("start").toLocalDateTime(), ChronoUnit.MINUTES);
            //check to see if distance to meeting is within 15 minutes
            if ((0 <= time) && (time <= 15))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("imminent"));
                alert.setContentText(rb.getString("imminent2"));
                alert.showAndWait();
                return;
            }
        }
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
