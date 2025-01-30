package Services;

import Entites.Notification;
import Entites.EmailNotification;
import Entites.PostalNotification;
import java.util.*;

public class NotificationService {
    private final Map<Integer, Notification> notifications = new HashMap<>(); // Stores notifications by ID

    public Notification createNotification(Notification notification) {
        notifications.put(notification.getNotificationId(), notification);
        return notification;
    }

    public Notification getNotification(int notificationId) {
        return notifications.get(notificationId);
    }

    public Notification updateNotification(int notificationId, Notification updatedNotification) {
        if (notifications.containsKey(notificationId)) {
            notifications.put(notificationId, updatedNotification);
            return updatedNotification;
        }
        return null;
    }

    public boolean deleteNotification(int notificationId) {
        return notifications.remove(notificationId) != null;
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications.values());
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

    public boolean sendNotification(int notificationId) {
        Notification notification = notifications.get(notificationId);
        if (notification != null) {
            return notification.sendNotification();
        }
        return false;
    }
}
