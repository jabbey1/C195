package view_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.DBConnection;
import java.io.IOException;
import java.sql.SQLException;

public class MainMenuController {
    public Label Hello;
    Stage stage;
    Parent scene;


    public void onActionAddCustomer(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustAdd.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionViewCustomers(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustEdit.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppAdd.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionViewAppointments(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppEdit.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionMonthView(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MonthView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionWeekView(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("WeekView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionViewTypeReport(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("TypeReport.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionViewConsultantReport(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("ConsultantReport.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionViewExtraReport(ActionEvent actionEvent) throws IOException, SQLException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("ExtraReport.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionLogout(ActionEvent actionEvent) {
        DBConnection.closeConnection();
        System.exit(0);
    }
}
