package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

/**
 * Mit der Enumeration kann der Status einer Anfrage festgelegt werden.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public enum RequestState {
    OPEN("Offen"),
    ACCEPTED("Akzeptiert"),
    REJECTED("Abgelehnt");

    public final String label;

    RequestState(String label) {
        this.label = label;
    }
}
