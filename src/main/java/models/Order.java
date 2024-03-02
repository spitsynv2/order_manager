package models;

import database.DatabaseConnection;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class Order {

    private int id;
    private long date;
    private String payment;
    private String status;
    private List<Dish> dishes;
    private List<String> dishesStrings;

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void setDishesStrings(List<String> dishesStrings) {
        this.dishesStrings = dishesStrings;
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

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        Instant instant = Instant.ofEpochMilli(date);

        ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public void saveToDatabase(Restaurant restaurant, User user) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO orders(date, Payment_type, status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, date+"");
                statement.setString(2, payment);
                statement.setString(3, status);
                statement.executeUpdate();
            }

            String sqlToRestaurantOrders = "INSERT INTO Orders_Restaurant(Restaurant_Id, Order_Id) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sqlToRestaurantOrders)) {
                statement.setInt(1, restaurant.getId());
                statement.setInt(2, id);
                statement.executeUpdate();
            }

            String sqlToUsersOrders = "INSERT INTO Users_Orders(User_Id, Order_Id) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sqlToUsersOrders)) {
                statement.setInt(1, user.getId());
                statement.setInt(2, id);
                statement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
    }

    public void makeOrder(Restaurant restaurant, User user){
        saveToDatabase(restaurant, user);
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
            String sql = "SELECT * FROM orders ORDER BY Date DESC";
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

    public static Map<String, Integer> getOrderCountsThisWeek() {
        Map<String, Integer> orderCountsByDate = new LinkedHashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            LocalDate currentDate = LocalDate.now();
            LocalDate startDateOfWeek = currentDate.with(DayOfWeek.MONDAY);

            LocalDateTime startOfWeek = startDateOfWeek.atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusDays(7).minusSeconds(1);

            long startEpochMilli = startOfWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endEpochMilli = endOfWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            String sql = "SELECT Date, COUNT(*) AS OrderCount FROM Orders " +
                    "WHERE Date >= ? AND Date <= ? GROUP BY Date ORDER BY Date ASC ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, startEpochMilli);
                statement.setLong(2, endEpochMilli);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long epochMilli = resultSet.getLong("Date");
                        LocalDate localDate = Instant.ofEpochMilli(epochMilli)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String date = localDate.format(formatter);

                        int orderCount = resultSet.getInt("OrderCount");

                        if (orderCount == 0){
                            continue;
                        }

                        if (!orderCountsByDate.containsKey(date)){
                            orderCountsByDate.put(date, orderCount);
                        }else {
                            orderCountsByDate.replace(date,orderCountsByDate.get(date)+orderCount);
                        }

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }

        return orderCountsByDate;
    }

    public static List<Order> getAllOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM orders WHERE Status = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, status);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("Id");
                        long date = Long.parseLong(resultSet.getString("Date"));
                        String payment = resultSet.getString("Payment_type");
                        String orderStatus = resultSet.getString("Status");

                        Order order = new Order(id, date, payment, orderStatus);
                        orders.add(order);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }

        return orders;
    }


    public static void updateOrderStatus(int orderId, String status) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Orders SET Status=? WHERE Id=?")) {

            statement.setString(1, status);
            statement.setInt(2, orderId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order status updated successfully!");
            } else {
                System.out.println("Failed to update order status.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public List<String> getDishesByOrderId() {
        List<String> orderDishes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Orders_Dishes WHERE Order_Id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int dishId = resultSet.getInt("Dish_Id");
                        String dishName = resultSet.getString("Dish_name");
                        orderDishes.add(dishName);
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

    public static Set<String> getUniqueDishesByOrderId(int orderId) {
        Set<String> uniqueDishes = new HashSet<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT DISTINCT Dish_name FROM Orders_Dishes WHERE Order_Id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, orderId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String dishName = resultSet.getString("Dish_name");
                        uniqueDishes.add(dishName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }

        return uniqueDishes;
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
                ", dishes=" + dishesStrings +
                '}';
    }
}

