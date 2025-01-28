package Entites;
import java.util.Date;

class Notification {
    private int notificationId;
    private Date createdOn;
    private String content;

    public Notification(int notificationId, Date createdOn, String content) {
        this.notificationId = notificationId;
        this.createdOn = createdOn;
        this.content = content;
    }

    public boolean sendNotification() {
        return true;
    }
}
