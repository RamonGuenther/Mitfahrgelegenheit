package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

/**
 * Mit der Enumeration DriveType, kann für ein Fahrtangebot festgelegt werden,
 * ob es sich um eine Hin- oder Rückfahrt zu einem FH-Standort handelt.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public enum DriveType {
    OUTWARD_TRIP("Hinfahrt"),
    RETURN_TRIP("Rückfahrt");

    public final String label;

    DriveType(String label) {
        this.label = label;
    }
}

