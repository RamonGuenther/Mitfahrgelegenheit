package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Stopover extends Waypoint {
    public Stopover(Address address, LocalDateTime time) {
        super(address, time);
    }

    protected Stopover() {
        super(null,null);
    }
}
