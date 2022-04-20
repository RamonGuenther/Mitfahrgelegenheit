package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum DayOfWeek {
    MONDAY("Montag"),
    TUESDAY("Dienstag"),
    WEDNESDAY("Mittwoch"),
    THURSDAY("Donnerstag"),
    FRIDAY("Freitag"),
    SATURDAY("Samstag");

    public final String label;

    DayOfWeek(String label) {
        this.label = label;
    }

    public static DayOfWeek getDayOfWeekByShortName(String label){
        switch (label){
            case "Montag" -> {
                return MONDAY;
            }
            case "Dienstag" -> {
                return TUESDAY;
            }
            case "Mittwoch" -> {
                return WEDNESDAY;
            }
            case "Donnerstag" -> {
                return THURSDAY;
            }
            case "Freitag" -> {
                return FRIDAY;
            }
            case "Samstag" -> {
                return SATURDAY;
            }
            default -> {
                return MONDAY;
            }
        }
    }

    public static DayOfWeek getDayOfWeekByLongName(String label){
        switch (label){
            case "Montags" -> {
                return MONDAY;
            }
            case "Dienstags" -> {
                return TUESDAY;
            }
            case "Mittwochs" -> {
                return WEDNESDAY;
            }
            case "Donnerstags" -> {
                return THURSDAY;
            }
            case "Freitags" -> {
                return FRIDAY;
            }
            case "Samstags" -> {
                return SATURDAY;
            }
            default -> {
                return MONDAY;
            }
        }
    }
}
