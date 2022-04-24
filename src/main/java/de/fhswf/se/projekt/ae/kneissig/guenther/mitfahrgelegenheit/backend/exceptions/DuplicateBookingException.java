package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

public class DuplicateBookingException extends Exception{
    public DuplicateBookingException(String message) {
        super(message);
    }
}
