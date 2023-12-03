package models;

import database.DatabaseConnection;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int id;
    private String name;
    private String ingredients;
    private String info;
    private double price;

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Dish(int id, String name, String ingredients, String info, double price) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.info = info;
        this.price = price;
    }

    public static List<Dish> retrieveAllDishes() {
        List<Dish> dishList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Dishes");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                String ingredients = resultSet.getString("Ingredients");
                String info = resultSet.getString("Additional_info");
                double price = resultSet.getDouble("Price");
                Dish dish = new Dish(id,name, ingredients, info, price);
                dishList.add(dish);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        return dishList;
    }

    public static void updateDish(Dish dish) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Dishes SET Ingredients=?, Additional_info=?, Price=?, Name=? WHERE Id=?")) {

            statement.setString(1, dish.getIngredients());
            statement.setString(2, dish.getInfo());
            statement.setDouble(3, dish.getPrice());
            statement.setString(4, dish.getName());
            statement.setInt(5, dish.id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Dish updated successfully!");
            } else {
                System.out.println("Failed to update dish.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }


    public static void deleteDish(Dish dish,int restaurantId) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM Restaurant_Dishes WHERE Dishes_Id=? AND Restaurant_Id=?")) {
                statement1.setInt(1, dish.id);
                statement1.setInt(2, restaurantId);
                statement1.executeUpdate();
            }


            try (PreparedStatement statement2 = connection.prepareStatement(
                    "DELETE FROM Dishes WHERE Id=?")) {
                statement2.setInt(1, dish.id);
                int rowsAffected = statement2.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Dish deleted successfully!");
                } else {
                    System.out.println("Dish not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateDishStatus(Dish dish, int restaurantId, String newStatus) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Restaurant_Dishes SET Dish_status=? WHERE Dishes_Id=? AND Restaurant_Id=?")) {

            statement.setString(1, newStatus);
            statement.setInt(2, dish.id);
            statement.setInt(3, restaurantId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Dish status in (Restaurant_Dishes) updated successfully!");
            } else {
                System.out.println("Failed to update dish status in (Restaurant_Dishes). Make sure the dish and restaurant exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void insertDish(Dish dish, int restaurantId, String dishStatus) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            if (!isDishNameUnique(dish.getName())) {
                System.out.println("Dish with the same name already exists. Please choose a different name.");
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Dishes (Name, Ingredients, Additional_info, Price) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, dish.getName());
                statement.setString(2, dish.getIngredients());
                statement.setString(3, dish.getInfo());
                statement.setDouble(4, dish.getPrice());
                statement.executeUpdate();
            }


            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Restaurant_Dishes (Dishes_Id, Restaurant_Id, Dish_status) VALUES (?, ?, ?)")) {
                statement.setInt(1, dish.id);
                statement.setInt(2, restaurantId);
                statement.setString(3, dishStatus);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static boolean isDishNameUnique(String dishName) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Dishes WHERE Name=?")) {
            statement.setString(1, dishName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next(); // Returns true if the ResultSet is empty (dish name is unique)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }

    public static int getNextDishId() {
        int nextUserId = -1;

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT seq FROM sqlite_sequence WHERE name = 'Dishes'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                nextUserId = resultSet.getInt("seq") + 1;
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }

        return nextUserId;
    }
}