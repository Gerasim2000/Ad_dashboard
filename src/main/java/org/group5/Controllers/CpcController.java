package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
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

public class CpcController extends GUIController implements Initializable {
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
    private Button compareBtn;

    //todo create functions for impressions
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle("CPC");
        try {
            initialiseInputs(granularityDrop, datePickerFrom, datePickerTo, timeFrom, timeTo);
            loadDates();
            displayDate();
            Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
            createGranularityChart(controller.computeCPC(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
        } catch (SQLException throwables) {
            alertFail(throwables, "Chart load error", "Failed to load chart");
            throwables.printStackTrace();
        }
    }

    public double setStat(Date start, Date end, int granularity) throws SQLException {
        float[] data = controller.computeCPC(new Filter(start, end), granularity);
        return data[0];
    }

    public void showGraph() throws ParseException, SQLException {

        if (!setFilterSettings(filterSettings, datePickerFrom, datePickerTo, timeFrom, timeTo)) return;

        lineChart.getData().clear();
        lineChart.setAnimated(true);// enable animation


        Filter filter = filterSettings;

        try {
            switch (granularityDrop.getValue()) {
                case "Day granularity" -> createGranularityChart(controller.computeCPC(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
                case "Week granularity" -> createGranularityChart(controller.computeCPC(filter, Controller.GRAN_WEEK), filter, Controller.GRAN_WEEK, lineChart);
                case "Month granularity" -> createGranularityChart(controller.computeCPC(filter, Controller.GRAN_MONTH), filter, Controller.GRAN_MONTH, lineChart);
                case "Time of day" -> createPerformanceMetricChart(controller.computeCPCPM(filter, Controller.PERFORMANCE_DAY), filter, Controller.PERFORMANCE_DAY, lineChart);
                case "Day of week" -> createPerformanceMetricChart(controller.computeCPCPM(filter, Controller.PERFORMANCE_WEEK), filter, Controller.PERFORMANCE_WEEK, lineChart);
                default -> throw new IllegalStateException("Unexpected value: " + granularityDrop.getValue());
            }
        } catch (SQLException e) {
            alertFail(e, "Graph Update Error", "Failed to query data for updating graph");
        }

        lineChart.setAnimated(false);// disable animation

    }


    public void displayDate() throws SQLException {
        lastDay.setText("Last Day: " + roundNum(setStat(dayBefore, controller.getLastDate(), Controller.GRAN_DAY)));
        lastWeek.setText("Last Week: " + roundNum(setStat(weekBefore, controller.getLastDate(), Controller.GRAN_WEEK)));
        lastMonth.setText("Last Month: " + roundNum(setStat(monthsBefore, controller.getLastDate(), Controller.GRAN_MONTH)));

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
        System.out.println("Opening Window");
        showCompareWindow();
    }


}


//    @FXML
//    private Label lastDay;
//    @FXML
//    private Label lastWeek;
//    @FXML
//    private Label lastMonth;
//    @FXML
//    private Label lastYear;
//    @FXML
//    private LineChart<String, Float> lineChart;
//
//    //TODO make functions for cpm
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//        try {
//            loadDates();
//            displayDate();
//            populateChart(dayBefore, controller.getLastDate(), 3000000);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//    public double  setStat(Date start, Date end, int interval) throws SQLException {
//        float[] days = controller.computeCPCByGranularity(start, end, interval);
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
//    public void populateChart(Date start, Date end, int interval) throws SQLException {
//
//        float[] ys = controller.computeCPCByGranularity(start, end, interval);
//
//        String[] xs = new String[ys.length];
//
//        XYChart.Series<String, Float> series = new XYChart.Series();
////        series.setName("data");
//
//        int j = 0;
//        for (long i = start.getTime(); i < end.getTime(); i += interval) {
//            xs[j] = (new Date(i)).toString();
//            series.getData().add(new XYChart.Data<String, Float>(xs[j], ys[j]));
//            j++;
//        }
//
//        System.out.println(series != null);
//        lineChart.getData().add(series);
//        lineChart.getXAxis().setTickLabelsVisible(false);
//        lineChart.setLegendVisible(false);
//    }
//}
