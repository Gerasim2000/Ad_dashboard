package org.group5;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group5.Controllers.CompareWindowController;
import org.group5.Controllers.SettingsController;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class GUIController {

    //Main Menu FXML
    @FXML
    public Button impressionsBtn;
    public Button clicksBtn;
    public Button bouncesBtn;
    public Button conversionsBtn;
    public Button totalCostBtn;
    public Button ctrBtn;
    public Button cpaBtn;
    public Button cpcBtn;
    public Button cpmBtn;
    public Button bounceRateBtn;
    public Button uniquesBtn;
    public Button byDay;
    public Label totalImpressions;
    public Label totalClicks;
    public Label totalBounces;
    public Label totalUniques;
    public MenuItem fileOpen;
    public MenuItem helpBtn;
    public MenuItem quitBtn;


    public static String stylesheetColour = "stylesPURPLE.css";
    public static String stylesheetFont = "stylesMedium.css";

    @FXML
    private LineChart<String, Integer> bouncesChart;
    @FXML
    private LineChart<String, Integer> uniquesChart;
    @FXML
    private LineChart<String, Integer> clicksChart;
    @FXML
    private LineChart<String, Integer> impressionChart;
    public static Controller controller;

    public Date start;
    public Date end;
    public int interval;

    public Date dayBefore;
    public Date weekBefore;
    public Date monthsBefore;
    public Date yearBefore;

    public int dayInMilliseconds = 86400000;
    public int weekInMilliseconds = 604800000;
    public long monthInMilliseconds = 2592000000L;
    public long yearInMilliseconds = 31556952000L;


    @FXML
    private void showHelpWindow() {
        openWindow("FXMLs/help.fxml");
    }

    @FXML
    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    private static Stage compareWindow;
    private static CompareWindowController compareWindowController;

    public static LineChart<String, Number> compareChart;

    protected void loadDates() throws SQLException {
        dayBefore = new Date(controller.getLastDate().getTime() - dayInMilliseconds);
        weekBefore = new Date(controller.getLastDate().getTime() - weekInMilliseconds);
        monthsBefore = new Date(controller.getLastDate().getTime() - monthInMilliseconds);
        yearBefore = new Date(controller.getLastDate().getTime() - yearInMilliseconds);
    }

    public boolean setFilterSettings(Filter filterSettings, DatePicker datePickerFrom, DatePicker datePickerTo, TextField timeFrom, TextField timeTo) {
        try {
            Date dateStart = new SimpleDateFormat("yyy-MM-dd hh:mm").parse(datePickerFrom.getValue().toString() + " " + timeFrom.getText());
            Date dateFinish = new SimpleDateFormat("yyy-MM-dd hh:mm").parse(datePickerTo.getValue().toString() + " " + timeTo.getText());
            filterSettings.setStart(dateStart);
            filterSettings.setEnd(dateFinish);
            return true;
        } catch (ParseException e) {
            alertFail(e, "Parse Error", "Failed to parse given dates");
        }
        return false;
    }


    public void showCharts() throws SQLException {

        start = controller.getFirstDate();
        end = controller.getLastDate();

        createGranularityChart(controller.countUniques(new Filter(start, end), Controller.GRAN_DAY), new Filter(start, end), Controller.GRAN_DAY, uniquesChart);
        createGranularityChart(controller.countBounces(new Filter(start, end), Controller.GRAN_DAY), new Filter(start, end), Controller.GRAN_DAY, bouncesChart);
        createGranularityChart(controller.countClicks(new Filter(start, end), Controller.GRAN_DAY), new Filter(start, end), Controller.GRAN_DAY, clicksChart);
        createGranularityChart(controller.countImpressions(new Filter(start, end), Controller.GRAN_DAY), new Filter(start, end), Controller.GRAN_DAY, impressionChart);
    }


    public void importantMetric() {
        try {
            totalImpressions.setText("Total: " + controller.countImpressions());
            totalClicks.setText("Total: " + controller.countClicks());
            totalBounces.setText("Total: " + controller.countBounces());
            totalUniques.setText("Total: " + controller.countUniques());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void initData(Controller controller) {
        this.controller = controller;
        importantMetric();
    }

    public void openWindow(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("Styles/" + stylesheetColour).toExternalForm());
            scene.getStylesheets().add(getClass().getResource("Styles/" + stylesheetFont).toExternalForm());
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImpressions() throws IOException {
        openWindow("FXMLs/impressions.fxml");

    }

    public void fileOpen(ActionEvent actionEvent) {
        close();
        openWindow("FXMLs/dataImport.fxml");
    }

    public void showUniques(ActionEvent actionEvent) {
        openWindow("FXMLs/uniques.fxml");
    }

    public void showClicks(ActionEvent actionEvent) {
        openWindow("FXMLs/clicks.fxml");
    }

    public void showBounces(ActionEvent actionEvent) {
        openWindow("FXMLs/bounces.fxml");
    }

    public void showConversions(ActionEvent actionEvent) {
        openWindow("FXMLs/conversions.fxml");
    }

    public void showClickCost(ActionEvent actionEvent) {
        openWindow("FXMLs/clickCost.fxml");
    }

    public void showCtr(ActionEvent actionEvent) {
        openWindow("FXMLs/ctr.fxml");
    }

    public void showCpa(ActionEvent actionEvent) {
        openWindow("FXMLs/cpa.fxml");
    }

    public void showCpc(ActionEvent actionEvent) {
        openWindow("FXMLs/cpc.fxml");
    }

    public void showCpm(ActionEvent actionEvent) {
        openWindow("FXMLs/cpm.fxml");
    }

    public void showBounceRate(ActionEvent actionEvent) {
        openWindow("FXMLs/bounceRate.fxml");
    }

    public void showTotalCost(ActionEvent actionEvent) {
        openWindow("FXMLs/totalcost.fxml");
    }

    public Controller getController() {
        return controller;
    }

    protected void alertFail(Alert.AlertType alertType, Exception e, String title, String header) {
        Alert alert = new Alert(alertType, e.toString(), ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            // Alert acknowledged
            alert.close();
        }
    }

    protected void alertFail(Exception e, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            // Alert acknowledged
            alert.close();
        }
    }

    public void showCompareWindow() {
        if (compareWindow == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/compareWindow.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                compareWindowController = loader.getController();
//                scene.getStylesheets().add(getClass().getResource("stylesPURPLE.css").toExternalForm());
                stage.setScene(scene);

                compareWindow = stage;
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!compareWindow.isShowing()) {
            compareWindowController.clear();
            compareWindow.show();
        }

        compareWindowController.addOverlay();

    }

    public String roundNum(double v) {
        String x = String.valueOf(v);
        return x.substring(0, Math.min(x.length(), 4));
    }

    public String roundNum(float v) {
        String x = String.valueOf(v);
        return x.substring(0, Math.min(x.length(), 4));
    }

    public LineChart<String, Number> convertIntToNumber(LineChart<String, Integer> lineChart) {

        XYChart.Series series = new XYChart.Series();

        for (XYChart.Data<String, Integer> xy : lineChart.getData().get(0).getData()) {
            String x = xy.getXValue();
            int y = xy.getYValue();

            series.getData().add(new XYChart.Data<String, Number>(x, (float) y));
        }

        NumberAxis yAxis = new NumberAxis();
        CategoryAxis xAxis = new CategoryAxis();

        LineChart<String, Number> dataChart = new LineChart<String, Number>(xAxis, yAxis);
        dataChart.getData().add(series);
        dataChart.setTitle(lineChart.getTitle());
        return dataChart;
    }

    public LineChart<String, Number> convertFloatToNumber(LineChart<String, Float> lineChart) {

        XYChart.Series series = new XYChart.Series();

        for (XYChart.Data<String, Float> xy : lineChart.getData().get(0).getData()) {
            String x = xy.getXValue();
            float y = xy.getYValue();

            series.getData().add(new XYChart.Data<String, Number>(x, (float) y));
        }


        NumberAxis yAxis = new NumberAxis();
        CategoryAxis xAxis = new CategoryAxis();
        LineChart<String, Number> dataChart = new LineChart<String, Number>(xAxis, yAxis);
        dataChart.getData().add(series);
        dataChart.setTitle(lineChart.getTitle());
        return dataChart;
    }


//    public void initialiseChart(ChoiceBox<String> granularityDrop, DatePicker datePickerFrom, DatePicker datePickerTo, TextField timeFrom, TextField timeTo) {
//        initialiseInputs(granularityDrop, datePickerFrom, datePickerTo, timeFrom, timeTo);
//        loadDates();
//        total.setText("Total: " + controller.countImpressions());
//        displayDate();
//        Filter filter = new Filter(controller.getFirstDate(), controller.getLastDate());
//        createGranularityChart(controller.countImpressions(filter, Controller.GRAN_DAY), filter, Controller.GRAN_DAY, lineChart);
//    }

//    public void displayDate() throws SQLException {
//        lastDay.setText("Last Day: " + setStat(dayBefore, controller.getLastDate(), Controller.GRAN_DAY));
//        lastWeek.setText("Last Week: " + setStat(weekBefore, controller.getLastDate(), Controller.GRAN_WEEK));
//        lastMonth.setText("Last Month: " + setStat(monthsBefore, controller.getLastDate(), Controller.GRAN_MONTH));
//        lastYear.setText("Last Year: " + setStat(yearBefore, controller.getLastDate(), Controller.GRAN_MONTH * 12));
//    }

    public Filter filterSettings;

    public void initialiseInputs(ChoiceBox<String> granularityDrop, DatePicker datePickerFrom, DatePicker datePickerTo, TextField timeFrom, TextField timeTo) throws SQLException {

        granularityDrop.getItems().add("Day granularity");
        granularityDrop.getItems().add("Week granularity");
        granularityDrop.getItems().add("Month granularity");
        granularityDrop.getItems().add("Time of day");
        granularityDrop.getItems().add("Day of week");

        granularityDrop.setValue("Day granularity");

        datePickerFrom.setValue(controller.getFirstDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        datePickerTo.setValue(controller.getLastDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        timeFrom.setText(format.format(controller.getFirstDate()));
        timeTo.setText(format.format(controller.getLastDate()));

        filterSettings = new Filter(controller.getFirstDate(), controller.getLastDate());
    }

    public void createGranularityChart(int[] ys, Filter filter, int granularity, LineChart lineChart) {
        String[] xs = new String[ys.length];

        XYChart.Series series = new XYChart.Series();

        int tStart = (int) (filter.getStart().getTime() / 1000);
        int tEnd = (int) (filter.getEnd().getTime() / 1000);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

        int j = 0;
        for (int i = tStart; i < tEnd; i += granularity) {
            xs[j] = format.format(new Date(i * 1000L));
            series.getData().add(new XYChart.Data<String, Integer>(xs[j], ys[j]));
            j++;
        }

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);


        compareChart = convertIntToNumber(lineChart);
        System.out.println("compare: " + compareChart);

    }

    public void createGranularityChart(float[] ys, Filter filter, int granularity, LineChart lineChart) {
        String[] xs = new String[ys.length];

        XYChart.Series series = new XYChart.Series();

        int tStart = (int) (filter.getStart().getTime() / 1000);
        int tEnd = (int) (filter.getEnd().getTime() / 1000);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

        int j = 0;
        for (int i = tStart; i < tEnd; i += granularity) {
            xs[j] = format.format(new Date(i * 1000L));
            series.getData().add(new XYChart.Data<String, Float>(xs[j], ys[j]));
            j++;
        }

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        compareChart = convertFloatToNumber(lineChart);
    }

    public void createPerformanceMetricChart(int[] ys, Filter filter, int perfMetric, LineChart lineChart) {

        String[] xs = new String[ys.length];

        XYChart.Series series = new XYChart.Series();

        String[] days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        int max = (perfMetric == Controller.PERFORMANCE_DAY) ? 24 : 7;

        int j = 0;
        for (int i = 0; i < max; i += 1) {
            xs[j] = (perfMetric == Controller.PERFORMANCE_DAY) ? String.valueOf(i) : days[i];
            series.getData().add(new XYChart.Data<String, Integer>(xs[j], ys[j]));
            j++;
        }

        lineChart.getXAxis().setLabel((perfMetric == Controller.PERFORMANCE_DAY) ? "Hour" : "Day");

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        compareChart = convertIntToNumber(lineChart);

    }

    public void createPerformanceMetricChart(float[] ys, Filter filter, int perfMetric, LineChart lineChart) {

        String[] xs = new String[ys.length];

        XYChart.Series series = new XYChart.Series();

        String[] days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        int max = (perfMetric == Controller.PERFORMANCE_DAY) ? 24 : 7;

        int j = 0;
        for (int i = 0; i < max; i += 1) {
            xs[j] = (perfMetric == Controller.PERFORMANCE_DAY) ? String.valueOf(i) : days[i];
            series.getData().add(new XYChart.Data<String, Float>(xs[j], ys[j]));
            j++;
        }

        lineChart.getXAxis().setLabel((perfMetric == Controller.PERFORMANCE_DAY) ? "Hour" : "Day");

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        compareChart = convertFloatToNumber(lineChart);

    }

    public void saveChart(Chart chart) {

        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();

        //Set extension filter for png files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                WritableImage image = chart.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public void printChart(Chart chart) {

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(chart.getScene().getWindow())) {
            boolean success = job.printPage(chart);
            if (success) {
                job.endJob();
            }
        }
    }

    public void showFilterSelect() {
        // Need user to select: Gender, Age, Income, Context.

        // Create the custom dialog.
        Dialog<Filter> dialog = new Dialog<>();
        dialog.setTitle("Filter Settings");
        dialog.setHeaderText("Set Filters");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        CheckBox genderCheck = new CheckBox("Gender");
        genderCheck.setSelected(filterSettings.hasGender());

        ChoiceBox<String> genderChoice = new ChoiceBox<>();
        List<String> gChoices = new ArrayList<>();
        gChoices.add(Filter.GENDER_MALE);
        gChoices.add(Filter.GENDER_FEMALE);
        genderChoice.setValue(
                (filterSettings.hasGender()) ? filterSettings.getGender() : Filter.GENDER_MALE
        );
        genderChoice.setItems(
                FXCollections.observableArrayList(gChoices)
        );

        genderChoice.disableProperty().bind(genderCheck.selectedProperty().not());

        CheckBox ageCheck = new CheckBox("Age");
        ageCheck.setSelected(filterSettings.hasAge());

        ChoiceBox<String> ageChoice = new ChoiceBox<>();
        List<String> ageChoices = new ArrayList<>();
        ageChoices.add(Filter.AGE_LESSTHAN25);
        ageChoices.add(Filter.AGE_25TO34);
        ageChoices.add(Filter.AGE_35TO44);
        ageChoices.add(Filter.AGE_45TO54);
        ageChoices.add(Filter.AGE_GREATERTHAN54);
        ageChoice.setValue(
                (filterSettings.hasAge()) ? filterSettings.getAge() : Filter.AGE_LESSTHAN25
        );
        ageChoice.setItems(
                FXCollections.observableArrayList(ageChoices)
        );

        ageChoice.disableProperty().bind(ageCheck.selectedProperty().not());

        CheckBox incomeCheck = new CheckBox("Income");
        incomeCheck.setSelected(filterSettings.hasIncome());

        ChoiceBox<String> incomeChoice = new ChoiceBox<>();
        List<String> incomes = new ArrayList<>();
        incomes.add(Filter.INCOME_LOW);
        incomes.add(Filter.INCOME_MED);
        incomes.add(Filter.INCOME_HIGH);
        incomeChoice.setValue(
                (filterSettings.hasIncome()) ? filterSettings.getIncome() : Filter.INCOME_LOW
        );
        incomeChoice.setItems(
                FXCollections.observableArrayList(incomes)
        );

        incomeChoice.disableProperty().bind(incomeCheck.selectedProperty().not());

        CheckBox contextCheck = new CheckBox("Context");
        contextCheck.setSelected(filterSettings.hasContext());

        ChoiceBox<String> contextChoice = new ChoiceBox<>();
        List<String> contexts = new ArrayList<>();
        contexts.add(Filter.CONTEXT_BLOG);
        contexts.add(Filter.CONTEXT_NEWS);
        contexts.add(Filter.CONTEXT_MEDIA);
        contexts.add(Filter.CONTEXT_HOBBIES);
        contexts.add(Filter.CONTEXT_SOCIAL);
        contexts.add(Filter.CONTEXT_SHOPPING);
        contexts.add(Filter.CONTEXT_TRAVEL);
        contextChoice.setValue(
                (filterSettings.hasContext()) ? filterSettings.getContext() : Filter.CONTEXT_BLOG
        );
        contextChoice.setItems(
                FXCollections.observableArrayList(contexts)
        );

        contextChoice.disableProperty().bind(contextCheck.selectedProperty().not());

        grid.add(genderCheck, 0, 0);
        grid.add(genderChoice, 1, 0);
        grid.add(ageCheck, 0, 1);
        grid.add(ageChoice, 1, 1);
        grid.add(incomeCheck, 0, 2);
        grid.add(incomeChoice, 1, 2);
        grid.add(contextCheck, 0, 3);
        grid.add(contextChoice, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                Filter f = new Filter(null, null);
                if (genderCheck.isSelected()) {
                    f.setGender(genderChoice.getValue());
                }
                if (ageCheck.isSelected()) {
                    f.setAge(ageChoice.getValue());
                }
                if (incomeCheck.isSelected()) {
                    f.setIncome(incomeChoice.getValue());
                }
                if (contextCheck.isSelected()) {
                    f.setContext(contextChoice.getValue());
                }
                return f;
            }
            return null;
        });

        Optional<Filter> result = dialog.showAndWait();

        result.ifPresent(f -> filterSettings = f);

    }

    public void openSettings(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/settings.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("Styles/" + GUIController.stylesheetColour).toExternalForm());


            SettingsController settingsController = loader.getController();
            settingsController.initData();

            stage.show();
            close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Stage stage = (Stage) impressionsBtn.getScene().getWindow();
        stage.close();
    }
}
