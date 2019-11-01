package nl.saxion.concurrency.Module;

public class Room {
    private int numOfRoom;
    private boolean isAvailable;
    //private boolean staked;

    public Room(int serialNum, boolean isAvailable) {
        this.numOfRoom = serialNum;
        this.isAvailable = isAvailable;
    }

    public int getSerialNum() {
        return numOfRoom;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setSerialNum(int serialNum) {
        this.numOfRoom = serialNum;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}


