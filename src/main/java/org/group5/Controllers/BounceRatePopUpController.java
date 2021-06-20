package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.group5.Controller;
import org.group5.GUIController;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

public class BounceRatePopUpController extends GUIController implements Initializable {
    @FXML
    private ChoiceBox<String> bounceTypeChoice;
    @FXML
    private TextField upperBound;
    @FXML
    private Button submit;
    @FXML
    private BounceRateController option;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bounceTypeChoice.getItems().add("By Time Spent");
        bounceTypeChoice.getItems().add("By Pages Visited");
    }

    public void choice(BounceRateController option) {
        this.option = option;
        // Set current values.
        if (option.getCurrentBounceType() == Controller.BounceType.PagesViewed) {
            bounceTypeChoice.setValue("By Pages Visited");
        } else {
            bounceTypeChoice.setValue("By Time Spent");
        }
        upperBound.setText(String.valueOf(option.getCurrentThreshold()));
    }

    public void submitOnAction() throws SQLException {
        String type = bounceTypeChoice.getValue();
        String limit = upperBound.getText();

        if (type.equals("By Time Spent")) {
            option.setBounceType(Controller.BounceType.TimeSpent);

        } else if (type.equals("By Pages Visited")) {
            option.setBounceType(Controller.BounceType.PagesViewed);
        }

        try {
            option.setBounceThresh(Integer.parseInt(limit));
        } catch (Exception e) {
            alertFail(e, "Invalid Entry Error", "Please enter an integer number of seconds or pages viewed");
            return;
        }

        try {
            option.showGraph();
        } catch (ParseException e) {
            alertFail(e, "Parse Exception", "Failed to parse given dates");
        }
        Stage stage = (Stage) submit.getScene().getWindow();
        stage.close();


    }

}
