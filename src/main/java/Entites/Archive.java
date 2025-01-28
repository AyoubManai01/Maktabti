package Entites;

import java.util.ArrayList;

class Archive {
    private List<BookLending> pastLendings;
    private List<BookReservation> pastReservations;

    public Archive() {
        pastLendings = new ArrayList<>();
        pastReservations = new ArrayList<>();
    }

    public void addLending(BookLending lending) {
        pastLendings.add(lending);
    }

    public void addReservation(BookReservation reservation) {
        pastReservations.add(reservation);
    }

    public List<BookLending> getPastLendings() {
        return pastLendings;
    }

    public List<BookReservation> getPastReservations() {
        return pastReservations;
    }
}
