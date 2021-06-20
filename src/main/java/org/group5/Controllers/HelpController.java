package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpController {

    public Button submitBtn;

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) submitBtn.getScene().getWindow();
        stage.close();
    }
}
