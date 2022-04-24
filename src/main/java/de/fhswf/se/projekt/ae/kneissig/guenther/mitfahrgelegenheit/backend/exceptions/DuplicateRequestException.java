package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

public class DuplicateRequestException extends Exception{
    public DuplicateRequestException(String message) {
        super(message);
    }
}
