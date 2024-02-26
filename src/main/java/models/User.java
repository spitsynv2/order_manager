package models;

import database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String password;
    private int permission;

    public User(int id, String name, String password, int permission){
        this.id = id;
        this.name = name;
        this.password = password;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPermission() {
        return permission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(6);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }

    public static void insertUser(User user) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO Users (Name, Password, Permission) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, user.name);
            statement.setString(2, user.password);
            statement.setInt(3, user.permission);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateUser(User user) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updateQuery = "UPDATE Users SET Name = ?, Password = ?, Permission = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getPermission());
            statement.setInt(4, user.getId()); // Assuming 'getId()' returns the user's ID
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void insertRestaurantUser(User user, Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO Restaurant_Users (User_Id, Restaurant_Id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, user.getId());
            statement.setInt(2, restaurant.getId());

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void deleteUser(User user,int restaurantId) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Delete the user from the Restaurant_Users table
            String deleteRestaurantQuery = "DELETE FROM Restaurant_Users WHERE User_Id = ? AND Restaurant_Id = ?";
            PreparedStatement deleteRestaurantStatement = connection.prepareStatement(deleteRestaurantQuery);
            deleteRestaurantStatement.setInt(1, user.getId());
            deleteRestaurantStatement.setInt(2, restaurantId);
            deleteRestaurantStatement.executeUpdate();
            deleteRestaurantStatement.close();

            // Delete the user from the Users_Orders table
            String deleteOrdersQuery = "DELETE FROM Users_Orders WHERE User_Id = ?";
            PreparedStatement deleteOrdersStatement = connection.prepareStatement(deleteOrdersQuery);
            deleteOrdersStatement.setInt(1, user.getId());
            deleteOrdersStatement.executeUpdate();
            deleteOrdersStatement.close();

            // Delete the user from the Users table
            String deleteQuery = "DELETE FROM Users WHERE ID = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, user.getId());
            deleteStatement.executeUpdate();
            deleteStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }


    public static User retrieveUserByName(String userName, String userPassword){
        User user = null;
        try {
            Connection connection = DatabaseConnection.getConnection();

            String selectQuery = "SELECT * FROM Users WHERE Name = ? AND Password = ?";

            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1,userName);
            statement.setString(2,userPassword);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                String password = resultSet.getString("Password");
                int permission = resultSet.getInt("Permission");
                user = new User(id, name, password, permission);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        return user;
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

    public static int getNextUserId() {
        int nextUserId = -1;

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT seq FROM sqlite_sequence WHERE name = 'Users'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                nextUserId = resultSet.getInt("seq") + 1;
            }else {
                nextUserId = 1;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", permission=" + permission +
                '}';
    }
}
