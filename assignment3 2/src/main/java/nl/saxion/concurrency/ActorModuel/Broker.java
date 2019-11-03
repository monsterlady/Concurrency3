package nl.saxion.concurrency.ActorModuel;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import nl.saxion.concurrency.Main;
import nl.saxion.concurrency.Messages.CreateHotel;
import nl.saxion.concurrency.Moduel.Hotel;


public class Broker extends AbstractActor {
    private ActorSystem actorSystem;

    public Broker(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateHotel.class,createHotel -> {
                    Hotel nwhotel = new Hotel("Hotel " + Main.hotelList.size(),2);
                    Main.hotelList.add(nwhotel);
                })
                .build();
    }
}
