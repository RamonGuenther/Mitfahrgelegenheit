package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum FhLocation {

    ISERLOHN ("Iserlohn"),
    LUEDENSCHEID ("Lüdenscheid"),
    SOEST("Soest"),
    HAGEN("Hagen"),
    MESCHEDE("Meschede");

    public final String label;

    FhLocation(String label) {
        this.label = label;
    }
}
