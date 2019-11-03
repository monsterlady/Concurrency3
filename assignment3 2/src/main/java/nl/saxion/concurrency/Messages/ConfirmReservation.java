package nl.saxion.concurrency.Messages;

import java.io.Serializable;

public final class ConfirmReservation implements Serializable {
    private final String serialNumOfHotel;
    private final int serialNumOfRoom;
    private final String serialNum;

    public ConfirmReservation(String serialNumOfHotel, int serialNumOfRoom,String serialNum) {
        this.serialNumOfHotel = serialNumOfHotel;
        this.serialNumOfRoom = serialNumOfRoom;
        this.serialNum = serialNum;
    }

    public String getSerialNumOfHotel() {
        return serialNumOfHotel;
    }

    public int getSerialNumOfRoom() {
        return serialNumOfRoom;
    }

    public String getSerialNum() {
        return serialNum;
    }
}
