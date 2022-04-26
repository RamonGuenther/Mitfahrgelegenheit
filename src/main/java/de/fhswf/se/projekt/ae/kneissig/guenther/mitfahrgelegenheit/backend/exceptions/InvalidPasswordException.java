package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidPasswordException wird geworfen, wenn das Passwort einem
 * bestimmten Format nicht entspricht.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
