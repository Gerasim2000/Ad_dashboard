package org.group5.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.group5.GUIController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class WelcomeController {
    public Button tutorialBtn;
    public Button startBtn;
    public MenuItem settingsBtn;
    public MenuItem quitBtn;
    public MenuItem helpBtn;

    public void openTutorial(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("UserGuide.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

    public void startDashboard(ActionEvent actionEvent) throws SQLException, IOException {

        try {
            Stage welcomeStage = (Stage) startBtn.getScene().getWindow();
            // do what you have to do
            welcomeStage.close();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/dataImport.fxml"));

            stage.setTitle("Data Import");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../Styles/stylesPURPLE.css").toExternalForm());
            stage.setMinWidth(420);
            stage.setMinHeight(300);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSettings(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/settings.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../Styles/" + GUIController.stylesheetColour).toExternalForm());


            SettingsController settingsController = loader.getController();
            settingsController.initData();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void showHelp(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/help.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../stylesPURPLE.css").toExternalForm());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
