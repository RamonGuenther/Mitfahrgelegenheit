package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Ziel;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class FahrerRoute {
    @Id
    private Integer id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zeit", column = @Column(name = "start_zeit")),
            @AttributeOverride(name = "adresse.plz", column = @Column(name = "start_plz")),
            @AttributeOverride(name = "adresse.strasse", column = @Column(name = "start_strasse")),
            @AttributeOverride(name = "adresse.ort", column = @Column(name = "start_ort")),
            @AttributeOverride(name = "adresse.hausnummer", column = @Column(name = "start_hausnummer"))
    })
    private Start start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zeit", column = @Column(name = "ziel_zeit")),
            @AttributeOverride(name = "adresse.plz", column = @Column(name = "ziel_plz")),
            @AttributeOverride(name = "adresse.strasse", column = @Column(name = "ziel_strasse")),
            @AttributeOverride(name = "adresse.ort", column = @Column(name = "ziel_ort")),
            @AttributeOverride(name = "adresse.hausnummer", column = @Column(name = "ziel_hausnummer"))
    })
    private Ziel ziel;

    private Integer sitzplaetze;

    @ManyToOne(cascade = CascadeType.ALL)
    private Benutzer benutzer;

    private LocalDateTime erstellungsDatum;

    private DriveType fahrtenTyp;


    public FahrerRoute(Start start, Ziel ziel, Integer sitzplaetze, Benutzer benutzer, LocalDateTime erstellungsDatum, DriveType fahrtenTyp) {
        this.start = start;
        this.ziel = ziel;
        this.sitzplaetze = sitzplaetze;
        this.benutzer = benutzer;
        this.erstellungsDatum = erstellungsDatum;
        this.fahrtenTyp = fahrtenTyp;
        id=hashCode();
    }

    public FahrerRoute(
            Integer id,
            Start start,
            Ziel ziel,
            Integer sitzplaetze,
            Benutzer benutzer,
            LocalDateTime erstellungsDatum,
            DriveType fahrtenTyp
    ) {
        this.id = id;
        this.start = start;
        this.ziel = ziel;
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


    @Override
    public int hashCode() {
        return Objects.hash(start, ziel, sitzplaetze, benutzer.getId());
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

    public Benutzer getBenutzer() {
        return benutzer;
    }
}
