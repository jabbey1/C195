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
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

public class WeekView implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn timeCol;
    @FXML
    private TableColumn custCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn contactCol;
    @FXML
    private Label weekTxt;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Find and display the date of the sunday of this week
        LocalDate now = LocalDate.now();
        TemporalField US = WeekFields.of(Locale.US).dayOfWeek();
        weekTxt.setText(String.valueOf(now.with(US, 1)));
        LocalDate sun = now.with(US, 1);
        LocalDate next = sun.plusWeeks(1);

        // Tables seperate out appointments for this week
        ObservableList<Appointment> filtList = FXCollections.observableArrayList();
        // lambda to search through appointments to find ones in the current month
        TableViewOLUtil.getAppOL().forEach(appointment -> {
            LocalDate app = appointment.getStart().toLocalDate();
            if ((app.isAfter(sun) || app.isEqual(sun)) && app.isBefore(next))
                filtList.add(appointment);
        });
        //Populate table
        tableView.setItems(filtList);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
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
