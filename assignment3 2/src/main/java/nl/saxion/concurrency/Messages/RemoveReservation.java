package nl.saxion.concurrency.Messages;

import nl.saxion.concurrency.Moduel.Reservation;

import java.io.Serializable;

public final class RemoveReservation implements Serializable {
    private final Reservation reservationToRemove;

    public RemoveReservation(Reservation reservationToRemove) {
        this.reservationToRemove = reservationToRemove;
    }

    public Reservation getReservationToRemove() {
        return reservationToRemove;
    }
}
