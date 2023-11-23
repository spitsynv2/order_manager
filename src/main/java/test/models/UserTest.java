package test.models;

import database.DatabaseConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTest {

    public static void main(String[] args) {
        System.out.println(retrieveAllRestaurant_Users());
    }
    public static List<User> retrieveAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                String password = resultSet.getString("Password");
                int permission = resultSet.getInt("Permission");
                User user = new User(id, name, password, permission);
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        return userList;
    }

    public static List<String> retrieveAllRestaurant_Users() {
        List<String> Restaurant_Users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Restaurant_Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String restaurantName = resultSet.getString("Restaurant_Name");
                String userId = resultSet.getString("User_Id");
                Restaurant_Users.add("("+restaurantName +", UserId: " + userId+")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        return Restaurant_Users;
    }



}
