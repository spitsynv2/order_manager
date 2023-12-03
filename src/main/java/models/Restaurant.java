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
    private double tax = 23;

    public Restaurant(String name, String address, String phone, String email, double tax){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.tax = tax;
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

    public String getAddress() {
        return address;
    }

    public double getTax() {
        return tax;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public String getPhone() {
        return phone;
    }

    //TODO -> setters or another implementation to change restaurant data;

    public void insertRestaurant(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO Restaurant (Name, Address, Phone, Email, Tax) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, restaurant.name);
            statement.setString(2, restaurant.address);
            statement.setString(3, restaurant.phone);
            statement.setString(4, restaurant.email);
            statement.setDouble(5, restaurant.tax);
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
                double tax = resultSet.getDouble("Tax");
                restaurant = new Restaurant(name, address, phone, email, tax);
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

    public static void updateRestaurant(Restaurant restaurant, String oldName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updateRestaurantQuery = "UPDATE Restaurant SET Address = ?, Phone = ?, Email = ?, Name = ?, Tax = ? WHERE Name = ?";
            PreparedStatement statement = connection.prepareStatement(updateRestaurantQuery);
            statement.setString(1, restaurant.address);
            statement.setString(2, restaurant.phone);
            statement.setString(3, restaurant.email);
            statement.setString(4, restaurant.name);
            statement.setDouble(5, restaurant.tax);
            statement.setString(6, oldName);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateRestaurantNameDishes(String oldRestaurantName, String newRestaurantName) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Restaurant_Dishes SET Restaurant_Name=? WHERE Restaurant_Name=?")) {

            statement.setString(1, newRestaurantName);
            statement.setString(2, oldRestaurantName);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Restaurant name updated successfully in Restaurant_Dishes!");
            } else {
                System.out.println("No records found for the given old restaurant name.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateDetails(Restaurant restaurant, String oldName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updatePrintDetailsQuery = "UPDATE Print_details SET Paper_size = ?, Additional_info = ?, Restaurant_Name = ? WHERE Restaurant_Name = ?";
            PreparedStatement statement = connection.prepareStatement(updatePrintDetailsQuery);

            statement.setString(1, restaurant.paperSize);

            if (restaurant.info != null) {
                statement.setString(2, restaurant.info);
            } else {
                statement.setString(2, "");
            }

            statement.setString(3, restaurant.name);
            statement.setString(4, oldName);

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
