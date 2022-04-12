package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Destination extends Waypoint
{
    public Destination(Address address)
    {
        super(address);
    }

    protected Destination()
    {
        super(null);
    }

}
