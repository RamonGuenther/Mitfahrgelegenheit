package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    private final Long id;
    private final String username;
    @Embedded
    private final Address address;
    @Embedded
    private final Languages languages;
    private final LocalDateTime lastLogin;

    @Embedded
    private final UserRating userRating;

    public User(
            Long uid,
            String username,
            Address address,
            Languages languages,
            LocalDateTime lastLogin,
            UserRating userRating
    ) {
        this.id = uid;
        this.username = username;
        this.address = address;
        this.languages = languages;
        this.lastLogin = lastLogin;
        this.userRating = userRating;
    }

    public User() {
        this.id = null;
        this.username = null;
        this.address = null;
        this.languages = null;
        this.lastLogin = null;
        this.userRating = null;
    }

    public Long getId() {
        return id;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    @Transient
    public Long getUid() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Address getAddress() {
        return address;
    }

    public Languages getSprachen() {
        return languages;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

}
