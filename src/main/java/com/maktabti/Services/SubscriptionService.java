package com.maktabti.Services;

import com.maktabti.Entities.Subscription;
import com.maktabti.Entities.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {

    private Connection connection;

    public SubscriptionService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/maktabti_db", "root", "");
    }

    // Add a new subscription to the database
    public void addSubscription(Subscription subscription) {
        String query = "INSERT INTO subscriptions (user_id, start_date, end_date, fine) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, getUserIdByEmail(subscription.getEmail()));
            stmt.setDate(2, Date.valueOf(subscription.getStartDate()));
            stmt.setDate(3, Date.valueOf(subscription.getEndDate()));
            stmt.setDouble(4, subscription.getFine());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all subscriptions
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT s.id, u.email, s.start_date, s.end_date, s.fine FROM subscriptions s JOIN users u ON s.user_id = u.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Subscription subscription = new Subscription(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("fine")
                );
                subscriptions.add(subscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    // Get the user ID by email (used to relate subscriptions to users)
    private int getUserIdByEmail(String email) {
        String query = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // If no user found
    }

    // Remove a subscription by ID
    public boolean removeSubscription(int subscriptionId) {
        String sql = "DELETE FROM subscriptions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscriptionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get transactions for a specific email
    public List<Transaction> getTransactionsByEmail(String email) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.id, t.user_id, t.book_id, t.transaction_date, t.type " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "WHERE u.email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email); // Use email to fetch the userId
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getTimestamp("transaction_date").toLocalDateTime(), // Convert to LocalDateTime
                            rs.getString("type")
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
