package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse DuplicateBookingException wird geworfen, wenn jemand mehr als eine
 * Fahrtanfrage zu einem Fahrtangebot stellt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class DuplicateBookingException extends Exception{
    public DuplicateBookingException(String message) {
        super(message);
    }
}
