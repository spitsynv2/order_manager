package models;

import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

public class Order {

    private int id;
    private long date;
    private String payment;
    private String status;
    private List<Dish> dishes;

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Order(int id, long date, String payment, String status) {
        this.id = id;
        this.date = date;
        this.payment = payment;
        this.status = status;
    }

    public Order(int id, String payment, String status) {
        this.id = id;
        this.date = new Date().getTime();
        this.payment = payment;
        this.status = status;
    }

    public void saveToDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO orders(date, Payment_type, status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, date+"");
                statement.setString(2, payment);
                statement.setString(3, status);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
    }

    public void makeOrder(){
        saveToDatabase();
        for (Dish dish : dishes) {
            saveToOrderDishes(dish);
        }
    }

    public void saveToOrderDishes(Dish dish) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Orders_Dishes(Order_Id, Dish_Id, Dish_Name) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.setInt(2, dish.getId());
                statement.setString(3, dish.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
    }

    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM orders";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("Id");
                        long date = Long.parseLong(resultSet.getString("Date"));
                        String payment = resultSet.getString("Payment_type");
                        String status = resultSet.getString("Status");

                        Order order = new Order(id, date, payment, status);
                        orders.add(order);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }

        return orders;
    }

    public List<Dish> getDishesByOrderId() {
        List<Dish> orderDishes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Orders_Dishes WHERE Order_Id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int dishId = resultSet.getInt("Dish_Id");
                        String dishName = resultSet.getString("Dish_name");
                        Dish orderDish = Dish.getDishById(dishId);
                        orderDishes.add(orderDish);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }

        return orderDishes;
    }

    public static int getNextOrderId() {
        int nextUserId = -1;

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT seq FROM sqlite_sequence WHERE name = 'Orders'";
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
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", payment='" + payment + '\'' +
                ", status='" + status + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}

