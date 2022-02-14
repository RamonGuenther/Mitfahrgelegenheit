package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Start extends Wegpunkt {

    public Start(Adresse adresse, LocalDateTime zeit) {
        super(adresse, zeit);
    }

    protected Start() {
        super();
    }
}
