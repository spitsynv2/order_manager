package controllers.fragments;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Dish;

public class MenuItemController {

    @FXML private VBox dishVbox;
    @FXML private Label name_label;
    @FXML private Label type_label;
    @FXML private Label price_label;
    @FXML private Label product_id;
    public void setData(Dish dish, int id){
        name_label.setText(dish.getName());
        type_label.setText(dish.getType());
        price_label.setText("Price: "+dish.getPrice());
        product_id.setText(id+"");
        product_id.setManaged(false);
        product_id.setVisible(false);
    }

}
