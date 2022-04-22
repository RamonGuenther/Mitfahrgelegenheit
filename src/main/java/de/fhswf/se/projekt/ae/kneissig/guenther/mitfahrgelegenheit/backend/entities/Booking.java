package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate regularDriveSingleDriveDate;

    @Embedded
    private Stopover stopover;

    private boolean ratedByPassenger;
    private boolean ratedByDriver;

    public Booking(DriveRoute driveRoute,User passenger, Stopover stopover)
    {
        nullCheck(driveRoute, passenger, stopover);

        this.driveRoute = driveRoute;
        this.passenger = passenger;
        this.dateOfBooking = LocalDateTime.now();
        this.stopover = stopover;
        this.ratedByDriver = false;
        this.ratedByPassenger = false;
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

    public boolean isRatedByPassenger() {
        return ratedByPassenger;
    }

    public void setRatedByPassenger(boolean ratedByPassenger) {
        this.ratedByPassenger = ratedByPassenger;
    }

    public boolean isRatedByDriver() {
        return ratedByDriver;
    }

    public void setRatedByDriver(boolean ratedByDriver) {
        this.ratedByDriver = ratedByDriver;
    }

    public LocalDate getRegularDriveSingleDriveDate() {
        return regularDriveSingleDriveDate;
    }

    public void setRegularDriveSingleDriveDate(LocalDate regularDriveSingleDriveDate) {
        this.regularDriveSingleDriveDate = regularDriveSingleDriveDate;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Booking) && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger, driveRoute);
    }
}
