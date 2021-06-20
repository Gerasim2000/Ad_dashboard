package org.group5.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group5.Controller;
import org.group5.GUIController;
import org.group5.Model;

import java.io.File;
import java.sql.Date;


public class DataImportController extends GUIController {
    public Button impressionBtn;
    private File impressionFile;
    private File clickFile;
    private File serverFile;
    @FXML
    private Label impLabel;
    @FXML
    private Label clickLabel;
    @FXML
    private Label serverLabel;


    public File fileChoose(ActionEvent event) throws Exception {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File dataFile = fileChooser.showOpenDialog(stage);
        if (dataFile != null) {
            return dataFile;
        } else {
            throw new Exception("No file selected");
        }
    }

    public void chooseImpression(ActionEvent event) {
        try {
            impressionFile = fileChoose(event);
            impLabel.setText("Impression Log: " + impressionFile.getName());
        } catch (Exception e) {
            alertFail(Alert.AlertType.WARNING, e, "File Choose Warning", "You did not choose an impression log file");
        }
    }

    public void chooseClick(ActionEvent event) {
        try {
            clickFile = fileChoose(event);
            clickLabel.setText("Click Log: " + clickFile.getName());
        } catch (Exception e) {
            alertFail(Alert.AlertType.WARNING, e, "File Choose Warning", "You did not choose a click log file");
        }
    }

    public void chooseServer(ActionEvent event) {
        try {
            serverFile = fileChoose(event);
            serverLabel.setText("Server Log: " + serverFile.getName());
        } catch (Exception e) {
            alertFail(Alert.AlertType.WARNING, e, "File Choose Warning", "You did not choose a server log file");
        }
    }

    public void importData() {
        try {
            Model model = new Model();
            Controller controller = new Controller(model);
            controller.loadFiles(impressionFile.getPath(), clickFile.getPath(), serverFile.getPath());
            Stage stage = (Stage) impressionBtn.getScene().getWindow();
            stage.close();

            Stage menuStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/menu.fxml"));
            menuStage.setTitle("Dashboard");
            Scene scene = new Scene(loader.load());
            menuStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../Styles/" + stylesheetColour).toExternalForm());
            scene.getStylesheets().add(getClass().getResource("../Styles/" + stylesheetFont).toExternalForm());


            GUIController guiController = loader.getController();
            guiController.initData(controller);
            guiController.start = new Date(controller.getLastDate().getTime() - 86400000);
            guiController.end = controller.getLastDate();
            guiController.interval = 3000000;
            guiController.showCharts();
            guiController.importantMetric();

            menuStage.setMinWidth(800);
            menuStage.setMinHeight(700);
            menuStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alertFail(Alert.AlertType.ERROR, e, "File loading error", "Failed to load files");
        }
    }
}
