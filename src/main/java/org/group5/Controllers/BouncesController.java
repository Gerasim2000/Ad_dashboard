package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.group5.Controller;
import org.group5.Filter;
import org.group5.GUIController;

import javax.print.PrintException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class BouncesController extends GUIController implements Initializable {
    @FXML
    private Label total;
    @FXML
    private Label lastDay;
    @FXML
    private Label lastWeek;
    @FXML
    private Label lastMonth;
    @FXML
    private Label lastYear;
    @FXML
    private LineChart<String, Integer> lineChart;
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private TextField timeFrom;
    @FXML
    private DatePicker datePickerTo;
    @FXML
    private TextField timeTo;
    @FXML
    private ChoiceBox<String> granularityDrop;
    @FXML
    private Button bounceType;

    //todo create functions for impressions
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lineChart.setTitle("Bounces");

        try {
            initialiseInputs(granularityDrop, datePickerFrom, datePickerTo, timeFrom, timeTo);
            loadDates();
            total.setText("Total: " + controller.countBounces());
            displayDate();
            Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
            createGranularityChart(controller.countBounces(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
        } catch (SQLException throwables) {
            alertFail(throwables, "Chart load error", "Failed to load chart");
            throwables.printStackTrace();
        }
    }

    public double setStat(Date start, Date end, int granularity) throws SQLException {
        int[] data = controller.countBounces(new Filter(start, end), granularity);
        return data[0];
    }

    public void showGraph() throws ParseException, SQLException {

        clear();

        if (!setFilterSettings(filterSettings, datePickerFrom, datePickerTo, timeFrom, timeTo)) return;

        lineChart.getData().clear();
        lineChart.setAnimated(true);// enable animation

        Filter filter = filterSettings;

        try {
            switch (granularityDrop.getValue()) {
                case "Day granularity" -> createGranularityChart(controller.countBounces(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
                case "Week granularity" -> createGranularityChart(controller.countBounces(filter, Controller.GRAN_WEEK), filter, Controller.GRAN_WEEK, lineChart);
                case "Month granularity" -> createGranularityChart(controller.countBounces(filter, Controller.GRAN_MONTH), filter, Controller.GRAN_MONTH, lineChart);
                case "Time of day" -> createPerformanceMetricChart(controller.countBouncesPM(filter, Controller.PERFORMANCE_DAY), filter, Controller.PERFORMANCE_DAY, lineChart);
                case "Day of week" -> createPerformanceMetricChart(controller.countBouncesPM(filter, Controller.PERFORMANCE_WEEK), filter, Controller.PERFORMANCE_WEEK, lineChart);
                default -> throw new IllegalStateException("Unexpected value: " + granularityDrop.getValue());
            }
        } catch (SQLException e) {
            alertFail(e, "Graph Update Error", "Failed to query data for updating graph");
        }

        lineChart.setAnimated(false);// disable animation

    }

    public void compare() {
        showCompareWindow();
    }

    public void displayDate() throws SQLException {
        lastDay.setText("Last Day: " + setStat(dayBefore, controller.getLastDate(), Controller.GRAN_DAY));
        lastWeek.setText("Last Week: " + setStat(weekBefore, controller.getLastDate(), Controller.GRAN_WEEK));
        lastMonth.setText("Last Month: " + setStat(monthsBefore, controller.getLastDate(), Controller.GRAN_MONTH));
//        lastYear.setText("Last Year: " + setStat(yearBefore, controller.getLastDate(), Controller.GRAN_MONTH * 12));
    }

    void setBounceType(Controller.BounceType b) {
        if (b == Controller.BounceType.TimeSpent) {
            bounceType.setText("Bounce Type: Time Spent");
        } else {
            bounceType.setText("Bounce Type: Pages Viewed");
        }
        controller.setBounceType(b);
    }

    void setBounceThresh(int p) {
        if (controller.getBounceType() == Controller.BounceType.PagesViewed) {
            controller.setPagesViewedThreshold(p);
        } else {
            controller.setBounceTimeSpentThreshold(p);
        }
    }

    public void saveChart() {
        saveChart(lineChart);
    }

    public void print() throws IOException, PrintException {
        printChart(lineChart);
    }

    public void clear() {
        lineChart.getData().clear();
    }

    public void bouncePopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/bouncePopUp.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            BouncePopUpController popUp = loader.getController();
            popUp.choice(this);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Controller.BounceType getCurrentBounceType() {
        return controller.getBounceType();
    }

    public int getCurrentThreshold() {
        if (getCurrentBounceType() == Controller.BounceType.PagesViewed) {
            return controller.getBouncePagesViewedThreshold();
        } else {
            return controller.getBounceTimeSpentThreshold();
        }
    }

}