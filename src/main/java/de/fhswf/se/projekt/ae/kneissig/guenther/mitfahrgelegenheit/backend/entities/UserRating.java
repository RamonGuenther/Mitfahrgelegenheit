package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse UserRating Speichert alle Fahrer- und Mitfahrerbewertungen zu
 * einem Benutzer.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Embeddable
public class UserRating {

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Rating> driverRatings;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Rating> passengerRatings;

    public UserRating() {
       driverRatings = new ArrayList<>();
       passengerRatings = new ArrayList<>();
    }

    public List<Rating> getDriverRatings() {
        return driverRatings;
    }

    public List<Rating> getPassengerRatings() {
        return passengerRatings;
    }

    public int getDriverRatingAverage(){
        return  0;
    }

    public int getPassengerRatingAverage(){
        return 0;
    }

    public void addDriverRating(Rating rating){
        driverRatings.add(rating);
    }

    public void addPassengerRating(Rating rating){
        passengerRatings.add(rating);
    }

    /**
     * Die Methode getAverageDriverRating berechnet den Gesamtdurchschnitt
     * der Fahrerbewertungen eines Benutzers.
     *
     * @return      Gesamtdurchschnitt Fahrerbewertungen
     */
    public Integer getAverageDriverRating(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getPunctuality() + r.getReliability();
        }
        return (int) Math.round( result / (driverRatings.size() *2));
    }

    /**
     * Die Methode getAveragePassengerRating berechnet den Gesamtdurchschnitt
     * der Mitfahrerbewertungen eines Benutzers.
     *
     * @return      Gesamtdurchschnitt Mitfahrerbewertungen
     */
    public Integer getAveragePassengerRating(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getPunctuality() + r.getReliability();
        }
        return (int) Math.round( result / (passengerRatings.size() *2));
    }

    /**
     * Die Methode getAverageDriverRatingPunctuality berechnet den Durchschnitt der
     * Pünktlichkeit der Fahrerbewertungen eines Benutzers.
     *
     * @return  Durchschnitt der Pünktlichkeit bei den Fahrerbewertungen
     */
    public Integer getAverageDriverRatingPunctuality(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getPunctuality();
        }
        return (int) Math.round( result / (driverRatings.size()));
    }

    /**
     * Die Methode getAverageDriverRatingReliability berechnet den Durchschnitt der
     * Zuverlässigkeit der Fahrerbewertungen eines Benutzers.
     *
     * @return  Durchschnitt der Zuverlässigkeit bei den Fahrerbewertungen
     */
    public Integer getAverageDriverRatingReliability(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getReliability();
        }
        return (int) Math.round( result / (driverRatings.size()));
    }

    /**
     * Die Methode getAveragePassengerRatingPunctuality berechnet den Durchschnitt der
     * Pünktlichkeit der Mitfahrerbewertungen eines Benutzers.
     *
     * @return  Durchschnitt der Pünktlichkeit bei den Mitfahrerbewertungen
     */
    public Integer getAveragePassengerRatingPunctuality(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getPunctuality();
        }
        return (int) Math.round( result / (passengerRatings.size()));
    }

    /**
     * Die Methode getAveragePassengerRatingReliability berechnet den Durchschnitt der
     * Zuverlässigkeit der Mitfahrerbewertungen eines Benutzers.
     *
     * @return  Durchschnitt der Zuverlässigkeit bei den Mitfahrerbewertungen
     */
    public Integer getAveragePassengerRatingReliability(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getReliability();
        }
        return (int) Math.round( result / (passengerRatings.size()));
    }
}
