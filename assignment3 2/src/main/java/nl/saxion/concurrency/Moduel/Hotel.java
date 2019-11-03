package nl.saxion.concurrency.Moduel;

import akka.actor.AbstractActor;
import nl.saxion.concurrency.Moduel.Room;

import java.util.ArrayList;
import java.util.UUID;

public class Hotel {
    private ArrayList<Room> roomArrayList;
    private String nameOfHotel;
    private String serialNum;
    private int theAmountOfRooms;

    public Hotel(ArrayList<Room> roomArrayList, String nameOfHotel, int theAmountOfRooms) {
        this.roomArrayList = roomArrayList;
        this.nameOfHotel = nameOfHotel;
        this.theAmountOfRooms = theAmountOfRooms;
        this.serialNum = UUID.randomUUID().toString();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public Hotel(String nameOfHotel, int theAmountOfRooms) {
        this.nameOfHotel = nameOfHotel;
        this.theAmountOfRooms = theAmountOfRooms;
        this.serialNum = UUID.randomUUID().toString();
        roomArrayList = new ArrayList<Room>();
        int counter = 0;
        while(counter != theAmountOfRooms){
            Room nwRoom = new Room(counter,true);
            roomArrayList.add(nwRoom);
            counter++;
        }
    }

    public void setRoomArrayList(ArrayList<Room> roomArrayList) {
        this.roomArrayList = roomArrayList;
    }

    public void setNameOfHotel(String nameOfHotel) {
        this.nameOfHotel = nameOfHotel;
    }

    public void setTheAmountOfRooms(int theAmountOfRooms) {
        this.theAmountOfRooms = theAmountOfRooms;
    }

    public ArrayList<Room> getRoomArrayList() {
        return roomArrayList;
    }

    public String getNameOfHotel() {
        return nameOfHotel;
    }

    public int getTheAmountOfRooms() {
        return theAmountOfRooms;
    }

    public boolean isHotelAvailable(){
        boolean hasAvailableRoom = false;
        for(Room room : roomArrayList){
            //if there's only one room available, return ture
            if (room.isAvailable()){
                hasAvailableRoom = true;
                break;
            }
        }
        return hasAvailableRoom;
    }

    public void reserveRoomBySerialNum(int num){
        for(Room room : roomArrayList){
            if(room.getSerialNum() == num){
                room.setAvailable(false);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "roomArrayList=" + roomArrayList +
                ", nameOfHotel='" + nameOfHotel + '\'' +
                ", theAmountOfRooms=" + theAmountOfRooms +
                '}';
    }

}
