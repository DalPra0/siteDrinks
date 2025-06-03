package com.DalPra.drinkmenu.dao;

import com.DalPra.drinkmenu.model.Drink;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/drink_menu_db?useSSL=false&serverTimezone=UTC";
    private String jdbcUsername = "root"; // Replace with your MySQL username
    private String jdbcPassword = "lucasdpb"; // Replace with your MySQL password

    private static final String INSERT_DRINK_SQL = "INSERT INTO drinks (name, ingredients, instructions, image_url, rating) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_DRINK_BY_ID = "SELECT id, name, ingredients, instructions, image_url, rating, created_at FROM drinks WHERE id = ?";
    private static final String SELECT_ALL_DRINKS = "SELECT id, name, ingredients, instructions, image_url, rating, created_at FROM drinks ORDER BY created_at DESC";
    private static final String SELECT_LATEST_DRINKS = "SELECT id, name, ingredients, instructions, image_url, rating, created_at FROM drinks ORDER BY created_at DESC LIMIT ?";


    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void addDrink(Drink drink) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DRINK_SQL)) {
            preparedStatement.setString(1, drink.getName());
            preparedStatement.setString(2, drink.getIngredients());
            preparedStatement.setString(3, drink.getInstructions());
            preparedStatement.setString(4, drink.getImageUrl());
            preparedStatement.setInt(5, drink.getRating());
            preparedStatement.executeUpdate();
        }
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DRINKS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                drinks.add(mapRowToDrink(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drinks;
    }

    public List<Drink> getLatestDrinks(int limit) {
        List<Drink> drinks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LATEST_DRINKS)) {
            preparedStatement.setInt(1, limit);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                drinks.add(mapRowToDrink(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drinks;
    }

    private Drink mapRowToDrink(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String ingredients = rs.getString("ingredients");
        String instructions = rs.getString("instructions");
        String imageUrl = rs.getString("image_url");
        int rating = rs.getInt("rating");
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Drink(id, name, ingredients, instructions, imageUrl, rating, createdAt);
    }
}