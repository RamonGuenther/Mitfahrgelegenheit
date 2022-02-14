package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@MappedSuperclass
public abstract class Wegpunkt {

    @Embedded
    protected final Adresse adresse;

    @Column(name = "zeit")
    protected final LocalDateTime zeit;

    @Override
    public int hashCode() {
        return Objects.hash(adresse, zeit);
    }

    public Wegpunkt(Adresse adresse, LocalDateTime zeit) {
        this.adresse = adresse;
        this.zeit = zeit;
    }

    @PersistenceConstructor
    protected Wegpunkt() {
        this.adresse = null;
        this.zeit = null;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public LocalDateTime getZeit() {
        return zeit;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Wegpunkt)
            return getAdresse().equals(((Wegpunkt) other).getAdresse())
                    && getZeit().equals(((Wegpunkt) other).getZeit());
        else
            return false;
    }
}


