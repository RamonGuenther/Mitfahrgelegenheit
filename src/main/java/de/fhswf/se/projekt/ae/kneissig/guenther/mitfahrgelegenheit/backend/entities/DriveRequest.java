package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;

public class DriveRequest {

    private Long id;
    private DriveRoute driveRoute;
    private RequestState requestState;
    private User passenger;
    private String note;

    public DriveRequest(DriveRoute driveRoute, RequestState requestState, User passenger, String note) {
        this.driveRoute = driveRoute;
        this.requestState = requestState;
        this.passenger = passenger;
        this.note = note;
    }

    public DriveRoute getRoute() {
        return driveRoute;
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

    public void setRoute(DriveRoute driveRoute) {
        this.driveRoute = driveRoute;
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
