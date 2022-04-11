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

    @Column(name = "time")
    protected LocalDateTime time;

    public Waypoint(Address address, LocalDateTime time) {
        this.address = address;
        this.time = time;
    }

    @PersistenceConstructor
    protected Waypoint() {
        this.address = null;
        this.time = null;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }


    public String getFullAddressToString(){
        return address.getStreet() + " " +
                address.getHouseNumber() + ", " +
                address.getPostal() + " " +
                address.getPlace();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return time.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter) + " Uhr";
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


