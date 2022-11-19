package view_Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.TableViewOLUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MonthView implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private Label monthTxt;
    @FXML
    private TableColumn contactCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn custCol;
    @FXML
    private TableColumn dateCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Find and display the date of the sunday of this month
        LocalDate now = LocalDate.now();
        monthTxt.setText(String.valueOf(now.getMonth()));

        // Tables seperate out appointments for this month
        ObservableList<Appointment> filtList = FXCollections.observableArrayList();
        // lambda to search through all appointments for ones in the current month
        TableViewOLUtil.getAppOL().forEach(appointment -> {
            LocalDate app = appointment.getStart().toLocalDate();
            if (app.getMonth() == now.getMonth())
                filtList.add(appointment);
        });
        //Populate table
        tableView.setItems(filtList);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        custCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
