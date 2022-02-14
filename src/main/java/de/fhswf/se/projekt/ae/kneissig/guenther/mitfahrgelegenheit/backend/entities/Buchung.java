package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import java.time.LocalDateTime;

public class Buchung  {
    private final Long id;

    private final FahrerRoute route;

    private final Benutzer mitfahrer;

    private final Boolean bestaetigt;

    private final LocalDateTime buchungsDatum;

    public Buchung(Long id, FahrerRoute route, Benutzer mitfahrer, Boolean bestaetigt, LocalDateTime buchungsDatum)
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

    public FahrerRoute getRoute()
    {
        return route;
    }

    public Benutzer getMitfahrer()
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
