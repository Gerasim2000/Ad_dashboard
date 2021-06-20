package org.group5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.group5.Controllers.WelcomeController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class MainApp extends Application {

    public static String stylesheetColour = "stylesWelcome.css";
//    public static String stylesheetFont = "stylesWelcome.css";

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/welcome.fxml"));

            primaryStage.setTitle("Dashboard");
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("Styles/" + stylesheetColour).toExternalForm());
//            scene.getStylesheets().add(getClass().getResource("Styles/" + stylesheetFont).toExternalForm());
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(450);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
