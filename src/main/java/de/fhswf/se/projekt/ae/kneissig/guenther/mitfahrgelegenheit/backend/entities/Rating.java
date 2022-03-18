package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class Rating {
    private LocalDate date;
    private int punctuality;
    private int reliability;

    public Rating(LocalDate date, int punctuality, int reliability) {
        this.date = date;
        this.punctuality = punctuality;
        this.reliability = reliability;
    }

    public Rating() {

    }

    public LocalDate getDate() {
        return date;
    }

    public int getPunctuality() {
        return punctuality;
    }

    public int getReliability() {
        return reliability;
    }
}
