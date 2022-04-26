package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

/**
 * Die Enumeration FhLocation enthält die Standortde der Fachhochschule Südwestfalen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
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
