package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;

/**
 * Die Klasse Destination dient dazu, eine Adresse als Zieladresse
 * einer Fahrt zu identifizieren.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
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
