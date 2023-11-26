package views;

import controllers.AdminPageRestaurantInfoController;
import controllers.HelloPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.setProperty("prism.lcdtext", "false");

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/restaurant-page-view.fxml")));
        Parent root = fxmlLoader.load();
        AdminPageRestaurantInfoController controller = fxmlLoader.getController();
        controller.initializeWithData();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
