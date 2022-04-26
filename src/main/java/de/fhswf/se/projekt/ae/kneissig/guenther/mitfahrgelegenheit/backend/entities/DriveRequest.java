package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;

/**
 * Die Klasse DriveRequest repräsentiert eine Anfrage zu einem Fahrtangebot.
 * Sie enthält zum Beispiel den Nutzer, der die Anfrage stellt, die Adresse, an der
 * er abgeholt werden möchte, samt Link für googleMaps, ggf. eine Notiz und den
 * aktuellen Status der Anfrage.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Entity
public class DriveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User passenger;

    @ManyToOne
    private DriveRoute driveRoute;

    private RequestState requestState;
    private String note;
    private String currentRouteLink;
    private LocalDateTime requestTime;
    private LocalDate regularDriveSingleDriveDate;

    @Embedded
    private Stopover stopover;

    public DriveRequest(DriveRoute driveRoute, User passenger, String note, String currentRouteLink, Stopover stopover) {

        nullCheck(driveRoute, passenger, stopover);

        this.driveRoute = driveRoute;
        this.requestState = RequestState.OPEN;
        this.passenger = passenger;
        this.note = note;
        this.currentRouteLink = currentRouteLink;
        this.requestTime = LocalDateTime.now();
        this.stopover = stopover;
    }

    public DriveRequest() {

    }

    public RequestState getRequestState() {
        return requestState;
    }

    public User getPassenger() {
        return passenger;
    }

    public String getNote() {
        return note;
    }

    public String getCurrentRouteLink() {
        return currentRouteLink;
    }

    public Stopover getStopover() {
        return stopover;
    }

    public DriveRoute getDriveRoute() {
        return driveRoute;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getRegularDriveSingleDriveDate() {
        return regularDriveSingleDriveDate;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public void setRegularDriveSingleDriveDate(LocalDate regularDriveSingleDriveDate) {
        this.regularDriveSingleDriveDate = regularDriveSingleDriveDate;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return requestTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return requestTime.format(formatter) + " Uhr";
    }

    public String getFormattedSingleDriveDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return regularDriveSingleDriveDate.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DriveRequest) && id.equals(((DriveRequest) o).id);
    }

}

