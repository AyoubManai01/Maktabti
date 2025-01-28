package Entites;
import java.util.Date;

public class BookReservation {
    public Date creationDate;
    public ReservationStatus status;

    public ReservationStatus getStatus(){
        return status;
    }
    public BookReservation fetchReservationDetails(){
        return this;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
