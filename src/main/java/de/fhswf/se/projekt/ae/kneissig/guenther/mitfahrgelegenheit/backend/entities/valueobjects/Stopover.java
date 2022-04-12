package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Stopover extends Waypoint {
    public Stopover(Address address) {
        super(address);
    }

    protected Stopover() {
        super(null);
    }
}
