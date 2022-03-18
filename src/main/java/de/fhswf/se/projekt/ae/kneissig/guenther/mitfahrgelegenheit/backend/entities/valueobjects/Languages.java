package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects;

import org.springframework.data.annotation.PersistenceConstructor;

import com.google.common.collect.Streams;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Languages {

    private final String mainLanguage;

    @ElementCollection
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

    public Set<String> getAllLanguages() {
        return Streams.concat(Stream.of(mainLanguage), languages.stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Languages languages1 = (Languages) o;
        return Objects.equals(mainLanguage, languages1.mainLanguage) && Objects.equals(languages, languages1.languages);
    }
}
