package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Ziel extends Wegpunkt
{
    public Ziel(Adresse adresse, LocalDateTime zeit)
    {
        super(adresse, zeit);
    }

    protected Ziel()
    {
        super(null, null);
    }
}
