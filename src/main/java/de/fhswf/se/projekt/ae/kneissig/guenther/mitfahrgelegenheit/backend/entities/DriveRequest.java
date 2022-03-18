package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;

public class DriveRequest {

    private Long id;
    private Route route;
    private RequestState requestState;
    private User passenger;
    private String note;

    public DriveRequest(Route route, RequestState requestState, User passenger, String note) {
        this.route = route;
        this.requestState = requestState;
        this.passenger = passenger;
        this.note = note;
    }

    public Route getRoute() {
        return route;
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

    public void setRoute(Route route) {
        this.route = route;
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
