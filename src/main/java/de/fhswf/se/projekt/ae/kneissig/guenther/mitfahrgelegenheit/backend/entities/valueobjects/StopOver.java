package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import java.time.LocalDateTime;

public class StopOver extends Waypoint {
    public StopOver(Address address, LocalDateTime time) {
        super(address, time);
    }

    protected StopOver() {
        super();
    }
}
