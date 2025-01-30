package Entites;

import java.util.Date;

public class PostalNotification extends Notification {
    private Address address;

    public PostalNotification(int notificationId, Date createdOn, String content, Address address) {
        super(notificationId, createdOn, content);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}

