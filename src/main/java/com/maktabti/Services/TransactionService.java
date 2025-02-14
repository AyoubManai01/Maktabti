package com.maktabti.Services;

import com.maktabti.Entities.Transaction;
import com.maktabti.Utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT id, user_id, book_id, transaction_date, type FROM transactions";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getTimestamp("transaction_date").toLocalDateTime(),
                        rs.getString("type")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
        }

        return transactions;
    }

    // Add a transaction to the database
    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (user_id, book_id, transaction_date, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, transaction.getUserId());
            ps.setInt(2, transaction.getBookId());
            ps.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));
            ps.setString(4, transaction.getType());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setId(generatedKeys.getInt(1)); // Set the generated ID for the transaction
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error adding transaction: " + e.getMessage());
        }
    }

    // Search for transactions by user ID
    public List<Transaction> searchTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT id, user_id, book_id, transaction_date, type FROM transactions WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getTimestamp("transaction_date").toLocalDateTime(),
                            rs.getString("type")
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error searching transactions by user ID: " + e.getMessage());
        }

        return transactions;
    }

    // Delete a transaction by its ID
    public void deleteTransaction(int transactionId) {
        String query = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error deleting transaction: " + e.getMessage());
        }
    }

    // Check if a book ID exists in the books table
    public boolean bookExists(int bookId) {
        String query = "SELECT 1 FROM books WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a row is found
            }
        } catch (SQLException e) {
            logger.severe("Error checking if book exists: " + e.getMessage());
        }
        return false;
    }

    // Check if a user ID exists in the users table
    public boolean userExists(int userId) {
        String query = "SELECT 1 FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a row is found
            }
        } catch (SQLException e) {
            logger.severe("Error checking if user exists: " + e.getMessage());
        }
        return false;
    }
}
