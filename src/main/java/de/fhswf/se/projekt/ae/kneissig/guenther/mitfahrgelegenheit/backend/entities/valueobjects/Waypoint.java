package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@MappedSuperclass
public abstract class Waypoint {

    @Embedded
    protected Address address;

    @Column(name = "time")
    protected LocalDateTime time;

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

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

    @Override
    public boolean equals(Object other) {
        if (other instanceof Waypoint)
            return getAddress().equals(((Waypoint) other).getAddress());
        else
            return false;
    }
}


