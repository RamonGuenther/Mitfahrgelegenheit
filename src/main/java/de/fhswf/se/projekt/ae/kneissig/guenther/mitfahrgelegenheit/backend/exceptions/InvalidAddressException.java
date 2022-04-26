package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidAddressException wird geworfen, wenn die eingebende
 * Adresse nicht dem Format entspricht.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidAddressException extends Exception{
    public InvalidAddressException(String message) {
        super(message);
    }
}
