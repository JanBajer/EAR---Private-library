package cz.cvut.fel.ear.library.model;

import cz.cvut.fel.ear.library.util.Constants;

import javax.persistence.Entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


// TODO opravit querries
@Entity
@Table(name = "RESERVATION")


@NamedQueries({
        @NamedQuery(name = "Reservation.findByUser", query = "SELECT r FROM Reservation r WHERE r.user = :user"),
        @NamedQuery(name = "Reservation.findActiveByDate", query = "SELECT r FROM Reservation r WHERE r.dateOfReservation <= :date AND r.dateOfExpiration >= :date"),
        @NamedQuery(name = "Reservation.findAllActive", query = "SELECT r FROM Reservation r WHERE r.active = true"),
        @NamedQuery(name = "Reservation.findAllActiveByBook", query = "SELECT r FROM Reservation r WHERE r.active = true AND r.book = :book"),
        @NamedQuery(name = "Reservation.findActiveByUser", query = "SELECT r FROM Reservation r WHERE r.active = true AND r.user = :user"),
})
public class Reservation extends BookState{


    @Column (nullable = false)
    private LocalDate dateOfReservation;

    @Column (nullable = false)
    private LocalDate dateOfExpiration;



    public Reservation() {
    }
    /* GETTERS AND SETTERS */

    public Reservation(Book book, User user) {
        super(user, book);
        this.user = user;
        this.book = book;
        this.active = true;
        this.dateOfReservation = LocalDate.now();
        this.dateOfExpiration = LocalDate.now().plusDays(Constants.DAYS_OF_RESERVATION); // reservation is for 5 days
    }


    public LocalDate getDateOfReservation() {
        return dateOfReservation;
    }

    public void setDateOfReservation(LocalDate dateOfReservation) {
        this.dateOfReservation = dateOfReservation;
    }

    public LocalDate getDateOfExpiration() {
        return dateOfExpiration;
    }

    public void setDateOfExpiration(LocalDate dateOfExpiration) {
        this.dateOfExpiration = dateOfExpiration;
    }


}
