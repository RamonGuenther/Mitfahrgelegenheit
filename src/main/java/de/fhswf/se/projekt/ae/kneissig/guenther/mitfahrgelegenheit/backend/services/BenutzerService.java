package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.BenutzerRepository;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.FahrerRouteRepository;
import org.springframework.stereotype.Service;

@Service
public class BenutzerService {

    private final BenutzerRepository repository;

    public BenutzerService(BenutzerRepository repository){
        this.repository = repository;
    }

    public void save(Benutzer benutzer){
        repository.save(benutzer);
    }

    public void findBenutzerById(Long id){
        repository.getById(id);
    }

}
