package view_Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Appointment;
import model.User;
import utils.DBQuery;
import utils.TableViewOLUtil;
import utils.TimeZoneUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppEdit implements Initializable {

    @FXML
    private TableView<Appointment> appTableView;

    @FXML
    private TableColumn<Appointment, String> custCol;

    @FXML
    private TableColumn<Appointment, String> cityCol;

    @FXML
    private TableColumn<Appointment, LocalDate> dateCol;

    @FXML
    private ComboBox<String> consultantBox;

    @FXML
    private ComboBox<String> locationBox;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private ComboBox<Integer> lengthBox;

    @FXML
    private TextField titleText;

    @FXML
    private ComboBox<String> timeBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TableViewOLUtil.updateAppList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //populate appointment table
        appTableView.setItems(TableViewOLUtil.getAppOL());
        custCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("custCity"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        //populate selection boxes
        typeBox.getItems().addAll("Consultation", "Routine", "Debriefing", "Intervention");
        consultantBox.getItems().addAll("Dr. White", "Dr. Green", "Dr. Brown", "Dr. Plum", "Dr. Grey");
        locationBox.getItems().addAll("In office", "Remote", "Other");
        lengthBox.getItems().addAll(15, 30, 45, 60);
    }

    @FXML
    void onActionDateChange(ActionEvent event) throws SQLException {
        //following code from AppAdd onactiondatepicked method
        //clear time dropdown menu
        timeBox.getItems().clear();
        //list of possible times
        ObservableList<String> appTimes = FXCollections.observableArrayList("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30");
        ObservableList<String> addList = FXCollections.observableArrayList();
        addList.addAll(appTimes);
        //Convert and select all apps from DB on the date selected
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DBQuery.setPreparedStatement("SELECT * FROM appointment WHERE DATE(start) = ?");
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, datePicker.getValue().toString());
        ps.execute();
        ResultSet rs = ps.getResultSet();
        int day = datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue().getMonthValue();
        int year = datePicker.getValue().getYear();
        LocalDate date = LocalDate.of(year, month, day);
        //check each appointment on the selected day and find used times
        //lambda to cycle through the list of times that are in business hours
        appTimes.forEach(s -> {
            LocalTime time = LocalTime.parse(s, formatter);
            LocalDateTime dTime = LocalDateTime.of(date, time);
            LocalDateTime gmtTime = TimeZoneUtil.toGMT(dTime);
            try {
                rs.beforeFirst();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {while (rs.next())
            {
                if (((gmtTime.isAfter(rs.getTimestamp("start").toLocalDateTime()) || gmtTime.isEqual(rs.getTimestamp("start").toLocalDateTime()))
                        && gmtTime.isBefore(rs.getTimestamp("end").toLocalDateTime())))
                {
                    addList.remove(s);
                }
            }}
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        timeBox.setDisable(false);
        timeBox.getItems().addAll(addList);

    }

    @FXML
    void onActionDelete(ActionEvent event) throws SQLException {
        Appointment selectedApp = appTableView.getSelectionModel().getSelectedItem();
        //Cofirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this appointment?");
        ButtonType b1 = new ButtonType("Delete");
        ButtonType b2 = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(b1, b2);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == b2)
        {
            return;
        }
        //start Delete here
        DBQuery.setPreparedStatement("DELETE FROM appointment WHERE appointmentId = '" + selectedApp.getId() + "';");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        TableViewOLUtil.updateAppList();
    }

    @FXML
    void onActionEdit(ActionEvent event) throws SQLException {
        Appointment a = appTableView.getSelectionModel().getSelectedItem();
        if ( consultantBox.getValue() == null || locationBox.getValue() == null || typeBox.getValue() == null || lengthBox.getValue() == null ||
                titleText.getText() == null || datePicker.getValue() == null || timeBox.getValue() == null || descriptionText.getText() == null )
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Entry Error");
            alert.setContentText("Please do not leave any appointment data blank.");
            alert.showAndWait();
            return;
        }
        if (datePicker.getValue().isBefore(LocalDate.now()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Entry Error");
            alert.setContentText("The date you have selected is in the past.  Please choose a new date.");
            alert.showAndWait();
            return;
        }

        //assemble time from various fields
        LocalDate date = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(timeBox.getValue(), formatter);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        //convert to SQL format and to GMT time
        java.sql.Timestamp sqlTimeStart = Timestamp.valueOf(TimeZoneUtil.toGMT(dateTime));
        //do the same but also calculate for the end time
        Long length = Long.parseLong(lengthBox.getValue().toString());
        java.sql.Timestamp sqlTimeEnd = Timestamp.valueOf(TimeZoneUtil.toGMT(dateTime.plusMinutes(length)));

        DBQuery.setPreparedStatement("UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?, " +
                                    "start = ?, end = ?, lastUpdateBy = ? WHERE appointmentId = ?");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, titleText.getText());
        ps.setString(2, descriptionText.getText());
        ps.setString(3, locationBox.getValue());
        ps.setString(4, consultantBox.getValue());
        ps.setString(5, typeBox.getValue());
        ps.setTimestamp(6, sqlTimeStart); //start time
        ps.setTimestamp(7, sqlTimeEnd); //end time
        ps.setString(8, User.getUserName());
        ps.setInt(9, a.getId());

        ps.execute();
        TableViewOLUtil.updateAppList();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Appointment Edited!");
        alert.showAndWait();

    }

    @FXML
    void onActionLengthChange(ActionEvent event) {
        //Probably not going to use? implemented during edit
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void onActionAppSelect(MouseEvent mouseEvent) {
        Appointment a = appTableView.getSelectionModel().getSelectedItem();
        consultantBox.setValue(a.getContact());
        locationBox.setValue(a.getLocation());
        typeBox.setValue(a.getType());
        titleText.setText(a.getTitle());
        datePicker.setValue(a.getStart().toLocalDate());
        timeBox.setValue(TimeZoneUtil.fromGMT(a.getStart()).toLocalTime().toString());
        descriptionText.setText(a.getDescription());
        lengthBox.setValue((int) a.getStart().until(a.getEnd(), ChronoUnit.MINUTES));

    }
}
