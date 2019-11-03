package nl.saxion.concurrency;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.remote.WireFormats;
import akka.util.Timeout;
import nl.saxion.concurrency.Messages.CreateHotel;
import nl.saxion.concurrency.Moduel.Hotel;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Routers extends AllDirectives {
    private ActorSystem actorSystem;
    private ActorRef broker;
    private final static Timeout timeout = new Timeout(100, TimeUnit.MILLISECONDS);

    public Routers(ActorSystem actorSystem,ActorRef broker) {
        this.actorSystem = actorSystem;
        this.broker = broker;
    }


    public Route allroutes(){
        return route(
               path("newHotel",() -> route(createNewHotel()))
        );
    }

    private Route createNewHotel(){
        return post(() ->
                entity(Jackson.unmarshaller(Hotel.class),
                        hotel -> {
                            Patterns.ask(broker, new CreateHotel(hotel),timeout);
                            return complete(hotel.getNameOfHotel() + " has been created");
                        }
                        )
                );
    }
}
