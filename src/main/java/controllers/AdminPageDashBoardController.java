package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import models.Restaurant;
import models.User;

public class AdminPageDashBoardController extends AdminPageControllerAbstract {

    @FXML private Label sub_header;

    @FXML private Label chartLabel1;
    @FXML private Label chartLabel2;
    @FXML private Label chartLabel3;
    @FXML private Label chartLabel4;
    @FXML private Label statisticsLabel1;
    @FXML private Label statisticsLabel2;
    @FXML private Label ordersNumber;
    @FXML private Label topDish;

    @FXML private LineChart ordersWeekLineChart;
    @FXML private LineChart dishWeekLineChart;
    @FXML private BarChart employeeOrdersBarChart;
    @FXML private BarChart dishOrdersBarChart;

    private Label[] labels;

    public void initializeWithData(User user, boolean islightMode) {

        this.user = user;
        this.restaurant = Restaurant.retrieveRestaurant();

        labels = new Label[]{chartLabel1,chartLabel2,chartLabel3,chartLabel4,statisticsLabel1,statisticsLabel2,ordersNumber,topDish};

        if (!islightMode) {
            handleChangeColorMode();
        }
    }


    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }

            dashboardButton.getStyleClass().remove("active_buttonWhite");
            dashboardButton.getStyleClass().add("active_buttonDark");

            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

        }else {
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
            dashboardButton.getStyleClass().remove("active_buttonDark");
            dashboardButton.getStyleClass().add("active_buttonWhite");

            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

        }
        super.handleChangeColorMode();
    }



    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }
}
