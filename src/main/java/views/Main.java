package views;

import database.DatabaseConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String sqlCreateScriptPath = "src/main/java/database/order_manager_database_create.sql";
        String sqlInsertScriptPath = "src/main/java/database/order_manager_database_insert.sql";

        if (!databaseFileExists(DatabaseConnection.getDbFilePath())){
            runSqlScript(sqlCreateScriptPath);
            runSqlScript(sqlInsertScriptPath);
        }

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


