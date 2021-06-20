package org.group5.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;
import org.group5.Controller;
import org.group5.GUIController;
import org.group5.MainApp;
import org.group5.Model;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {
    @FXML
    public ChoiceBox <String> fontSizePicker;
    public ChoiceBox <String>  colorPicker;
    public Button applyBtn;

    public void initData() {
        fontSizePicker.getItems().add("Small");
        fontSizePicker.getItems().add("Medium");
        fontSizePicker.getItems().add("Large");

        fontSizePicker.setValue("Medium");

        colorPicker.getItems().add("Purple");
        colorPicker.getItems().add("Black");
        colorPicker.getItems().add("Red");
        colorPicker.getItems().add("Blue");
        colorPicker.getItems().add("Green");

        colorPicker.setValue("Purple");
    }

    public void apply(ActionEvent actionEvent) throws IOException, SQLException {
        switch (fontSizePicker.getValue()) {
            case "Small" -> GUIController.stylesheetFont = "stylesSmall.css";
            case "Medium" -> GUIController.stylesheetFont = "stylesMedium.css";
            case "Large" -> GUIController.stylesheetFont = "stylesLarge.css";
        }

        switch (colorPicker.getValue()) {
            case "Red" -> GUIController.stylesheetColour = "stylesRED.css";
            case "Purple" -> GUIController.stylesheetColour = "stylesPURPLE.css";
            case "Black" -> GUIController.stylesheetColour = "stylesBLACK.css";
            case "Blue" -> GUIController.stylesheetColour = "stylesBLUE.css";
            case "Green" -> GUIController.stylesheetColour = "stylesGREEN.css";
        }

        openMenu();
    }

    private void openMenu() throws IOException, SQLException {
        Stage stage = (Stage) applyBtn.getScene().getWindow();
        stage.close();

        Stage menuStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/menu.fxml"));
        menuStage.setTitle("Dashboard");
        Scene scene = new Scene(loader.load());
        menuStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("../Styles/" + GUIController.stylesheetColour).toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../Styles/" + GUIController.stylesheetFont).toExternalForm());

        GUIController guiController = loader.getController();
        guiController.importantMetric();
        guiController.showCharts();

        menuStage.setMinWidth(800);
        menuStage.setMinHeight(700);
        menuStage.show();
    }

}
