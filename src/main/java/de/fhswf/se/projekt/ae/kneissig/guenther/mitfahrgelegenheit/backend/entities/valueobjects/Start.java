package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;

/**
 * Die Klasse Start dient dazu, eine Adresse als Startadresse
 * einer Fahrt zu identifizieren.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Embeddable
public class Start extends Waypoint {

    public Start(Address address) {
        super(address);
    }

    protected Start() {
        super();
    }


}
