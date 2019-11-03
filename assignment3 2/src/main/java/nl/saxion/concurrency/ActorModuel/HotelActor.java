package nl.saxion.concurrency.ActorModuel;

import akka.actor.AbstractActor;
import nl.saxion.concurrency.Moduel.Hotel;

public class HotelActor extends AbstractActor {
    private final Hotel hotel;

    public HotelActor(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
