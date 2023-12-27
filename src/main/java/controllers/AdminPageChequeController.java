package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Restaurant;
import models.User;

public class AdminPageChequeController extends AdminPageControllerAbstract{

    @FXML
    private Label sub_header;
    @FXML
    private Label sub_header2;

    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }

    public void initializeWithData(User user, boolean islightMode) {
        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            chequeButton.getStyleClass().remove("active_buttonWhite");
            chequeButton.getStyleClass().add("active_buttonDark");

            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

        }else {
            chequeButton.getStyleClass().remove("active_buttonDark");
            chequeButton.getStyleClass().add("active_buttonWhite");

            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");
        }
        super.handleChangeColorMode();
    }
}
