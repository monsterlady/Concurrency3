package nl.saxion.concurrency.Messages;

import nl.saxion.concurrency.Moduel.Hotel;

import java.io.Serializable;
import java.util.List;

public final class HotelList implements Serializable {
    private final List<Hotel>  hotelArrayList;

    public HotelList(List<Hotel> hotelArrayList) {
        this.hotelArrayList = hotelArrayList;
    }

    public List<Hotel> getHotelArrayList() {
        return hotelArrayList;
    }
}
