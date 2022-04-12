package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Start extends Waypoint {

    public Start(Address address) {
        super(address);
    }

    protected Start() {
        super();
    }


}
