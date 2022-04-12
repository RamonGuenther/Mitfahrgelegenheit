package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class DriveRequest {

    @Id
    private Integer id;

    @ManyToOne
    private User passenger;

    @ManyToOne
    private DriveRoute driveRoute;

    @NaturalId
    private String businessKey;

    private RequestState requestState;

    private String note;
    private String currentRouteLink;
    private LocalDateTime requestTime;

    @Embedded
    private Stopover stopover;

    public DriveRequest(DriveRoute driveRoute, RequestState requestState, User passenger, String note, String currentRouteLink, LocalDateTime requestTime, Stopover stopover) {
        this.driveRoute = driveRoute;
        this.requestState = requestState;
        this.passenger = passenger;
        this.note = note;
        this.currentRouteLink = currentRouteLink;
        this.requestTime = requestTime;
        this.stopover = stopover;
        businessKey = passenger.getUsername();
        id= hashCode();
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

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
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


    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return requestTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return requestTime.format(formatter) + " Uhr";
    }

    public String getBusinessKey() {
        return businessKey;
    }


    @Override
    public boolean equals(Object o) {
        return (o instanceof DriveRequest) && businessKey.equals(((DriveRequest) o).businessKey);
    }


    @Override
    public int hashCode() {
        return Objects.hash(passenger, driveRoute);
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DriveRequest that)) return false;
//        if (!getId().equals(that.getId())) return false;
//        if (!getPassenger().equals(that.getPassenger())) return false;
//        return Objects.equals(getBusinessKey(),that.getBusinessKey());
//    }
//
//    @Override
//    public int hashCode() {
//        System.out.println(businessKey);
//        return Objects.hash(businessKey);
//    }
}

