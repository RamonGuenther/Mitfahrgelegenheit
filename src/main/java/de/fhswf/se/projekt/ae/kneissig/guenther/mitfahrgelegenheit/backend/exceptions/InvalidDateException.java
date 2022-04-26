package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidateException wird geworfen, wenn jemand das heutige Datum
 * oder ein vergangenes Datum angibt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidDateException extends Exception{

    public InvalidDateException(String message) {
        super(message);
    }
}
