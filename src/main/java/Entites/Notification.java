package Entites;
import java.util.Date;

public class Notification {
    public int notificationId;
    public Date createdOn;
    private String content;

    public int getNotificationId() {
        return notificationId;
    }

    public Notification(int notificationId, Date createdOn, String content) {
        this.notificationId = notificationId;
        this.createdOn = createdOn;
        this.content = content;
    }

    public boolean sendNotification() {
        return true;
    }
}