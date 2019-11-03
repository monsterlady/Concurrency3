package nl.saxion.concurrency;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.scaladsl.server.PathMatcher;
import akka.pattern.Patterns;
import akka.http.javadsl.server.PathMatchers;
import akka.util.Timeout;
import nl.saxion.concurrency.ActorModuel.Broker;
import nl.saxion.concurrency.Messages.*;
import nl.saxion.concurrency.Moduel.Hotel;
import nl.saxion.concurrency.Moduel.Reservation;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CustomRouter extends AllDirectives {
    private ActorSystem actorSystem;
    private ActorRef broker;
    private final static Timeout timeout = new Timeout(1000, TimeUnit.MILLISECONDS);


    public CustomRouter(ActorSystem actorSystem, ActorRef broker) {
        this.actorSystem = actorSystem;
        this.broker = broker;
    }


    public Route allroutes(){
        return route(
               path("newhotel",() -> route(createNewHotel())),
                path("hotels",() -> route(getAllHotels())),
                path("reservations",()->route(getAllReservations())),
                createReservation(),
                confirmReservation(),
                cancleReservation()

        );
    }

    private Route cancleReservation() {
        return concat(
                pathPrefix("cancel",()-> concat(
                        get(() ->
                                path(PathMatchers.segment(), (reservationSerialNum)->{
                                    if(Broker.findReservationBySerialNum(reservationSerialNum) == null){
                                        return complete("Reservation not found");
                                    } else {
                                        Reservation reservation = Broker.findReservationBySerialNum(reservationSerialNum);
                                        assert reservation != null;
                                        Patterns.ask(broker, new RemoveReservation(reservation), timeout);
                                        return complete("Cancel Success, The Reservation "+ reservation.getSerialNum() + " has been canceled.\n"
                                                + "You could find other hotel by clicking the link below:\n" + "http://localhost:3000/hotels"
                                        );
                                    }

                                }))
                ))
        );
    }

    private Route createNewHotel(){
        return post(() -> entity(Jackson.unmarshaller(Hotel.class), hotel -> {
                          Patterns.ask(broker, new CreateHotel(hotel),timeout);
                          return complete("Hotel: " + hotel.getNameOfHotel() + " has been created");
                      }));
    }

    private Route getAllHotels() {
        return get(
                () ->
                {
                    Future<Object> hotelList = Patterns.ask(broker, new GetAllHotels(), timeout);
                    return completeOKWithFuture(hotelList, Jackson.marshaller());
                }
        );
    }

    private Route createReservation(){
        return concat(
                pathPrefix("reservation", ()-> concat(
                        get(() ->
                                path(PathMatchers.segment(),(uuid) ->{
                                            if(Broker.findHotelbySerialNum(uuid) == null ){
                                                return complete("Hotel not found");
                                            } else {
                                                if(!Broker.findHotelbySerialNum(uuid).isHotelAvailable()){
                                                    return complete(Broker.findHotelbySerialNum(uuid).getNameOfHotel() +" is full");
                                                } else {
                                                    Reservation nwreservation = new Reservation(Broker.findHotelbySerialNum(uuid).assignEmptyRoom(),uuid);
                                                    Patterns.ask(broker, new CreateReservation(nwreservation), timeout);
                                                    return complete("Your Reservation of "+ Broker.findHotelbySerialNum(uuid).getNameOfHotel()+ " has been created \n" +nwreservation.toString() + "\n Please confirm your Reservation by Clicking the link below within 15 minutes: \n" + "http://localhost:3000/confirm/"+ nwreservation.getSerialNum());
                                                }
                                            }
                                        }
                                        )

                                )
                )));
    }

    private Route confirmReservation(){
        return concat(
                pathPrefix("confirm",()-> concat(
                        get(() ->
                                path(PathMatchers.segment(), (reservationSerialNum)->{
                                    if(Broker.findReservationBySerialNum(reservationSerialNum) == null){
                                        return complete("Reservation not found");
                                    } else {
                                        Reservation reservation = Broker.findReservationBySerialNum(reservationSerialNum);
                                        assert reservation != null;
                                        Patterns.ask(broker, new ConfirmReservation(reservation.getSerialNumOfHotel(),reservation.getSerialNumOfRoom()), timeout);
                                        return complete("Thanks for the confirmation, The Room "+ reservation.getSerialNumOfRoom() + " of " + Broker.findHotelbySerialNum(reservation.getSerialNumOfHotel()).getNameOfHotel()+ " has been confirmed.\n"
                                        + "You could cancel it anytime by click the link below:\n" + "http://localhost:3000/cancel/"+ reservationSerialNum
                                        );
                                    }

                                }))
                ))
        );

    }

    private Route getAllReservations(){
        return get(() ->{
            Future<Object> reservations = Patterns.ask(broker, new GetAllReservations(), timeout);
            return completeOKWithFuture(reservations, Jackson.marshaller());
        });
    }
}
