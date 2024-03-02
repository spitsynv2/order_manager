package views;

import controllers.HelloPageController;
import database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.setProperty("prism.lcdtext", "false");

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/hello-page-view.fxml")));
        Parent root = fxmlLoader.load();
        HelloPageController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (!databaseFileExists(DatabaseConnection.getDbFilePath())) {
            InputStream createScriptStream = App.class.getResourceAsStream("/scripts/order_manager_database_create.sql");
            InputStream insertScriptStream = App.class.getResourceAsStream("/scripts/order_manager_database_insert.sql");

            if (createScriptStream != null && insertScriptStream != null) {
                try {
                    runSqlScript(createScriptStream);
                    runSqlScript(insertScriptStream);
                } finally {
                    try {
                        createScriptStream.close();
                        insertScriptStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.err.println("Failed to load SQL scripts.");
            }
        }

        launch();
    }

    private static boolean databaseFileExists(String databaseFilePath) {
        return new File(databaseFilePath).exists();
    }

    private static void runSqlScript(InputStream inputStream) {
        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             Statement statement = connection.createStatement()) {

            String line;
            StringBuilder query = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("--")) {
                    continue;
                }

                query.append(line);

                if (line.endsWith(";")) {
                    statement.execute(query.toString());
                    query.setLength(0);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
