package Entites;
import java.util.Date;

public class EmailNotification extends Notification {
    private String email;

    public EmailNotification(int notificationId, Date createdOn, String content, String email) {
        super(notificationId, createdOn, content);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
