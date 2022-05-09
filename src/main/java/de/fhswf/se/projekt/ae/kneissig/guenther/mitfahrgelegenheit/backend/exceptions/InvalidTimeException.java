package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidTimeException wird geworfen, wenn die Uhrzeit
 * der jetzigen entspricht oder in der Vergangenheit liegt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidTimeException extends Exception {

    public InvalidTimeException(String message) {
        super(message);
    }
}
