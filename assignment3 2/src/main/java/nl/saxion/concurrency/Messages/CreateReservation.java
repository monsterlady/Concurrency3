package nl.saxion.concurrency.Messages;

import nl.saxion.concurrency.Moduel.Reservation;

import java.io.Serializable;

public final class CreateReservation implements Serializable {
    private final Reservation reservation;


    public CreateReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
