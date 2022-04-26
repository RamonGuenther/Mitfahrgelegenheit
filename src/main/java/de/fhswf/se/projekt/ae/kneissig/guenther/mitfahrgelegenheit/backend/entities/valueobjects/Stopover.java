package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;

/**
 * Die Klasse Stopover dient dazu, eine Adresse als Zwischenstop
 * einer Fahrt zu identifizieren.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Embeddable
public class Stopover extends Waypoint {
    public Stopover(Address address) {
        super(address);
    }

    protected Stopover() {
        super(null);
    }
}
