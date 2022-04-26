package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;

/**
 * Die Klasse AverageRatingsDrive erstellt eine Ansicht für
 * die Fahrerbewertungen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/average-ratings.css")
public class AverageRatings extends VerticalLayout {

    private StarsRating punctuality;
    private StarsRating reliability;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Fahrerbewertungen und die entsprechende Beschriftung
     * zuständig.
     */
    public AverageRatings(){
        FormLayout ratings = new FormLayout();
        ratings.setId("ratingsForm");

        punctuality = new StarsRating(0);
        punctuality.setManual(false);
        punctuality.setId("ratingPunctuality");
        ratings.addFormItem(punctuality, "Pünktlichkeit");

        reliability = new StarsRating(0);
        reliability.setManual(false);
        reliability.setId("ratingReliability");
        ratings.addFormItem(reliability, "Zuverlässigkeit");

        add(ratings);
    }

    public StarsRating getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(StarsRating punctuality) {
        this.punctuality = punctuality;
    }

    public StarsRating getReliability() {
        return reliability;
    }

    public void setReliability(StarsRating reliability) {
        this.reliability = reliability;
    }
}
