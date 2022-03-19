package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

/**
 * TD
 */
@Embeddable
public class UserRating {

    @ElementCollection
    private List<Rating> driverRatings;

    @ElementCollection
    private List<Rating> passengerRatings;



    public UserRating(List<Rating> driverRatings, List<Rating> passengerRatings) {
        this.driverRatings = driverRatings;
        this.passengerRatings = passengerRatings;
    }

    public UserRating() {

    }

    public List<Rating> getDriverRatings() {
        return driverRatings;
    }

    public List<Rating> getPassengerRatings() {
        return passengerRatings;
    }

    public void addDriverRating(Rating rating){
        driverRatings.add(rating);
    }

    public void addPassengerRating(Rating rating){
        passengerRatings.add(rating);
    }

    public int getDriverRatingAverage(){
        return  0;
    }

    public int getPassengerRatingAverage(){
        return 0;
    }
}
