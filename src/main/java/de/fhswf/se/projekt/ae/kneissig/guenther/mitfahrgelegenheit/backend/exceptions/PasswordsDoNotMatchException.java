package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

public class PasswordsDoNotMatchException extends Exception{
    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
