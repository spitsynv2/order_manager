package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    private static final String dbName = "order_manager_database.db";
    private static final String dbDirectory = System.getProperty("user.home") + File.separator + "database";
    private static final String dbFilePath = dbDirectory + File.separator + dbName;
    private static final String DB_URL = "jdbc:sqlite:" + dbFilePath;

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");

            File dbDir = new File(dbDirectory);
            if (!dbDir.exists()) {
                if (dbDir.mkdirs()) {
                    System.out.println("Database directory created at: " + dbDir.getAbsolutePath());
                } else {
                    throw new RuntimeException("Failed to create the database directory.");
                }
            }

            File dbFile = new File(dbFilePath);
            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    System.out.println("Database file created at: " + dbFile.getAbsolutePath());
                } else {
                    throw new RuntimeException("Failed to create the database file.");
                }
            }

            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize the database connection.");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDbFilePath() {
        return dbFilePath;
    }

}
