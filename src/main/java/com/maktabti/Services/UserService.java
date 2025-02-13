package com.maktabti.Services;

import com.maktabti.Entities.User;
import com.maktabti.Utils.DBUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    /**
     * Fetches all users from the database.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Fetches a user by their ID from the database.
     */
    public User getUserById(int id) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }

    /**
     * Adds a new user to the database.
     */
    public boolean addUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            // Check if the user already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

            // Proceed to add the user if not already existing
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());  // Ideally, you should hash the password here
            ps.setString(3, user.getRole());
            ps.setString(4, user.getEmail()); // Insert email as well
            ps.executeUpdate();
            return true; // User added successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }

    /**
     * Updates an existing user in the database.
     */
    public boolean updateUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, role = ?, email = ? WHERE id = ?"
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Ideally, hash the password before updating
            ps.setString(3, user.getRole());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Return true if the user was updated successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }

    /**
     * Deletes a user from the database by their ID.
     */
    public boolean deleteUser(int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, userId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0; // Return true if the user was deleted successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }

    /**
     * Searches for users by username or role.
     */
    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE username LIKE ? OR role LIKE ?"
            );
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Authenticates a user by their username and password.
     */
    public User authenticate(String username, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Authentication failed
    }

    /**
     * Creates a new user in the database.
     */
    public boolean createUser(String username, String password, String email) {
        try (Connection conn = DBUtil.getConnection()) {
            // Check if the user already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

            // Hash the password before saving it to the database
            String hashedPassword = hashPassword(password);

            // Create the user in the database
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)");
            ps.setString(1, username);
            ps.setString(2, hashedPassword); // Insert hashed password
            ps.setString(3, "client");  // Default role can be "client", or modify based on your requirements
            ps.setString(4, email);  // Insert email as well
            ps.executeUpdate();
            return true; // User added successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }

    /**
     * Hashes a password using SHA-256.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();  // Return hashed password as a string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of hashing failure
    }
}