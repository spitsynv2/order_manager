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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String sqlCreateScriptPath;
        sqlCreateScriptPath = null;
        String sqlInsertScriptPath = null;
        try {
            URI createScriptURI = Objects.requireNonNull(App.class.getResource("/scripts/order_manager_database_create.sql")).toURI();
            URI insertScriptURI = Objects.requireNonNull(App.class.getResource("/scripts/order_manager_database_insert.sql")).toURI();
            sqlCreateScriptPath = Paths.get(createScriptURI).toString();
            sqlInsertScriptPath = Paths.get(insertScriptURI).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (!databaseFileExists(DatabaseConnection.getDbFilePath())){
            runSqlScript(sqlCreateScriptPath);
            runSqlScript(sqlInsertScriptPath);
        }

        launch();
    }

    private static boolean databaseFileExists(String databaseFilePath) {
        return new File(databaseFilePath).exists();
    }

    private static void runSqlScript(String sqlScriptPath) {
        try (Connection connection = DatabaseConnection.getConnection();
             InputStream inputStream = Files.newInputStream(Paths.get(sqlScriptPath));
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
