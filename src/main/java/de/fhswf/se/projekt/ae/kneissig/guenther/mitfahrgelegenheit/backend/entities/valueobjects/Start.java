package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Start extends Waypoint {

    public Start(Address address, LocalDateTime time) {
        super(address, time);
    }

    protected Start() {
        super();
    }

    public String getFullAddressToString(){
        return address.getStreet() + " " +
                address.getHouseNumber() + ", " +
                address.getPostal() + " " +
                address.getPlace();}
}
