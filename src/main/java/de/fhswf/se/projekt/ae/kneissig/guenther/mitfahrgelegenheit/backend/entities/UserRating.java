package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * TD
 */
@Embeddable
public class UserRating {

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Rating> driverRatings;

    @LazyCollection(LazyCollectionOption.FALSE) //TODO: Was is das ????
    @ElementCollection
    private List<Rating> passengerRatings;

    public UserRating(List<Rating> driverRatings, List<Rating> passengerRatings) {
        this.driverRatings = driverRatings;
        this.passengerRatings = passengerRatings;
    }

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

    public Integer getAverageDriverRating(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getPunctuality() + r.getReliability();
        }

        return (int) Math.round( result / (driverRatings.size() *2));
    }

    public Integer getAveragePassengerRating(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getPunctuality() + r.getReliability();
        }

        return (int) Math.round( result / (passengerRatings.size() *2));
    }

    public Integer getAverageDriverRatingPunctuality(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getPunctuality();
        }

        return (int) Math.round( result / (driverRatings.size()));
    }

    public Integer getAverageDriverRatingReliability(){
        double result = 0.0;
        for(Rating r: driverRatings){
            result += r.getReliability();
        }

        return (int) Math.round( result / (driverRatings.size()));
    }

    public Integer getAveragePassengerRatingPunctuality(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getPunctuality();
        }

        return (int) Math.round( result / (passengerRatings.size()));
    }

    public Integer getAveragePassengerRatingReliability(){
        double result = 0.0;
        for(Rating r: passengerRatings){
            result += r.getReliability();
        }

        return (int) Math.round( result / (passengerRatings.size()));
    }
}
