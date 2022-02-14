package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse AverageRatingsDrive erstellt eine Ansicht für
 * die Fahrerbewertungen.
 *
 * @author Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/average-ratings.css")
public class AverageRatingsDriver extends VerticalLayout {

    private StarsRating punctuality;
    private StarsRating reliability;
    private StarsRating drivingStyle;
    private StarsRating cleanliness;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Fahrerbewertungen und die entsprechende Beschriftung
     * zuständig.
     */
    public AverageRatingsDriver(){
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

        drivingStyle = new StarsRating(0);
        drivingStyle.setId("ratingDrivingstyle");
        drivingStyle.setManual(false);
        ratings.addFormItem(drivingStyle, "Fahrstil");

        cleanliness = new StarsRating(0);
        cleanliness.setId("ratingCleanliness");
        cleanliness.setManual(false);
        ratings.addFormItem(cleanliness, "Sauberkeit");

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

    public StarsRating getDrivingStyle() {
        return drivingStyle;
    }

    public void setDrivingStyle(StarsRating drivingStyle) {
        this.drivingStyle = drivingStyle;
    }

    public StarsRating getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(StarsRating cleanliness) {
        this.cleanliness = cleanliness;
    }
}
