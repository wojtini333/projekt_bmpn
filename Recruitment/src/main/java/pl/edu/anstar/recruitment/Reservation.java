package pl.edu.anstar.recruitment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reservation {

    private static Logger LOG = LoggerFactory.getLogger(Reservation.class);

    public Reservation( String firstName, String lastName, String email, String phone_number, String type_of_trip, String date_of_booking) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone_number = phone_number;
        this.type_of_trip = type_of_trip;
        this.date_of_booking = date_of_booking;
    }

    private int reservation_id;

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getType_of_trip() {
        return type_of_trip;
    }

    public void setType_of_trip(String type_of_trip) {
        this.type_of_trip = type_of_trip;
    }

    public String getDate_of_booking() {
        return date_of_booking;
    }

    public void setDate_of_booking(String date_of_booking) {
        this.date_of_booking = date_of_booking;
    }

    private String firstName;
    private String lastName;
    private String email;
    private String phone_number;
    private String type_of_trip;
    private String date_of_booking;

    public Reservation() {
    }


}