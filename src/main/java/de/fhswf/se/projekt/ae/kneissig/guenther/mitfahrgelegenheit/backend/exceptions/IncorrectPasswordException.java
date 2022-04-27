package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse IncorrectPasswordException wird geworfen, wenn ein Nutzer beim Passwort bearbeiten
 * das falsche aktuelle Passwort eingibt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
