package controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Restaurant;
import models.User;

import java.io.IOException;
import java.util.*;

public abstract class AdminPageControllerAbstract {

    protected Stage stage;

    protected User user;
    protected Restaurant restaurant;

    @FXML protected BorderPane mainPane;
    @FXML protected VBox rightVBox;
    @FXML protected SplitPane secondaryPane;
    @FXML protected Label header;

    @FXML protected ImageView moon_sun;
    @FXML protected VBox moon_sunVBox;
    protected final
    Image sun = new Image(Objects.requireNonNull(getClass().getResource("/images/sun.png")).toExternalForm());
    protected final
    Image moon = new Image(Objects.requireNonNull(getClass().getResource("/images/moon.png")).toExternalForm());
    protected boolean islightMode = true;

    @FXML protected Button userInfoButton;
    @FXML protected Button restaurantInfoButton;
    @FXML protected Button dishesButton;
    @FXML protected Button menuButton;
    @FXML protected Button chequeButton;
    @FXML protected Button dashboardButton;
    @FXML protected Button infoButton;

    @FXML protected GridPane keyboard;
    protected List<Button> keyboardKeys = new ArrayList<>();
    protected List<String> keyboardKeysNumbers = new ArrayList<>();
    protected List<String> keyboardKeysLetters = new ArrayList<>();
    protected boolean isUpperCase = true;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void showYesNoDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UNDECORATED);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();

        if (msg.equals("Are you sure you want to exit?")){
            if (result.isPresent() && result.get() == yesButton) {
                stage.close();
            }
        }
    }

    @FXML
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
            secondaryPane.getStyleClass().remove("split-paneWhite");
            secondaryPane.getStyleClass().add("split-paneDark");

            keyboard.getStyleClass().remove("gridPaneTextLight");
            keyboard.getStyleClass().add("gridPaneTextDark");
            //VBox.setMargin(moon_sunVBox, new Insets(0, 5, 27, 25));
        }else {
            islightMode = true;
            mainPane.setStyle("-fx-background-colo: #ffffff");
            header.getStyleClass().remove("text_label_white_header");
            header.getStyleClass().add("text_label_black_header");
            moon_sun.setImage(moon);
            moon_sun.getStyleClass().remove("moon_sun_white");
            moon_sun.getStyleClass().add("moon_sun_black");
            rightVBox.getStyleClass().remove("VBoxDark");
            rightVBox.getStyleClass().add("VBoxLight");
            secondaryPane.getStyleClass().remove("split-paneDark");
            secondaryPane.getStyleClass().add("split-paneWhite");

            keyboard.getStyleClass().remove("gridPaneTextDark");
            keyboard.getStyleClass().add("gridPaneTextLight");
            //VBox.setMargin(moon_sunVBox, new Insets(0, 10, 27, 25));
        }
    }

    @FXML
    public void handleExitButton(){
        showYesNoDialog("Are you sure you want to exit?");
    }

    protected abstract boolean shouldUseKeyboard();

    protected void createKeyboard(){
        if (shouldUseKeyboard()) {

            String[] keyboardKeysNumbers = {
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                    "!", "@", "#", "$", "%", "^", "&", "*", ",", ".",
                    "⮝", "ABC", "+", "-", "?", "/", "€", "$", "SB", "⌫"
            };

            String[] keyboardKeysLetters = {
                    "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                    "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z",
                    "⮝", "123", "X", "C", "V", "B", "N", "M", "SB", "⌫"
            };

            this.keyboardKeysNumbers.addAll(new ArrayList<>(Arrays.asList(keyboardKeysNumbers)));
            this.keyboardKeysLetters.addAll(new ArrayList<>(Arrays.asList(keyboardKeysLetters)));

            int row = 0;
            int col = 0;

            HBox currentRowHBox = null;

            for (String key : keyboardKeysLetters) {
                Button button = new Button(key);
                button.setAlignment(Pos.CENTER);
                button.setMinSize(87, 65);
                button.setMaxSize(87, 65);

                if (button.getText().equals("⮝")){
                    button.setPadding(new Insets(11,0,0,0));
                }

                if (button.getText().equals("⌫")){
                    button.setFont(Font.font(11));
                }
                if (button.getText().equals(" ")){
                    button.setVisible(false);
                }

                if (button.getText().equals("123") || button.getText().equals("ABC")){
                    button.setPadding(new Insets(3,0,0,0));
                    button.setFont(Font.font(11));
                    button.setStyle("-fx-border-color: #2B2D30; -fx-border-width: 1.5px; -fx-border-radius: 2px;");
                }

                if (button.getText().equals("SB")){
                    button.setPadding(new Insets(1,0,0,0));
                    button.setStyle("-fx-border-color: #2B2D30; -fx-border-width: 1.5px; -fx-border-radius: 2px;");
                    button.setFont(Font.font(11));
                }


                if (col == 0) {
                    currentRowHBox = new HBox();
                    currentRowHBox.setAlignment(Pos.CENTER);
                    currentRowHBox.setSpacing(3);
                    keyboard.add(currentRowHBox, 0, row);
                }

                currentRowHBox.getChildren().add(button);
                this.keyboardKeys.add(button);

                col++;
                if (col == 10) {
                    col = 0;
                    row++;
                }
            }
        }

    }

    @FXML
    protected void handleFieldClicked(Event event) {
        if (shouldUseKeyboard()) {
            Object source = event.getSource();

            for (Button button : keyboardKeys) {
                if (button.getText().equals("⌫")){
                    button.setOnAction(e -> handleKeyClickErase(source));
                }else if (button.getText().equals("⮙") ||  button.getText().equals("⮝")){
                    button.setOnAction(e -> handleKeyClickChangeCase(button));
                }else if (button.getText().equals("123") ||  button.getText().equals("ABC")){
                    button.setOnAction(e -> handleKeyClickChangeKeys(button));
                } else {
                    button.setOnAction(e -> handleKeyClick(button.getText(),source));
                }
            }
        }
    }

    protected void handleKeyClickErase(Object source) {
        if (shouldUseKeyboard()) {
            if (source instanceof TextInputControl inputControl) {
                int caretPosition = inputControl.getCaretPosition();
                String text = inputControl.getText();
                if (caretPosition > 0) {
                    if (caretPosition <= text.length()) {
                        String newText = text.substring(0, caretPosition - 1) + text.substring(caretPosition);
                        inputControl.setText(newText);
                        inputControl.positionCaret(caretPosition - 1);
                    }
                }
            } else {
                System.out.println("Error: Invalid source");
            }
        }
    }

    protected void handleKeyClickChangeCase(Button key){
        if (shouldUseKeyboard()) {
            if (key.getText().equals("⮝")){
                isUpperCase = false;
                key.setText("⮙");
                key.setPadding(new Insets(7,0,0,0));

                for (Button button:keyboardKeys){
                    if (button.getText().equals("ABC") || button.getText().equals("SB")){
                        continue;
                    }
                    button.setText(button.getText().toLowerCase());
                }
            }else {
                isUpperCase = true;
                key.setText("⮝");
                key.setPadding(new Insets(11,0,0,0));
                for (Button button:keyboardKeys){
                    if (button.getText().equals("ABC") || button.getText().equals("SB")){
                        continue;
                    }
                    button.setText(button.getText().toUpperCase());
                }
            }
        }
    }

    protected void handleKeyClickChangeKeys(Button key){
        if (shouldUseKeyboard()) {
            if (key.getText().equals("123")){
                changeKeys("123");
                key.setText("ABC");
            }else {
                changeKeys("ABC");
                key.setText("123");
            }
        }
    }

    protected void changeKeys(String key) {
        if (shouldUseKeyboard()) {
            for (int i = 0; i<keyboardKeys.size(); i++){
                if (keyboardKeys.get(i).getText().equals("SB")){
                    continue;
                }
                if (keyboardKeys.get(i).getText().equals("⮙")){
                    keyboardKeys.get(i).setText("⮙");
                    keyboardKeys.get(i).setPadding(new Insets(7,0,0,0));
                }else if (keyboardKeys.get(i).getText().equals("⮝")){
                    keyboardKeys.get(i).setText("⮝");
                    keyboardKeys.get(i).setPadding(new Insets(11,0,0,0));
                }else {
                    if (key.equals("ABC")){
                        if (isUpperCase){
                            keyboardKeys.get(i).setText(keyboardKeysLetters.get(i));
                        }else {
                            keyboardKeys.get(i).setText(keyboardKeysLetters.get(i).toLowerCase());
                        }
                    }else {
                        keyboardKeys.get(i).setText(keyboardKeysNumbers.get(i));
                    }
                }
            }
        }
    }

    protected void handleKeyClick(String key, Object source) {
        if (shouldUseKeyboard()) {
            if (source instanceof TextInputControl inputControl) {
                int caretPosition = inputControl.getCaretPosition();
                String text = inputControl.getText();
                String newText = "";
                if (key.equals("SB") || key.equals("sb")){
                    newText = text.substring(0, caretPosition) + " " + text.substring(caretPosition);
                }else {
                    newText = text.substring(0, caretPosition) + key + text.substring(caretPosition);
                }
                inputControl.setText(newText);
                inputControl.positionCaret(caretPosition + 1);
            } else {
                System.out.println("Error: Invalid source");
            }
        }
    }
    @FXML
    protected void goToUserInfoView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/admin-userInfo-page-view.fxml")));
        Parent root = fxmlLoader.load();
        AdminPageUserInfoController controller = fxmlLoader.getController();
        controller.initializeWithData(user);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }
    @FXML
    protected void goToRestaurantView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/admin-restaurant-page-view.fxml")));
        Parent root = fxmlLoader.load();
        AdminPageRestaurantInfoController controller = fxmlLoader.getController();
        controller.initializeWithData(user);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }

}
