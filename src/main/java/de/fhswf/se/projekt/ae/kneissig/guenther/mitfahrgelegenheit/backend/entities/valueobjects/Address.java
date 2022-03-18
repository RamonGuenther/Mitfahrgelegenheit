package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
    private final String postal;
    private final String place;
    private final String street;
    private final String houseNumber;

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
