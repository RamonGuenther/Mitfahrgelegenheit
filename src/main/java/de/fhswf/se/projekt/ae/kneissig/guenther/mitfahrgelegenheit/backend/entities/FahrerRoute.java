package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Ziel;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FahrerRoute {
    @Id
    private final Integer id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zeit", column = @Column(name = "start_zeit")),
            @AttributeOverride(name = "adresse.plz", column = @Column(name = "start_plz")),
            @AttributeOverride(name = "adresse.strasse", column = @Column(name = "start_strasse")),
            @AttributeOverride(name = "adresse.ort", column = @Column(name = "start_ort")),
            @AttributeOverride(name = "adresse.hausnummer", column = @Column(name = "start_hausnummer"))
    })
    private final Start start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zeit", column = @Column(name = "ziel_zeit")),
            @AttributeOverride(name = "adresse.plz", column = @Column(name = "ziel_plz")),
            @AttributeOverride(name = "adresse.strasse", column = @Column(name = "ziel_strasse")),
            @AttributeOverride(name = "adresse.ort", column = @Column(name = "ziel_ort")),
            @AttributeOverride(name = "adresse.hausnummer", column = @Column(name = "ziel_hausnummer"))
    })
    private final Ziel ziel;

    private final String cronstring;

    private final Integer sitzplaetze;

    @ManyToOne(cascade = CascadeType.ALL)
    private final Benutzer benutzer;

    private final LocalDateTime erstellungsDatum;

    private final DriveType fahrtenTyp;


    public FahrerRoute(Integer id, Start start, Ziel ziel, String cronstring, Integer sitzplaetze, Benutzer benutzer, LocalDateTime erstellungsDatum, DriveType fahrtenTyp) {
        this.id = id;
        this.start = start;
        this.ziel = ziel;
        this.cronstring = cronstring;
        this.sitzplaetze = sitzplaetze;
        this.benutzer = benutzer;
        this.erstellungsDatum = erstellungsDatum;
        this.fahrtenTyp = fahrtenTyp;
    }

    @PersistenceConstructor
    public FahrerRoute() {
        this.id = null;
        this.start = null;
        this.ziel = null;
        this.cronstring = null;
        this.sitzplaetze = null;
        this.benutzer = null;
        this.erstellungsDatum = null;
        this.fahrtenTyp = null;
    }

    public Integer getId() {
        return id;
    }

    public Start getStart() {
        return start;
    }

    public Ziel getZiel() {
        return ziel;
    }

    public String getCronstring() {
        return cronstring;
    }

    public Integer getSitzplaetze() {
        return sitzplaetze;
    }

    public LocalDateTime getErstellungsDatum() {
        return erstellungsDatum;
    }

    public DriveType getFahrtenTyp() {
        return fahrtenTyp;
    }
}
