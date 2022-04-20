package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum DayOfWeek {
    MONDAY("Mo"),
    TUESDAY("Di"),
    WEDNESDAY("Mi"),
    THURSDAY("Do"),
    FRIDAY("Fr"),
    SATURDAY("Sa");

    public final String label;

    DayOfWeek(String label) {
        this.label = label;
    }

    public static DayOfWeek getDayOfWeek(String label){
        switch (label){
            case "Mo" -> {
                return MONDAY;
            }
            case "Di" -> {
                return TUESDAY;
            }
            case "Mi" -> {
                return WEDNESDAY;
            }
            case "Do" -> {
                return THURSDAY;
            }
            case "Fr" -> {
                return FRIDAY;
            }
            case "Sa" -> {
                return SATURDAY;
            }
            default -> {
                return MONDAY;
            }
        }
    }
}
