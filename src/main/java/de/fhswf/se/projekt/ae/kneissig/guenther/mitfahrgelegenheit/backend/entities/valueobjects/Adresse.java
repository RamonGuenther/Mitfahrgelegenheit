package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Adresse {
    private final String plz;
    private final String ort;
    private final String strasse;
    private final String hausnummer;


    public Adresse(String plz, String ort, String strasse, String hausnummer) {
        this.plz = plz;
        this.ort = ort;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
    }

    public Adresse() {
        this.plz = null;
        this.ort = null;
        this.strasse = null;
        this.hausnummer = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plz, ort, strasse, hausnummer);
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return Objects.equals(plz, adresse.plz)
                && Objects.equals(ort, adresse.ort)
                && Objects.equals(strasse, adresse.strasse)
                && Objects.equals(hausnummer, adresse.hausnummer);
    }
}
