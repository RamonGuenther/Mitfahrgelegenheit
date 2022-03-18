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
    protected final Address address;

    @Column(name = "time")
    protected final LocalDateTime time;

    @Override
    public int hashCode() {
        return Objects.hash(address, time);
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

    public Address getAdresse() {
        return address;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Waypoint)
            return getAdresse().equals(((Waypoint) other).getAdresse())
                    && getTime().equals(((Waypoint) other).getTime());
        else
            return false;
    }
}


