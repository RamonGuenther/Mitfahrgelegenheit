package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Embeddable
@MappedSuperclass
public abstract class Waypoint {

    @Embedded
    protected Address address;


    public Waypoint(Address address) {
        this.address = address;
    }

    @PersistenceConstructor
    protected Waypoint() {
        this.address = null;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFullAddressToString(){
        return address.getStreet() + " " +
                address.getHouseNumber() + ", " +
                address.getPostal() + " " +
                address.getPlace();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Waypoint)
            return getAddress().equals(((Waypoint) other).getAddress());
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}


