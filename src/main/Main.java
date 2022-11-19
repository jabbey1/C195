package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.TableViewOLUtil;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Connection conn = DBConnection.getConnection();
        TableViewOLUtil.updateCustList();
        TableViewOLUtil.updateAppList();

        //todo Switch back to me!
        Parent root = FXMLLoader.load(getClass().getResource("/view_Controller/Login.fxml"));
        //temp load to main menu for speed:
        //Parent root = FXMLLoader.load(getClass().getResource("/view_Controller/MainMenu.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
