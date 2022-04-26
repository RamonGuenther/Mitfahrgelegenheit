package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions;

/**
 * Die Klasse InvalidRegularDrivePeriod wird geworfen, wenn es sich
 * beim Erstellen einer regulären Fahrt zeittechnisch um eine
 * Einzelfahrt handelt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class InvalidRegularDrivePeriod extends Exception{
    public InvalidRegularDrivePeriod(String message) {
        super(message);
    }
}
