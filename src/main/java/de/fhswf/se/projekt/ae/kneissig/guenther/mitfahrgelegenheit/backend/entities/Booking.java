package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import java.time.LocalDateTime;

public class Booking {
    private final Long id;

    private final Route route;

    private final User mitfahrer;

    private final Boolean bestaetigt;

    private final LocalDateTime buchungsDatum;

    public Booking(Long id, Route route, User mitfahrer, Boolean bestaetigt, LocalDateTime buchungsDatum)
    {
        this.id = id;
        this.route = route;
        this.mitfahrer = mitfahrer;
        this.bestaetigt = bestaetigt;
        this.buchungsDatum = buchungsDatum;
    }

    public Long getId()
    {
        return id;
    }

    public Route getRoute()
    {
        return route;
    }

    public User getMitfahrer()
    {
        return mitfahrer;
    }

    public Boolean getBestaetigt()
    {
        return bestaetigt;
    }

    public LocalDateTime getBuchungsDatum()
    {
        return buchungsDatum;
    }
}
