package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;

import javax.persistence.*;

@Embeddable
public class DriveRequest {

    @OneToOne
    private User passenger;

    private RequestState requestState;
    private String note;
    private String currentRouteLink;

    public DriveRequest(RequestState requestState, User passenger, String note, String currentRouteLink) {
        this.requestState = requestState;
        this.passenger = passenger;
        this.note = note;
        this.currentRouteLink = currentRouteLink;
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
}
