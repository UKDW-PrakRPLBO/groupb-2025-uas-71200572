package org.uas.repository;

import org.uas.data.User;

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
        ArrayList<User> users = new ArrayList<>();

        return users;
    }

    public boolean authenticateUser(String username, String password) {
        return false;
    }

    public boolean insertUser(String email, String username, String password) {

        return false;

    }

    public boolean updateUser(String email, String username, String password) {
        return false;
    }

    public boolean deleteUser(String email) {
        return false;
    }
}

