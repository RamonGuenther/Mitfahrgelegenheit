package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
 * Die Klasse Booking repräsentiert die Buchung einer Fahrt durch einen Benutzer.
 * Sie enthält wichtige Angaben, wie z.B. den Mitfahrer, die Adresse des Mitfahrers
 * und ob die Fahrt nach Abschluss von beiden Seiten aus bewertet wurde.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
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
    @Embedded
    private Stopover stopover;

    private LocalDateTime dateOfBooking;
    private LocalDate regularDriveSingleDriveDate;

    private boolean ratedByPassenger;
    private boolean ratedByDriver;

    public Booking(DriveRoute driveRoute, User passenger, Stopover stopover) {
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

    public User getPassenger() {
        return passenger;
    }

    public Stopover getStopover() {
        return stopover;
    }

    public DriveRoute getDriveRoute() {
        return driveRoute;
    }

    public LocalDate getRegularDriveSingleDriveDate() {
        return regularDriveSingleDriveDate;
    }

    public LocalDateTime getDateOfBooking() {
        return dateOfBooking;
    }

    public boolean isRatedByPassenger() {
        return ratedByPassenger;
    }

    public boolean isRatedByDriver() {
        return ratedByDriver;
    }

    public void setRatedByPassenger(boolean ratedByPassenger) {
        this.ratedByPassenger = ratedByPassenger;
    }

    public void setRatedByDriver(boolean ratedByDriver) {
        this.ratedByDriver = ratedByDriver;
    }

    public void setRegularDriveSingleDriveDate(LocalDate regularDriveSingleDriveDate) {
        this.regularDriveSingleDriveDate = regularDriveSingleDriveDate;
    }

    public String getFormattedSingleDriveDate() {
        return regularDriveSingleDriveDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
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
