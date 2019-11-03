package nl.saxion.concurrency.ActorModuel;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import nl.saxion.concurrency.Messages.*;
import nl.saxion.concurrency.Moduel.Hotel;
import nl.saxion.concurrency.Moduel.Reservation;

import java.util.ArrayList;
import java.util.List;


public class Broker extends AbstractActor {
    private ActorSystem actorSystem;
    private static List<Hotel> hotelList = new ArrayList<Hotel>();
    private static List<Reservation> reservationList = new ArrayList<Reservation>();
    public Broker(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateHotel.class,createHotel -> {
                   Hotel nwHotel =new Hotel( createHotel.getNwHotel().getNameOfHotel(), createHotel.getNwHotel().getTheAmountOfRooms());
                   hotelList.add(nwHotel);
                })
                .match(GetAllHotels.class,getAllHotels -> getSender().tell(new HotelList(hotelList),getSelf()))
                .match(CreateReservation.class,createReservation -> {
                    Reservation nwReservation = new Reservation(createReservation.getReservation().getSerialNumOfRoom(),createReservation.getReservation().getSerialNumOfHotel(),createReservation.getReservation().getSerialNum());
                    reservationList.add(nwReservation);
                   // findHotelbySerialNum(createReservation.getReservation().getSerialNumOfHotel()).findRoombySerialNum(createReservation.getReservation().getSerialNumOfRoom()).setAvailable(false);
                })
                .match(ConfirmReservation.class, confirmReservation-> {
                   findReservationBySerialNum(confirmReservation.getSerialNum()).setConfirm(true);
                   findHotelbySerialNum(confirmReservation.getSerialNumOfHotel()).findRoombySerialNum(confirmReservation.getSerialNumOfRoom()).setAvailable(false);
                })
                .match(GetAllReservations.class,getAllReservations -> getSender().tell(new ReservationList(reservationList),getSelf()))
                .match(RemoveReservation.class,removeReservation -> {
                    findHotelbySerialNum(removeReservation.getReservationToRemove().getSerialNumOfHotel()).findRoombySerialNum(removeReservation.getReservationToRemove().getSerialNumOfRoom()).setAvailable(true);
                    reservationList.remove(removeReservation.getReservationToRemove());
                })
                .build();
    }

    public static List<Hotel> getHotelList() {
        return hotelList;
    }

    public static List<Reservation> getReservationList() {
        return reservationList;
    }

    public static void setReservationList(List<Reservation> reservationList) {
        Broker.reservationList = reservationList;
    }

    public static Hotel findHotelbySerialNum(String uuid){
        if(!getHotelList().isEmpty()){
            boolean isExist = false;
            Hotel hoteltoreturn = new Hotel();
            for(Hotel hotel : getHotelList()){
                if (hotel.getSerialNum().equals(uuid)){
                    isExist = true;
                   hoteltoreturn = hotel;
                   break;
                }
            }
            if(!isExist){
                return null;
            } else {
                return hoteltoreturn;
            }
        } else {
            return null;
        }

    }

    public static Reservation findReservationBySerialNum(String uuid){
        if(!getHotelList().isEmpty()){
            boolean isExist = false;
            Reservation reservationToReturn = new Reservation();
            for(Reservation reservation : getReservationList()){
                if (reservation.getSerialNum().equals(uuid)){
                    isExist = true;
                    reservationToReturn = reservation;
                    break;
                }
            }
            if(!isExist){
                return null;
            } else {
                return reservationToReturn;
            }
        } else {
            return null;
        }
    }
}
