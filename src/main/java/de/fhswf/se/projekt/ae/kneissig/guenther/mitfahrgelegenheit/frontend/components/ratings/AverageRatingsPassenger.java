package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse AverageRatingsPassenger erstellt eine Ansicht
 * für die Mitfahrerbewertungen.
 *
 * @author Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/average-ratings.css")
public class AverageRatingsPassenger extends VerticalLayout {

    private StarsRating punctuality;
    private StarsRating reliability;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Mitfahrerbewertungen und die entsprechende Beschriftung
     * zuständig.
     */
    public AverageRatingsPassenger(){
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
}
