package nl.saxion.concurrency.Module;

import java.time.Instant;
import java.util.UUID;

public class Reservation {
    private String serialNum;
    private int serialNumOfRoom;
    private int serialNumOfHotel;
    private long reservationCreatedTime;

    public Reservation(int serialNumOfRoom, int serialNumOfHotel) {
        this.serialNum = UUID.randomUUID().toString();
        this.serialNumOfRoom = serialNumOfRoom;
        this.serialNumOfHotel = serialNumOfHotel;
        this.reservationCreatedTime = Instant.now().toEpochMilli();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public int getSerialNumOfRoom() {
        return serialNumOfRoom;
    }

    public int getSerialNumOfHotel() {
        return serialNumOfHotel;
    }

    public long getReservationCreatedTime() {
        return reservationCreatedTime;
    }

    @Override
    public String toString() {
        return "Reservation{ \n" +
                "Reservation Code: " + serialNum + '\n' +
                "Room Code" + serialNumOfRoom +  '\n' +
                 "Hotel Code" + serialNumOfHotel + '\n' +
                "Created Time : " + reservationCreatedTime + '\n'+
                '}' + '\n';
    }
}
