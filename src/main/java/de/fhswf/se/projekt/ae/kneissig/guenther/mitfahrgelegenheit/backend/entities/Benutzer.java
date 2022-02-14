package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Adresse;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Sprachen;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
public class Benutzer {
    @Id
    private final Long id;
    private final String username;
    @Embedded
    private final Adresse adresse;
    @Embedded
    private final Sprachen sprachen;
    private final LocalDateTime letzterLogin;

    public Benutzer(
            Long uid,
            String username,
            Adresse adresse,
            Sprachen sprachen,
            LocalDateTime letzterLogin
    ) {
        this.id = uid;
        this.username = username;
        this.adresse = adresse;
        this.sprachen = sprachen;
        this.letzterLogin = letzterLogin;
    }

    public Benutzer() {
        this.id = null;
        this.username = null;
        this.adresse = null;
        this.sprachen = null;
        this.letzterLogin = null;
    }

    public Long getId() {
        return id;
    }

    @Transient
    public Long getUid() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public Sprachen getSprachen() {
        return sprachen;
    }

    public LocalDateTime getLetzterLogin() {
        return letzterLogin;
    }
}
