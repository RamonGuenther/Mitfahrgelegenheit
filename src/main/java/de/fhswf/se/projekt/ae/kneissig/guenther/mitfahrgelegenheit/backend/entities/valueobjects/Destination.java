package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Destination extends Waypoint
{
    public Destination(Address address, LocalDateTime time)
    {
        super(address, time);
    }

    protected Destination()
    {
        super(null, null);
    }

    public String getFullAddressToString(){
        return address.getStreet() + " " +
                address.getHouseNumber() + ", " +
                address.getPostal() + " " +
                address.getPlace();}
}
