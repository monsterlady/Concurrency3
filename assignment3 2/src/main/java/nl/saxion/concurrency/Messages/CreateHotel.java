package nl.saxion.concurrency.Messages;

import nl.saxion.concurrency.Moduel.Hotel;

import java.io.Serializable;

public final class CreateHotel implements Serializable {

    public final Hotel nwHotel;

    public CreateHotel(Hotel message) {
        this.nwHotel = message;
    }

    public Hotel getNwHotel() {
        return nwHotel;
    }
}
