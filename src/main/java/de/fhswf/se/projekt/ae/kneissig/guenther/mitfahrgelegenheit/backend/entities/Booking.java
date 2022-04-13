package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User passenger;

    @ManyToOne
    private DriveRoute driveRoute;

    private LocalDateTime dateOfBooking;

    @Embedded
    private Stopover stopover;

    public Booking(DriveRoute driveRoute,User passenger, Stopover stopover)
    {
        nullCheck(driveRoute, passenger, stopover);

        this.driveRoute = driveRoute;
        this.passenger = passenger;
        this.dateOfBooking = LocalDateTime.now();
        this.stopover = stopover;
    }

    public Booking() {

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

}
