package com.maktabti.Services;

import com.maktabti.Entities.Transaction;
import com.maktabti.Utils.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
            while (rs.next()) {
                Transaction t = new Transaction(rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getTimestamp("transaction_date").toLocalDateTime(),
                        rs.getString("type"));
                transactions.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO transactions (user_id, book_id, transaction_date, type) VALUES (?, ?, ?, ?)");
            ps.setInt(1, transaction.getUserId());
            ps.setInt(2, transaction.getBookId());
            ps.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));
            ps.setString(4, transaction.getType());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> searchTransactions(String filter, String query) {
        return getAllTransactions().stream()
                .filter(transaction -> {
                    switch (filter) {
                        case "User ID": return String.valueOf(transaction.getUserId()).contains(query);
                        case "Book ID": return String.valueOf(transaction.getBookId()).contains(query);
                        case "Type": return transaction.getType().equalsIgnoreCase(query);
                        case "Date": return transaction.getTransactionDate().toString().contains(query);
                        default: return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
