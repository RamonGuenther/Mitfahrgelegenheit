package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum DriveType {
    OUTWARD_TRIP("Hinfahrt"),
    RETURN_TRIP("Rückfahrt");

    public final String label;

    DriveType(String label) {
        this.label = label;
    }
}

