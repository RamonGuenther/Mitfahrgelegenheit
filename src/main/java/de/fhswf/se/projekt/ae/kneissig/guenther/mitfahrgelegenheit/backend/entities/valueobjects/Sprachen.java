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
public class Sprachen {

    private final String sprache;

    @ElementCollection
    private final Set<String> sprachen;

    public Sprachen(String sprache, Set<String> sprachen) {
        this.sprache = sprache;
        this.sprachen = sprachen;
    }

    public Sprachen(String sprache) {
        this.sprache = sprache;
        this.sprachen = Collections.emptySet();
    }

    @PersistenceConstructor
    Sprachen() {
        this.sprache = null;
        this.sprachen = null;
    }


    public String getPrimaereSprache() {
        return sprache;
    }

    public Set<String> getAll() {
        return Streams.concat(Stream.of(sprache), sprachen.stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprachen sprachen1 = (Sprachen) o;
        return Objects.equals(sprache, sprachen1.sprache) && Objects.equals(sprachen, sprachen1.sprachen);
    }
}
