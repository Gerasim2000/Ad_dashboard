package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import org.group5.GUIController;

import javax.print.PrintException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompareWindowController extends GUIController implements Initializable {

    @FXML
    private LineChart<String, Number> lineChart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setAnimated(false);
    }

    public void addOverlay() {
        System.out.println("Line Chart: " + lineChart);
        System.out.println("Compare Chart: " + compareChart);


//        lineChart.setAnimated(false);
        try {
            lineChart.getData().add(compareChart.getData().get(0));
            lineChart.getData().get(lineChart.getData().size() - 1).setName(compareChart.getTitle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
//        lineChart.setAnimated(true);

    }

    public void clear() {
        lineChart.getData().clear();
    }

    public void saveChart() {
        saveChart(lineChart);
    }

    public void print() throws IOException, PrintException {
        printChart(lineChart);
    }
}
