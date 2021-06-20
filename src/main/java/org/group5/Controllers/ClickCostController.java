package org.group5.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.group5.Controller;
import org.group5.Filter;
import org.group5.GUIController;
import org.group5.HistogramData;

import javax.print.PrintException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ClickCostController extends GUIController implements Initializable {
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
    private BarChart<String, Integer> lineChart;
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

        lineChart.setTitle("Click Cost");

        try {
//            initialiseInputs(granularityDrop, datePickerFrom, datePickerTo, timeFrom, timeTo);
//            loadDates();
//            total.setText("Total: " + controller.countImpressions());
//            displayDate();
            Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());

            // --- GRPAH ---

            HistogramData y = controller.computeClickCostHistogram(controller.getFirstDate(), controller.getLastDate());
            int[] ys = y.getData();

            float[] xs = new float[ys.length];

            XYChart.Series series = new XYChart.Series();

            int tStart = (int) (filter.getStart().getTime() / 1000);
            int tEnd = (int) (filter.getEnd().getTime() / 1000);

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

            int j = 0;
            for (float i = 0; i < y.getMax(); i += y.getInterval()) {
                xs[j] = i; //format.format(new Date(i * 1000L));
                String inputString = String.valueOf(xs[j]);
                int maxLength = (inputString.length() < 5) ? inputString.length() : 5;
                inputString = inputString.substring(0, maxLength);
                series.getData().add(new XYChart.Data<String, Integer>(inputString, ys[j]));
                j++;
            }

            lineChart.getData().add(series);
            lineChart.setLegendVisible(false);

            // ------


//            createGranularityChart(controller.computeClickCostHistogram(controller.getFirstDate(), controller.getLastDate()));
        } catch (SQLException throwables) {
            alertFail(throwables, "Chart load error", "Failed to load chart");
            throwables.printStackTrace();
        }
    }

    public void compare() {
        showCompareWindow();
    }

    public double setStat(Date start, Date end, int granularity) throws SQLException {
        float[] data = controller.clickCost(new Filter(start, end), granularity);
        return data[0];
    }

    public void showGraph() throws ParseException, SQLException {

//        Date dateStart = new SimpleDateFormat("yyy-MM-dd hh:mm").parse(datePickerFrom.getValue().toString() + " " + timeFrom.getText());
//        Date dateFinish = new SimpleDateFormat("yyy-MM-dd hh:mm").parse(datePickerTo.getValue().toString() + " " + timeTo.getText());
//
//        lineChart.getData().clear();
//        lineChart.setAnimated(true);// enable animation
//
//        Filter filter = new Filter(dateStart, dateFinish);
//
//        switch (granularityDrop.getValue()) {
//            case "Day granularity" -> createGranularityChart(controller.clickCost(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
//            case "Week granularity" -> createGranularityChart(controller.clickCost(filter, Controller.GRAN_WEEK), filter, Controller.GRAN_WEEK, lineChart);
//            case "Month granularity" -> createGranularityChart(controller.clickCost(filter, Controller.GRAN_MONTH), filter, Controller.GRAN_MONTH, lineChart);
//            case "Time of day" -> createPerformanceMetricChart(controller.clickCostPM(filter, Controller.PERFORMANCE_DAY), filter, Controller.PERFORMANCE_DAY, lineChart);
//            case "Day of week" -> createPerformanceMetricChart(controller.clickCostPM(filter, Controller.PERFORMANCE_WEEK), filter, Controller.PERFORMANCE_WEEK, lineChart);
//            default -> throw new IllegalStateException("Unexpected value: " + granularityDrop.getValue());
//        }
//
//        lineChart.setAnimated(false);// disable animation

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

}
