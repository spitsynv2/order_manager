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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;

import java.io.IOException;
import java.util.*;


public class HelloPageController {

    private Stage stage;
    @FXML private BorderPane mainPane;
    @FXML private ImageView moon_sun;
    private boolean islightMode = true;
    private Image sun;
    private Image moon;
    private Image userImage;
    private Image admin;
    @FXML private ImageView user_image;
    @FXML private Label header;
    @FXML private Button submit_button;
    @FXML private ImageView key;
    @FXML private PasswordField password;
    @FXML private TextField username;
    private boolean isUsernameField = true;
    @FXML private VBox keyVbox;
    @FXML private GridPane keyboard;
    private List<Button> keyboardKeys = new ArrayList<>();
    private List<String> keyboardKeysNumbers = new ArrayList<>();
    private List<String> keyboardKeysLetters = new ArrayList<>();
    private boolean isUpperCase = true;
    @FXML private VBox userVbox;
    private boolean isFieldActive = false;
    @FXML private ImageView board;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){
        sun = new Image(Objects.requireNonNull(getClass().getResource("/images/sun.png")).toExternalForm());
        moon = new Image(Objects.requireNonNull(getClass().getResource("/images/moon.png")).toExternalForm());

        userImage = new Image(Objects.requireNonNull(getClass().getResource("/images/user.png")).toExternalForm());
        admin = new Image(Objects.requireNonNull(getClass().getResource("/images/admin.png")).toExternalForm());

        submit_button.setStyle(
                "-fx-font-family: 'Arial';"+
                "    -fx-font-size: 2em;"+
                "    -fx-text-fill: #FFFFFF;"+
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 2, 0, 0, 0);");

        keyboard.setManaged(false);
        keyboard.setVisible(false);
        board.setVisible(false);
        username.setFocusTraversable(false);
        password.setFocusTraversable(false);

        createKeyboard();
    }

    @FXML
    public void handleChangeColorMode() {
        if (islightMode){
            islightMode = false;
            keyboard.getStyleClass().remove("gridPaneTextLight");
            keyboard.getStyleClass().add("gridPaneTextDark");
            moon_sun.setImage(sun);
            mainPane.setStyle("-fx-background-color: #1E1F22");
            header.setStyle(
                            "-fx-font-family: 'Arial';"+
                            "-fx-font-size: 4.75em;"+
                            "-fx-text-fill: #FFFFFF;");
            moon_sun.getStyleClass().remove("moon_sun_black");
            moon_sun.getStyleClass().add("moon_sun_white");
            submit_button.setStyle(
                            "-fx-font-family: 'Arial';"+
                            "    -fx-font-size: 2em;"+
                            "    -fx-text-fill: #FFFFFF;"+
                            "-fx-effect: dropshadow(three-pass-box, rgba(255, 255, 255, 0.5), 2, 0, 0, 0)");

        }else {
            islightMode = true;
            keyboard.getStyleClass().remove("gridPaneTextDark");
            keyboard.getStyleClass().add("gridPaneTextLight");
            moon_sun.setImage(moon);
            mainPane.setStyle("-fx-background-colo: #ffffff");
            header.setStyle(
                            "-fx-font-family: 'Arial';"+
                            "-fx-font-size: 4.75em;"+
                            "-fx-text-fill: #000000;");
            moon_sun.getStyleClass().remove("moon_sun_white");
            moon_sun.getStyleClass().add("moon_sun_black");
            submit_button.setStyle(
                            "-fx-font-family: 'Arial';"+
                            "    -fx-font-size: 2em;"+
                            "    -fx-text-fill: #FFFFFF;"+
                            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 2, 0, 0, 0);");

        }
    }

    @FXML
    public void handleUsernameIndicatorChange(){
        if (isUsernameField){
            isUsernameField = false;
            if (isFieldActive){
                VBox.setMargin(userVbox,new Insets(-85,40,-10,0));
            }else{
                VBox.setMargin(userVbox,new Insets(0,40,0,0));
            }
            username.setPromptText("Administrator ID");
            key.setRotate(-45);
            user_image.setImage(admin);
        }else {
            isUsernameField = true;
            if (isFieldActive){
                VBox.setMargin(userVbox,new Insets(-85,0,-10,0));
            }else{
                VBox.setMargin(userVbox,new Insets(0,0,0,0));
            }
            username.setPromptText("Username");
            key.setRotate(0);
            user_image.setImage(userImage);
        }
    }

    @FXML
    public void handleExitButton(){
        showYesNoDialog();
    }

    public void createKeyboard(){

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

    @FXML
    public void handleFieldClicked(Event event) {
        Object source = event.getSource();
        keyboard.setManaged(true);
        keyboard.setVisible(true);
        username.setFocusTraversable(true);
        password.setFocusTraversable(true);
        board.setVisible(true);
        isFieldActive = true;
        VBox.setMargin(keyVbox, new Insets(-85, 0, -20, 890));
        if (isUsernameField) {
            VBox.setMargin(userVbox, new Insets(-85, 0, -10, 0));
        } else {
            VBox.setMargin(userVbox, new Insets(-85, 40, -10, 0));
        }

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

    private void handleKeyClickErase(Object source) {
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

    private void handleKeyClickChangeCase(Button key){
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

    private void handleKeyClickChangeKeys(Button key){
        if (key.getText().equals("123")){
            changeKeys("123");
            key.setText("ABC");
        }else {
            changeKeys("ABC");
            key.setText("123");
        }

    }

    private void changeKeys(String key) {
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

    private void handleKeyClick(String key, Object source) {
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

    private void showYesNoDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to exit");
        alert.initStyle(StageStyle.UNDECORATED);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            stage.close();
        }
    }

    private void showInvalidDataDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UNDECORATED);

        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.YES);

        alert.getButtonTypes().setAll(yesButton);

        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();
    }


    @FXML
    private void handleBoardImageClick(){
        username.setFocusTraversable(false);
        password.setFocusTraversable(false);
        user_image.requestFocus();
        keyboard.setManaged(false);
        keyboard.setVisible(false);
        board.setVisible(false);
        isFieldActive = false;
        VBox.setMargin(keyVbox, new Insets(-170, 0, -35, 890));
        if (isUsernameField) {
            VBox.setMargin(userVbox,new Insets(0,0,0,0));
        } else {
            VBox.setMargin(userVbox,new Insets(0,40,0,0));
        }
    }

    @FXML
    private void handleSubmitButton() throws IOException {
        if (isFieldActive){
            handleBoardImageClick();
            User user = User.retrieveUserByName(username.getText(),password.getText());

            if (user == null){
                showInvalidDataDialog("Invalid user/admin name or password");
            }

            else if (isUsernameField){
                goToUserView(user);
            }else {
                try {
                    if (user.getPermission() == 1){
                        goToAdminView(user);
                    }else {
                        String errorMessage = "You dont have permission to go to admin panel, try login to cashier panel";

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(errorMessage);
                        alert.initOwner(stage);
                        alert.showAndWait();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }else {
            showInvalidDataDialog("Fields are empty/inactive or have incorrect data");
        }
    }

    private void goToAdminView(User admin) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/admin-userInfo-page-view.fxml")));
        Parent root = fxmlLoader.load();
        AdminPageUserInfoController controller = fxmlLoader.getController();
        controller.initializeWithData(admin, islightMode);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }

    private void goToUserView(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/user-menu-page-view.fxml")));
        Parent root = fxmlLoader.load();
        UserPageMenuController controller = fxmlLoader.getController();
        controller.initializeWithData(user, islightMode);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }

}
