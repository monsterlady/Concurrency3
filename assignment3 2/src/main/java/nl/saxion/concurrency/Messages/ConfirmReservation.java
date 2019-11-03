package nl.saxion.concurrency.Messages;

import java.io.Serializable;

public final class ConfirmReservation implements Serializable {
    private final String serialNumOfHotel;
    private final int serialNumOfRoom;

    public ConfirmReservation(String serialNumOfHotel, int serialNumOfRoom) {
        this.serialNumOfHotel = serialNumOfHotel;
        this.serialNumOfRoom = serialNumOfRoom;
    }

    public String getSerialNumOfHotel() {
        return serialNumOfHotel;
    }

    public int getSerialNumOfRoom() {
        return serialNumOfRoom;
    }
}
