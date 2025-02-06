package com.maktabti.Services;

import com.maktabti.Entities.Transaction;
import com.maktabti.Utils.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // New method to remove a transaction by id
    public boolean removeTransaction(int transactionId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM transactions WHERE id = ?");
            ps.setInt(1, transactionId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
