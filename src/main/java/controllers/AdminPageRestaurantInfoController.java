package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Restaurant;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminPageRestaurantInfoController extends AdminPageControllerAbstract {

    @FXML private Label sub_header;
    @FXML private Label sub_header2;
    @FXML private Label name_label;
    @FXML private Label email_label;
    @FXML private Label phone_label;
    @FXML private Label address_label;
    @FXML private Label tax_label;
    @FXML private Label info_label;
    @FXML private Label paperSize_label;

    @FXML private TextField name_field;
    @FXML private TextField email_field;
    @FXML private TextField phone_field;
    @FXML private TextArea address_field;
    @FXML private TextField tax_field;
    @FXML private TextArea info_field;
    @FXML private TextField paperSize_field;

    private Label[] labels;

    public void initializeWithData() {
        labels = new Label[]{name_label, email_label, phone_label, address_label, tax_label, info_label, paperSize_label};
        createKeyboard();
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");
            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

            restaurantInfoButton.getStyleClass().remove("active_buttonWhite");
            restaurantInfoButton.getStyleClass().add("active_buttonDark");

        }else {
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");
            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");

            restaurantInfoButton.getStyleClass().remove("active_buttonDark");
            restaurantInfoButton.getStyleClass().add("active_buttonWhite");
        }
        super.handleChangeColorMode();
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return true;
    }

    @FXML
    private void handleChangeDataButton() {

    }

}
