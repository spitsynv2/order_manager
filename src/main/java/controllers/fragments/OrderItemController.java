package controllers.fragments;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Order;

public class OrderItemController {

    @FXML private VBox orderVbox;
    @FXML private Label order_label;
    @FXML private Label date_label;
    @FXML private Label time_label;

    public void setData(Order order){
        order_label.setText("Order " + order.getId());
        date_label.setText(order.getDate().split(" ")[0]);
        time_label.setText(order.getDate().split(" ")[1]);

    }
}
