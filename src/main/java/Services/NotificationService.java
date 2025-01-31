package Services;

import Entites.Notification;
import Entites.EmailNotification;
import Entites.PostalNotification;

import java.util.*;

public class NotificationService {
    private final Map<Integer, Notification> notifications = new HashMap<>(); // Store notifications by ID


    public Notification createNotification(Notification notification) {
        notifications.put(notification.getNotificationId(), notification);
        return notification;
    }


    public Notification getNotification(int notificationId) {
        return notifications.get(notificationId);
    }


    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications.values());
    }


    public boolean deleteNotification(int notificationId) {
        return notifications.remove(notificationId) != null;
    }


    public boolean sendNotification(int notificationId) {
        Notification notification = notifications.get(notificationId);
        if (notification != null) {
            return notification.sendNotification(); // Simulates sending
        }
        return false;
    }


    public List<EmailNotification> getEmailNotifications() {
        List<EmailNotification> emailNotifications = new ArrayList<>();
        for (Notification notification : notifications.values()) {
            if (notification instanceof EmailNotification) {
                emailNotifications.add((EmailNotification) notification);
            }
        }
        return emailNotifications;
    }


    public List<PostalNotification> getPostalNotifications() {
        List<PostalNotification> postalNotifications = new ArrayList<>();
        for (Notification notification : notifications.values()) {
            if (notification instanceof PostalNotification) {
                postalNotifications.add((PostalNotification) notification);
            }
        }
        return postalNotifications;
    }


    public List<Notification> getUnsentNotifications() {
        List<Notification> unsentNotifications = new ArrayList<>();
        for (Notification notification : notifications.values()) {
            if (!notification.sendNotification()) { // If not sent
                unsentNotifications.add(notification);
            }
        }
        return unsentNotifications;
    }
}

