package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class ClicksController extends GUIController implements Initializable {
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

    //todo create functions for impressions
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle("Clicks");

        try {
            initialiseInputs(granularityDrop, datePickerFrom, datePickerTo, timeFrom, timeTo);
            loadDates();
            total.setText("Total: " + controller.countClicks());
            displayDate();
            Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
            createGranularityChart(controller.countClicks(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
        } catch (SQLException throwables) {
            alertFail(throwables, "Chart load error", "Failed to load chart");
            throwables.printStackTrace();
        }
    }

    public double setStat(Date start, Date end, int granularity) throws SQLException {
        int[] data = controller.countClicks(new Filter(start, end), granularity);
        return data[0];
    }

    public void showGraph() {

        if (!setFilterSettings(filterSettings, datePickerFrom, datePickerTo, timeFrom, timeTo)) return;

        lineChart.getData().clear();
        lineChart.setAnimated(true);// enable animation

        Filter filter = filterSettings;

        try {
            switch (granularityDrop.getValue()) {
                case "Day granularity" -> createGranularityChart(controller.countClicks(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
                case "Week granularity" -> createGranularityChart(controller.countClicks(filter, Controller.GRAN_WEEK), filter, Controller.GRAN_WEEK, lineChart);
                case "Month granularity" -> createGranularityChart(controller.countClicks(filter, Controller.GRAN_MONTH), filter, Controller.GRAN_MONTH, lineChart);
                case "Time of day" -> createPerformanceMetricChart(controller.countClicksPM(filter, Controller.PERFORMANCE_DAY), filter, Controller.PERFORMANCE_DAY, lineChart);
                case "Day of week" -> createPerformanceMetricChart(controller.countClicksPM(filter, Controller.PERFORMANCE_WEEK), filter, Controller.PERFORMANCE_WEEK, lineChart);
                default -> throw new IllegalStateException("Unexpected value: " + granularityDrop.getValue());
            }
        } catch (SQLException e) {
            alertFail(e, "Graph Update Error", "Failed to query data for updating graph");
        }

        lineChart.setAnimated(false);// disable animation

    }

    public void displayDate() throws SQLException {
        lastDay.setText("Last Day: " + setStat(dayBefore, controller.getLastDate(), Controller.GRAN_DAY));
        lastWeek.setText("Last Week: " + setStat(weekBefore, controller.getLastDate(), Controller.GRAN_WEEK));
        lastMonth.setText("Last Month: " + setStat(monthsBefore, controller.getLastDate(), Controller.GRAN_MONTH));
//        lastYear.setText("Last Year: " + setStat(yearBefore, controller.getLastDate(), Controller.GRAN_MONTH * 12));
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

    public void compare() {
        showCompareWindow();
    }

}

//    @FXML
//    private Label total;
//    @FXML
//    private Label lastDay;
//    @FXML
//    private Label lastWeek;
//    @FXML
//    private Label lastMonth;
//    @FXML
//    private Label lastYear;
//    @FXML
//    private LineChart<String, Integer> lineChart;
//
//
//
//    //TODO make function for clicks
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        try{
//            loadDates();
//            total.setText("Total Clicks: "+controller.countClicks());
//            displayDate();
//        populateChart(dayBefore, controller.getLastDate(), 3000000);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public double  setStat(Date start, Date end, int interval) throws SQLException {
//        int[] days = controller.countClicksByGranularity(start, end, interval);
//        System.out.println(Arrays.toString(days));
//        return days[0];
//    }
//
//    public void displayDate() throws SQLException {
//        lastDay.setText("Last Day: " +setStat(dayBefore, controller.getLastDate(), dayInMilliseconds));
//        lastWeek.setText("Last Week: " + setStat(weekBefore,controller.getLastDate(),weekInMilliseconds));
//        lastMonth.setText("Last Month: " + setStat(monthsBefore, controller.getLastDate(), dayInMilliseconds));
//        lastYear.setText("Last Year: " + setStat( yearBefore, controller.getLastDate(), dayInMilliseconds));
//    }
//
//
//    public void populateChart(Date start, Date end, int interval) throws SQLException {
//        int[] ys = controller.countClicksByGranularity(start, end, interval);
//
//        String[] xs = new String[ys.length];
//
//        XYChart.Series series = new XYChart.Series();
////        series.setName("data");
//
//        int j = 0;
//        for (long i = start.getTime(); i < end.getTime(); i += interval) {
//            xs[j] = (new Date(i)).toString();
//            series.getData().add(new XYChart.Data<String, Integer>(xs[j], ys[j]));
//            j++;
//        }
//
//        lineChart.getData().add(series);
//        lineChart.getXAxis().setTickLabelsVisible(false);
//        lineChart.setLegendVisible(false);
//    }
//}