package nl.saxion.concurrency.Moduel;

import nl.saxion.concurrency.ActorModuel.Broker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private String serialNum;
    private int serialNumOfRoom;
    private String serialNumOfHotel;
    private LocalDateTime reservationCreatedTime;

    public Reservation(int serialNumOfRoom, String serialNumOfHotel) {
        this.serialNum = UUID.randomUUID().toString();
        this.serialNumOfRoom = serialNumOfRoom;
        this.serialNumOfHotel = serialNumOfHotel;
        this.reservationCreatedTime = LocalDateTime.now();
    }

    public Reservation(int serialNumOfRoom, String serialNumOfHotel,String serialNum) {
        this.serialNum = serialNum;
        this.serialNumOfRoom = serialNumOfRoom;
        this.serialNumOfHotel = serialNumOfHotel;
        this.reservationCreatedTime = LocalDateTime.now();
    }

    public Reservation() {
        this.serialNum = UUID.randomUUID().toString();
        this.serialNumOfRoom = 0;
        this.serialNumOfHotel = "";
        this.reservationCreatedTime = LocalDateTime.now();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public int getSerialNumOfRoom() {
        return serialNumOfRoom;
    }

    public String getSerialNumOfHotel() {
        return serialNumOfHotel;
    }

    public LocalDateTime getReservationCreatedTime() {
        return reservationCreatedTime;
    }

    @Override
    public String toString() {
        return "Reservation{ \n" +
                "Reservation Code : " + serialNum + '\n' +
                "Room Code : " + serialNumOfRoom +  '\n' +
                 "Hotel Code : " + serialNumOfHotel + '\n' +
                "Created Time : " + reservationCreatedTime + '\n'+
                '}' + '\n';
    }
}
