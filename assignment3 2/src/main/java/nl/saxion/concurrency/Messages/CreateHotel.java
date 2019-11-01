package nl.saxion.concurrency.Messages;

import java.io.Serializable;

public class CreateHotel implements Serializable {
    public final String message;

    public CreateHotel(String message) {
        this.message = message;
    }
}
