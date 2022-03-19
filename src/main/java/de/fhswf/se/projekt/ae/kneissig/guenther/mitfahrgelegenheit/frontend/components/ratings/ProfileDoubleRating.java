package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse ProfileDoubleRating erstellt eine Ratinganzeige für
 * das Profil.
 * Die Ratinganzeige besteht aus zwei Ratings, eine für die Bewertung
 * als Fahrer und eine für die Bewertung als Mitfahrer.
 *
 * @author Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/profile-double-ratings.css")
public class ProfileDoubleRating extends VerticalLayout {

    private StarsRating driverRating;
    private StarsRating passengerRating;

    /**
     * Im Konstruktor werden die Ratingelemente erzeugt.
     */
    public ProfileDoubleRating(){

        driverRating = new StarsRating(0);
        driverRating.setId("driverRating");
        driverRating.setManual(false);
        passengerRating = new StarsRating(0);
        passengerRating.setId("passengerRating");
        passengerRating.setManual(false);

        add(driverRating, passengerRating);
    }

    public StarsRating getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(StarsRating driverRating) {
        this.driverRating = driverRating;
    }

    public StarsRating getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(StarsRating passengerRating) {
        this.passengerRating = passengerRating;
    }
}
