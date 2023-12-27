package models;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.SignStyle;

public class Restaurant {

    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String paperSize;
    private String info;
    private double tax = 23;
    private String to_print;

    public Restaurant(int id, String name, String address, String phone, String email, double tax, String to_print){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.tax = tax;
        this.to_print = to_print;
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

    public void setTo_print(String to_print) {
        this.to_print = to_print;
    }

    public String getTo_print() {
        return to_print;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public static Restaurant retrieveRestaurant(){
        Restaurant restaurant = null;
        try {
            Connection connection = DatabaseConnection.getConnection();

            String selectQuery = "SELECT * FROM Restaurant WHERE Id = 1";
            PreparedStatement statement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                String email = resultSet.getString("Email");
                double tax = resultSet.getDouble("Tax");
                String to_print = resultSet.getString("ToPrint");
                restaurant = new Restaurant(id,name, address, phone, email, tax,to_print);
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

            String selectQuery = "SELECT Paper_size, Additional_info FROM Print_details WHERE Restaurant_Id = 1";
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

    public static void updateRestaurant(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updateRestaurantQuery = "UPDATE Restaurant SET Address = ?, Phone = ?, Email = ?, Name = ?, Tax = ? WHERE Id = ?";
            PreparedStatement statement = connection.prepareStatement(updateRestaurantQuery);
            statement.setString(1, restaurant.address);
            statement.setString(2, restaurant.phone);
            statement.setString(3, restaurant.email);
            statement.setString(4, restaurant.name);
            statement.setDouble(5, restaurant.tax);
            statement.setInt(6, restaurant.id);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateDetails(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updatePrintDetailsQuery = "UPDATE Print_details SET Paper_size = ?, Additional_info = ? WHERE Restaurant_Id = ?";
            PreparedStatement statement = connection.prepareStatement(updatePrintDetailsQuery);

            statement.setString(1, restaurant.paperSize);

            if (restaurant.info != null) {
                statement.setString(2, restaurant.info);
            } else {
                statement.setString(2, "");
            }

            statement.setInt(3, restaurant.id);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static void updateRestaurantToPrint(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String updateRestaurantQuery = "UPDATE Restaurant SET ToPrint = ? WHERE Id = ?";
            PreparedStatement statement = connection.prepareStatement(updateRestaurantQuery);
            statement.setString(1, restaurant.to_print);
            statement.setInt(2, restaurant.id);

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
