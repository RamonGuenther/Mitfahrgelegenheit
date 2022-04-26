package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse DuplicateBookingException wird geworfen, wenn ein Mitfahrer mehr als einmal für eine
 * Fahrt akzeptiert wird.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class DuplicateRequestException extends Exception{
    public DuplicateRequestException(String message) {
        super(message);
    }
}
