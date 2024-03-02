package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.Dish;
import models.Order;
import models.Restaurant;
import models.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AdminPageDashBoardController extends AdminPageControllerAbstract {

    @FXML private Label sub_header;
    @FXML private AnchorPane subPane;

    @FXML private Label chartLabel1;
    @FXML private Label chartLabel2;
    @FXML private Label chartLabel3;
    @FXML private Label chartLabel4;
    @FXML private Label statisticsLabel1;
    @FXML private Label statisticsLabel2;
    @FXML private Label ordersNumber;
    @FXML private Label topDish;
    @FXML private Label topDishOrders;

    @FXML private LineChart ordersWeekLineChart;
    @FXML private LineChart dishWeekLineChart;
    @FXML private BarChart employeeOrdersBarChart;
    @FXML private BarChart dishOrdersBarChart;

    private Label[] labels;

    public void initializeWithData(User user, boolean islightMode) {
        this.user = user;
        this.restaurant = Restaurant.retrieveRestaurant();

        labels = new Label[]{chartLabel1, chartLabel2, chartLabel3, chartLabel4, statisticsLabel1, statisticsLabel2, ordersNumber, topDish, topDishOrders};
        fillOrdersWeekLineChart();
        fillDishWeekLineChart();
        fillDishOrdersBarChart();
        fillEmployeeOrdersBarChart();

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    private void fillOrdersWeekLineChart(){
        Map<String, Integer> ordersWeek = Order.getOrderCountsThisWeek();

        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();

        LocalDate today = LocalDate.now();
        int[] todayOrderCount = {0};

        ordersWeek.entrySet().forEach(entry -> {
            String dateStr = entry.getKey();
            int orderCount = entry.getValue();

            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (date.equals(today)) {
                todayOrderCount[0] = orderCount;
            }

            String dayOfWeekStr = date.getDayOfWeek().toString();;

            series1.getData().add(new XYChart.Data<>(dayOfWeekStr, orderCount));
        });

        ordersWeekLineChart.getData().add(series1);
        ordersWeekLineChart.setLegendVisible(false);

        ordersNumber.setText("number of: "+todayOrderCount[0]);
    }

    private void fillDishWeekLineChart(){
        Map<String, Integer> dishesOrdersThisWeek = Dish.retrieveTop5DishesByPopularityThisWeek();
        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();

        dishesOrdersThisWeek.entrySet().forEach(entry -> {
            String nameStr = entry.getKey();
            int orderCount = entry.getValue();

            series2.getData().add(new XYChart.Data<>(nameStr, orderCount));
        });

        dishWeekLineChart.getData().add(series2);
        dishWeekLineChart.setLegendVisible(false);

        List<String> bestDishInfo = Dish.retrieveBestDishToday();

        if (bestDishInfo.isEmpty()){
            topDish.setText("");
            topDishOrders.setText("");

        }else {
            topDish.setText(bestDishInfo.get(0));
            topDishOrders.setText("appearances: "+ bestDishInfo.get(1));
        }
    }

    private void fillDishOrdersBarChart(){
        Map<String, Integer> dishesOrdersAllTime = Dish.retrieveTop5DishesByPopularity();
        BarChart.Series<String, Integer> series3 = new XYChart.Series<>();

        dishesOrdersAllTime.entrySet().forEach(entry -> {
            String nameStr = entry.getKey();
            int orderCount = entry.getValue();

            series3.getData().add(new XYChart.Data<>(nameStr, orderCount));
        });

        dishOrdersBarChart.getData().add(series3);
        dishOrdersBarChart.setLegendVisible(false);
    }

    private void fillEmployeeOrdersBarChart(){
        Map<String, Integer> usersOrdersAllTime = User.retrieveTop5UsersByPopularity();
        BarChart.Series<String, Integer> series4 = new XYChart.Series<>();

        usersOrdersAllTime.entrySet().forEach(entry -> {
            String nameStr = entry.getKey();
            int orderCount = entry.getValue();

            series4.getData().add(new XYChart.Data<>(nameStr, orderCount));
        });

        employeeOrdersBarChart.getData().add(series4);
        employeeOrdersBarChart.setLegendVisible(false);
    }


    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            islightMode = false;
            mainPane.setStyle("-fx-background-color: #1E1F22");
            header.getStyleClass().remove("text_label_black_header");
            header.getStyleClass().add("text_label_white_header");
            moon_sun.setImage(sun);
            moon_sun.getStyleClass().remove("moon_sun_black");
            moon_sun.getStyleClass().add("moon_sun_white");
            rightVBox.getStyleClass().remove("VBoxLight");
            rightVBox.getStyleClass().add("VBoxDark");
            subPane.getStyleClass().remove("split-paneWhite");
            subPane.getStyleClass().add("split-paneDark");

            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }

            dashboardButton.getStyleClass().remove("active_buttonWhite");
            dashboardButton.getStyleClass().add("active_buttonDark");

            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");
        }else {
            islightMode = true;
            mainPane.setStyle("-fx-background-color: #ffffff");
            header.getStyleClass().remove("text_label_white_header");
            header.getStyleClass().add("text_label_black_header");
            moon_sun.setImage(moon);
            moon_sun.getStyleClass().remove("moon_sun_white");
            moon_sun.getStyleClass().add("moon_sun_black");
            rightVBox.getStyleClass().remove("VBoxDark");
            rightVBox.getStyleClass().add("VBoxLight");
            subPane.getStyleClass().remove("split-paneDark");
            subPane.getStyleClass().add("split-paneWhite");

            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
            dashboardButton.getStyleClass().remove("active_buttonDark");
            dashboardButton.getStyleClass().add("active_buttonWhite");

            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");
        }
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }
}
