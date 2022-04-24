package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
    private String postal;
    private String place;
    private String street;
    private String houseNumber;

    public Address(String postal, String place, String street, String houseNumber) {
        this.postal = postal;
        this.place = place;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Address() {
        this.postal = null;
        this.place = null;
        this.street = null;
        this.houseNumber = null;
    }

    public String getPostal() {
        return postal;
    }

    public String getPlace() {
        return place;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString(){
        return street + " " + houseNumber + ", " + postal +" " + place;
    }



    @Override
    public int hashCode() {
        return Objects.hash(postal, place, street, houseNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(postal, address.postal)
                && Objects.equals(place, address.place)
                && Objects.equals(street, address.street)
                && Objects.equals(houseNumber, address.houseNumber);
    }
}
