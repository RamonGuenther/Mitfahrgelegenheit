package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
public class Rating {
    private LocalDate date;
    private int punctuality;
    private int reliability;

    public Rating(int punctuality, int reliability) {
        this.date = LocalDate.now();
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

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
}
