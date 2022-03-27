package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class DriveRequest {

    @OneToOne
    private User passenger;

    private RequestState requestState;

    private String note;
    private String currentRouteLink;
    private LocalDateTime requestTime;

    @Embedded
    private Stopover stopover;

    public DriveRequest(RequestState requestState, User passenger, String note, String currentRouteLink, LocalDateTime requestTime, Stopover stopover) {
        this.requestState = requestState;
        this.passenger = passenger;
        this.note = note;
        this.currentRouteLink = currentRouteLink;
        this.requestTime = requestTime;
        this.stopover = stopover;
    }

    public DriveRequest() {
        this.requestState = null;
        this.passenger = null;
        this.note = null;
        this.currentRouteLink = null;
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

    @Override
    public boolean equals (Object o) {
        return o instanceof DriveRequest && passenger.getId().equals (((DriveRequest) o).passenger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassenger().getId().hashCode());
    }
}
