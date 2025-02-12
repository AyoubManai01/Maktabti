package com.maktabti.Services;

import com.maktabti.Entities.Subscription;
import com.maktabti.Utils.DBUtil;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {

    public List<Subscription> getAllSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM subscriptions");
            while (rs.next()) {
                Subscription s = new Subscription(rs.getInt("id"),
                        rs.getString("email"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("fine"));
                subscriptions.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    public void addSubscription(Subscription subscription) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO subscriptions (email, start_date, end_date, fine) VALUES (?, ?, ?, ?)");
            ps.setString(1, subscription.getEmail());
            ps.setDate(2, Date.valueOf(subscription.getStartDate()));
            ps.setDate(3, Date.valueOf(subscription.getEndDate()));
            ps.setDouble(4, subscription.getFine());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeSubscription(int subscriptionId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM subscriptions WHERE id = ?");
            ps.setInt(1, subscriptionId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> getBorrowHistory(String email) {
        List<String[]> history = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT book_title, borrow_date, return_date FROM borrows WHERE user_email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String returnDate = (rs.getDate("return_date") != null) ? rs.getDate("return_date").toString() : "Not Returned";
                history.add(new String[]{rs.getString("book_title"), rs.getDate("borrow_date").toString(), returnDate});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public void exportBorrowHistoryToCSV(String email) {
        List<String[]> history = getBorrowHistory(email);
        String fileName = "borrow_history_" + email.replace("@", "_at_") + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Book Title,Borrow Date,Return Date\n");
            for (String[] record : history) {
                writer.append(String.join(",", record)).append("\n");
            }
            System.out.println("CSV file created: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
