package org.uas.repository;

import org.uas.data.User;
import org.uas.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    public void createTable() {
        // Create database tables if they don't exist
        // Implement this method to create tables for users, courses, classes, and attendance records
        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "email TEXT NOT NULL PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ")";
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(userTableSql);
                // Execute more table creation statements as needed
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Handle table creation error
            }
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                users.add(new User(email, dbUsername, dbPassword));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertUser(String email, String username, String password) {
        String sql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(String email, String username, String password) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

