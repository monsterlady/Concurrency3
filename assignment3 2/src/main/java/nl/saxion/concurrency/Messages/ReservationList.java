package nl.saxion.concurrency.Messages;

import nl.saxion.concurrency.Moduel.Reservation;

import java.io.Serializable;
import java.util.List;

public final class ReservationList implements Serializable {
    private final List<Reservation> reservationList;

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public ReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
