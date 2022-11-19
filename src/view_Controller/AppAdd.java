package view_Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Customer;
import model.User;
import utils.DBQuery;
import utils.TableViewOLUtil;
import utils.TimeZoneUtil;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppAdd {

    @FXML
    private ChoiceBox<String> consultantChoiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private ChoiceBox<String> timeChoiceBox;
    @FXML
    private TextField cityText;
    @FXML
    private ComboBox<Customer> customerCombobox;
    @FXML
    private TextArea descriptionText;
    @FXML
    private TextField titleText;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private ComboBox<Integer> lengthComboBox;

    //@Override
    public void initialize() throws SQLException {
        //populate dropdown choiceboxes
        typeChoiceBox.getItems().addAll("Consultation", "Routine", "Debriefing", "Intervention");
        consultantChoiceBox.getItems().addAll("Dr. White", "Dr. Green", "Dr. Brown", "Dr. Plum", "Dr. Grey");

        locationComboBox.getItems().addAll("In office", "Remote", "Other");
        lengthComboBox.getItems().addAll(15, 30, 45, 60);
        customerCombobox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer cust) {
                return cust.getName();
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
        customerCombobox.getItems().setAll(TableViewOLUtil.getCustOL());
        //If you were getting contacts from DB:
        /*DBQuery.setPreparedStatement("SELECT * FROM appointment");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.getResultSet();
        while (rs.next())
        {
            consultantChoiceBox.getItems().add(rs.getString("contact"));
        }*/
        // if you are allowing city select:
        /*DBQuery.setPreparedStatement("SELECT * FROM city");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next())
        {
            cityChoiceBox.getItems().add(rs.getString("city"));
        }*/
    }

    @FXML
    void onActionAddAppointment(ActionEvent event) throws SQLException, IOException {
        //check for empty fields, and check that the selected date is not in the past
        if (customerCombobox.getValue() == null || consultantChoiceBox.getValue() == null || datePicker.getValue() == null
                || timeChoiceBox.getValue() == null || typeChoiceBox.getValue() == null)
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
        LocalTime time = LocalTime.parse(timeChoiceBox.getValue(), formatter);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        //convert to SQL format and to GMT time
        java.sql.Timestamp sqlTimeStart = Timestamp.valueOf(TimeZoneUtil.toGMT(dateTime));
        //do the same but also calculate for the end time
        Long length = Long.parseLong(lengthComboBox.getValue().toString());
        java.sql.Timestamp sqlTimeEnd = Timestamp.valueOf(TimeZoneUtil.toGMT(dateTime.plusMinutes(length)));


        DBQuery.setPreparedStatement("SELECT * FROM appointment");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.getResultSet();
        // if i want to do app conflicts by contact, then fix this next part
        /*while (rs.next())
        {
            Timestamp s = rs.getTimestamp("start");
            Timestamp e = rs.getTimestamp("end");
            if (rs.getString("contact").equals(consultantChoiceBox.getValue().toString())
                    && (sqlTimeStart.after(s) && sqlTimeStart.before(e) || sqlTimeEnd.after(s) && sqlTimeEnd.before(e)))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Entry Error");
                alert.setContentText("This appointment timeslot overlaps with another meeting, please choose a different time.");
                alert.showAndWait();
                return;
            }
        }*/

        //start SQL
        Customer cust = (Customer) customerCombobox.getValue();
        DBQuery.setPreparedStatement("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                    //INDEX HELPER:                                 1       2       3           4           5       6       7           8     9                 10          11
                                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, 'URL', ?, ?, NOW(), ?, ? )");
        ps = DBQuery.getPreparedStatement();
        ps.setInt(1, cust.getId());
        ps.setInt(2, User.getUserId());
        ps.setString(3, titleText.getText());
        ps.setString(4, descriptionText.getText());
        ps.setString(5, locationComboBox.getValue().toString());
        ps.setString(6, consultantChoiceBox.getValue().toString());
        ps.setString(7, typeChoiceBox.getValue().toString());
        ps.setTimestamp(8, sqlTimeStart);
        ps.setTimestamp(9,sqlTimeEnd);
        ps.setString(10, User.getUserName());
        ps.setString(11, User.getUserName());

        ps.execute();
        System.out.println("appt added");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Appointment Added!");
        alert.showAndWait();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    @FXML
    private void onActionSelect(ActionEvent actionEvent) throws SQLException {
        //Fill in the city text field whenever you select a new customer
        DBQuery.setPreparedStatement("SELECT city FROM city WHERE cityId = (SELECT cityId FROM address WHERE addressId = (SELECT addressId FROM customer WHERE customerName = ?))");
        PreparedStatement ps = DBQuery.getPreparedStatement();
        Customer cust = (Customer)customerCombobox.getValue();
        ps.setString(1, cust.getName());
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while(rs.next())
            cityText.setText(rs.getString("city"));
    }

    @FXML
    private void onActionDatePicked(ActionEvent actionEvent) throws SQLException {
        //clear time dropdown menu
        timeChoiceBox.getItems().clear();
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

        timeChoiceBox.setDisable(false);
        timeChoiceBox.getItems().addAll(addList);
    }
}
