package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum DayOfWeek {
    NON_REGULAR_DRIVE("Keine regelmäßige Fahrt"),
    MONDAY("Mo"),
    TUESDAY("DI"),
    WEDNESDAY("MI"),
    THURSDAY("DO"),
    FRIDAY("FR"),
    SATURDAY("SA");

    public final String label;

    DayOfWeek(String label) {
        this.label = label;
    }
}
