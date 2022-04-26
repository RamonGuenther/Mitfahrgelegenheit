package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidMailException wird geworfen, wenn die eingebende Mail
 * nicht dem FH Format entspricht.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidMailException extends Exception {
    public InvalidMailException(String message) {
        super(message);
    }
}
