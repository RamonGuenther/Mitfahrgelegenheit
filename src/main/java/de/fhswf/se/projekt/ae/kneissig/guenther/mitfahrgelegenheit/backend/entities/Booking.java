package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Booking {

    @Id
    private Integer id;

    @ManyToOne
    private User passenger;

    @ManyToOne
    private DriveRoute driveRoute;

    private final LocalDateTime dateOfBooking;

    @Embedded
    private Stopover stopover;

    public Booking(DriveRoute driveRoute,User passenger, LocalDateTime dateOfBooking, Stopover stopover)
    {
        this.driveRoute = driveRoute;
        this.passenger = passenger;
        this.dateOfBooking = dateOfBooking;
        this.stopover = stopover;
        id = hashCode();
    }

    public Booking() {
        passenger = null;
        dateOfBooking = null;
    }

    public Integer getId() {
        return id;
    }

    public User getPassenger()
    {
        return passenger;
    }


    public LocalDateTime getDateOfBooking()
    {
        return dateOfBooking;
    }

    public Stopover getStopover() {
        return stopover;
    }

    public DriveRoute getDriveRoute() {
        return driveRoute;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Booking) && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger,driveRoute);
    }

}
