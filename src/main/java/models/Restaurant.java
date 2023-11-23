package models;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Restaurant {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String paperSize;
    private String info;

    public Restaurant(String name, String address, String phone, String email){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    //TODO -> setters or another implementation to change restaurant data;

    public void insertRestaurant(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO Restaurant (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, restaurant.name);
            statement.setString(2, restaurant.address);
            statement.setString(3, restaurant.phone);
            statement.setString(4, restaurant.email);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public void insertDetails(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO Print_details (Restaurant_Name, Paper_size, Additional_info) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, restaurant.name);
            statement.setString(2, restaurant.paperSize);
            if (info != null){
                statement.setString(3, restaurant.info);
            }
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static Restaurant retrieveRestaurant(){
        Restaurant restaurant = null;
        try {
            Connection connection = DatabaseConnection.getConnection();

            String selectQuery = "SELECT * FROM Restaurant";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                String email = resultSet.getString("Email");
                restaurant = new Restaurant(name, address, phone, email);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
        retrieveDetails(restaurant);
        return restaurant;
    }

    public static void retrieveDetails(Restaurant restaurant){
        try {
            Connection connection = DatabaseConnection.getConnection();

            String selectQuery = "SELECT Paper_size, Additional_info FROM Print_details";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String paperSize = resultSet.getString("Paper_size");
                String additionalInfo = resultSet.getString("Additional_info");
                restaurant.setPaperSize(paperSize);
                restaurant.setInfo(additionalInfo);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public void updateRestaurant(Restaurant restaurant, String newName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Update restaurant details
            String updateRestaurantQuery = "UPDATE Restaurant SET Address = ?, Phone = ?, Email = ?, Name = ? WHERE Name = ?";
            PreparedStatement statement = connection.prepareStatement(updateRestaurantQuery);
            statement.setString(1, restaurant.address);
            statement.setString(2, restaurant.phone);
            statement.setString(3, restaurant.email);
            if (newName != null){
                statement.setString(4, newName);
                restaurant.updateDetails(restaurant,newName);
            }else {
                statement.setString(4, restaurant.name);
            }
            statement.setString(5, restaurant.name);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public void updateDetails(Restaurant restaurant, String newRestaurantName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updatePrintDetailsQuery = "UPDATE Print_details SET Paper_size = ?, Additional_info = ?, Restaurant_Name = ? WHERE Restaurant_Name = ?";
            PreparedStatement statement = connection.prepareStatement(updatePrintDetailsQuery);

            statement.setString(1, restaurant.paperSize);
            if (info != null) {
                statement.setString(2, restaurant.info);
            } else {
                statement.setString(2, "");
            }
            if (newRestaurantName != null){
                statement.setString(3, newRestaurantName);
            }else {
                statement.setString(3, restaurant.name);
            }
            statement.setString(4, restaurant.name);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }


    private boolean checkRestaurantExists(Connection connection) throws SQLException {
        String checkRestaurantQuery = "SELECT COUNT(*) FROM Restaurant";
        PreparedStatement statement  = connection.prepareStatement(checkRestaurantQuery);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return count > 0;
    }

}
