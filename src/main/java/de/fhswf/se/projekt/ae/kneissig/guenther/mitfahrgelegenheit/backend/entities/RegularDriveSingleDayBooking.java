package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Die Klasse RegularDriveSingleDayBooking ist speziell für Buchungen, bei der
 * ein einzelner Tag einer eigentlich regelmäßigen Fahrt gebucht werden soll.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */

@Entity
//public class RegularDriveSingleDayBooking{
public class RegularDriveSingleDayBooking extends Booking{

    public LocalDate driveDate;

    public RegularDriveSingleDayBooking(DriveRoute driveRoute,User passenger, Stopover stopover, LocalDate driveDate){
        super(driveRoute,passenger,stopover);
        this.driveDate = driveDate;
    }

    public RegularDriveSingleDayBooking(){

    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }
}
