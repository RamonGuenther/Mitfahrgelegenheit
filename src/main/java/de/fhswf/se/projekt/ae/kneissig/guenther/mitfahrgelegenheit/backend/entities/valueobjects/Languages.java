package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Die Klasse Languages speichert die Hauptsprache eines Benutzers,
 * sowie ggf. weitere Sprachen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Embeddable
public class Languages {

    private final String mainLanguage;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<String> languages;

    public Languages(String mainLanguage, Set<String> languages) {
        this.mainLanguage = mainLanguage;
        this.languages = languages;
    }

    public Languages(String mainLanguage) {
        this.mainLanguage = mainLanguage;
        this.languages = Collections.emptySet();
    }

    @PersistenceConstructor
    public Languages() {
        this.mainLanguage = null;
        this.languages = null;
    }

    public String getMainLanguage() {
        return mainLanguage;
    }

    public Set<String> getAllLanguages(){
        return languages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Languages languages1 = (Languages) o;
        return Objects.equals(mainLanguage, languages1.mainLanguage) && Objects.equals(languages, languages1.languages);
    }
}
