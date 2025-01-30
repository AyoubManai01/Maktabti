package Services;

import Entites.Notification;
import Entites.EmailNotification;
import Entites.PostalNotification;

import java.util.*;

public class NotificationService {
    private final Map<Integer, Notification> notifications = new HashMap<>(); // Store notifications by ID

    /**
     * Adds a new notification (Email or Postal) to the system.
     */
    public Notification createNotification(Notification notification) {
        notifications.put(notification.getNotificationId(), notification);
        return notification;
    }

    /**
     * Retrieves a specific notification by ID.
     */
    public Notification getNotification(int notificationId) {
        return notifications.get(notificationId);
    }

    /**
     * Retrieves all notifications in the system.
     */
    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications.values());
    }

    /**
     * Deletes a notification if it exists.
     */
    public boolean deleteNotification(int notificationId) {
        return notifications.remove(notificationId) != null;
    }

    /**
     * Marks a notification as sent.
     */
    public boolean sendNotification(int notificationId) {
        Notification notification = notifications.get(notificationId);
        if (notification != null) {
            return notification.sendNotification(); // Simulates sending
        }
        return false;
    }

    /**
     * Retrieves only email notifications.
     */
    public List<EmailNotification> getEmailNotifications() {
        List<EmailNotification> emailNotifications = new ArrayList<>();
        for (Notification notification : notifications.values()) {
            if (notification instanceof EmailNotification) {
                emailNotifications.add((EmailNotification) notification);
            }
        }
        return emailNotifications;
    }

    /**
     * Retrieves only postal notifications.
     */
    public List<PostalNotification> getPostalNotifications() {
        List<PostalNotification> postalNotifications = new ArrayList<>();
        for (Notification notification : notifications.values()) {
            if (notification instanceof PostalNotification) {
                postalNotifications.add((PostalNotification) notification);
            }
        }
        return postalNotifications;
    }

    /**
     * Retrieves unread (unsent) notifications.
     */
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

