package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Enumeration DayOfWeek dient als Wertebereich für die möglichen Wochentage,
 * für die regelmäßige Fahrten angeboten werden können.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
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

    public static DayOfWeek getDayOfWeek(String label){
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
        }
        return MONDAY;
    }

    public static List<String> getDayOfWeekList() {
        List<String> result = new ArrayList<>();

        for(DayOfWeek dayOfWeek : DayOfWeek.values()){
            result.add(dayOfWeek.label);
        }
        return result;
    }
}
