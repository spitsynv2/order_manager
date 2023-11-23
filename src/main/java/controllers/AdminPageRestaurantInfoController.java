package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminPageRestaurantInfoController extends AdminPageControllerAbstract {

    @FXML private Label sub_header;
    @FXML private Label sub_header2;
    @FXML private Label name_label;
    @FXML private Label email_label;
    @FXML private Label phone_label;
    @FXML private Label address_label;

    @FXML private TextField name_field;
    @FXML private TextField email_field;
    @FXML private TextField phone_field;
    @FXML private TextArea address_field;


    @Override
    protected void handleChangeColorMode() {
        if (islightMode){

        }else {

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
