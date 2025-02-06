package com.maktabti.Services;

import com.maktabti.Entities.Subscription;
import com.maktabti.Utils.DBUtil;
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
                        rs.getInt("user_id"),
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
                    "INSERT INTO subscriptions (user_id, start_date, end_date, fine) VALUES (?, ?, ?, ?)");
            ps.setInt(1, subscription.getUserId());
            ps.setDate(2, Date.valueOf(subscription.getStartDate()));
            ps.setDate(3, Date.valueOf(subscription.getEndDate()));
            ps.setDouble(4, subscription.getFine());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // New method to remove a subscription by id
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
}
