package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum RequestState {
    OPEN("Offen"),
    ACCEPTED("Akzeptiert"),
    REJECTED("Abgelehnt");

    public final String label;

    RequestState(String label) {
        this.label = label;
    }
}
