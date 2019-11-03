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
        return this.serialNum;
    }

    public Hotel(String nameOfHotel, int theAmountOfRooms) {
        this.nameOfHotel = nameOfHotel;
        this.theAmountOfRooms = theAmountOfRooms;
        this.serialNum = UUID.randomUUID().toString();
        ArrayList<Room> list = new ArrayList<>();
        int counter = 0;
        while(counter != theAmountOfRooms){
            Room nwRoom = new Room(counter+1,true);
            list.add(nwRoom);
            counter++;
        }
        this.roomArrayList = list;
    }

    //Overload this constructor for Jackson unmarshaller
    public Hotel() {
        this.nameOfHotel = "New Hotel";
        this.theAmountOfRooms = 0;
        this.roomArrayList = new ArrayList<>();
        this.serialNum = UUID.randomUUID().toString();
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
            //如果有一个房间是空着的就返回真
            if (room.isAvailable()){
                hasAvailableRoom = true;
                break;
            }
        }
        return hasAvailableRoom;
    }

    public int assignEmptyRoom(){
        int emptyRoom = 0;
        for(Room room : roomArrayList){
            //if there's only one room available, return ture
            //如果有一个房间是空着的就返回真
            if (room.isAvailable()){
                emptyRoom = room.getSerialNum();
                break;
            }
        }
        return emptyRoom;
    }

    public void reserveRoomBySerialNum(int num){
        for(Room room : roomArrayList){
            if(room.getSerialNum() == num){
                room.setAvailable(false);
                break;
            }
        }
    }

    public  Room findRoombySerialNum(int id){
        if(!getRoomArrayList().isEmpty()){
            boolean isExist = false;
            Room roomToReturn = new Room();
            for(Room room : getRoomArrayList()){
                if (room.getSerialNum() == id){
                    isExist = true;
                    roomToReturn = room;
                    break;
                }
            }
            if(!isExist){
                return null;
            } else {
                return roomToReturn;
            }
        } else {
            return null;
        }

    }

   /* @Override
    public String toString() {
        return "Hotel{" +
                "roomArrayList=" + roomArrayList +
                ", nameOfHotel='" + nameOfHotel + '\'' +
                ", theAmountOfRooms=" + theAmountOfRooms +
                '}';
    }*/

}
