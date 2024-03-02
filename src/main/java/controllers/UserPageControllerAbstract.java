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

public abstract class UserPageControllerAbstract {

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void showYesNoDialog(String msg) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(msg);
        alert.setResizable(false);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();

        if (msg.equals("Are you sure you want to exit?")){
            if (result.isPresent() && result.get() == yesButton) {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/hello-page-view.fxml")));
                Parent root = fxmlLoader.load();
                HelloPageController controller = fxmlLoader.getController();
                controller.initialize();
                controller.setStage(stage);
                stage.getScene().setRoot(root);
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
            secondaryPane.getStyleClass().remove("split-paneDark");
            secondaryPane.getStyleClass().add("split-paneWhite");

        }
    }

    @FXML
    public void handleExitButton() throws IOException {
        showYesNoDialog("Are you sure you want to exit?");
    }

    @FXML
    protected void goToMenuView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/user-menu-page-view.fxml")));
        Parent root = fxmlLoader.load();
        UserPageMenuController controller = fxmlLoader.getController();
        controller.initializeWithData(user,islightMode);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }

    @FXML
    protected void goToInfoView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/user-orders-page-view.fxml")));
        Parent root = fxmlLoader.load();
        UserPageOrdersController controller = fxmlLoader.getController();
        controller.initializeWithData(user,islightMode);
        controller.setStage(stage);
        stage.getScene().setRoot(root);
    }

}
