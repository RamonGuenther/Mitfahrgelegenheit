package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.FahrerRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FahrerRouteService {

    private final FahrerRouteRepository repository;

    public FahrerRouteService(FahrerRouteRepository repository) {
        this.repository = repository;
    }

    public List<FahrerRoute> findAllFahrerRoutenByBenutzer(Benutzer benutzer) {
        return repository.findAllByBenutzer(benutzer);
    }

    public List<FahrerRoute> findAllByBenutzerAndFahrtenTyp(Benutzer benutzer, DriveType fahrtenTyp) {
        return repository.findAllByBenutzerAndFahrtenTyp(benutzer, fahrtenTyp);
    }

    public List<FahrerRoute> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String startPlace, String benutzerUsername) {
        return repository.findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, benutzerUsername);
    }

    public List<FahrerRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String benutzerUsername) {
        return repository.findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(driveType, destinationPlace, benutzerUsername);
    }

    public List<FahrerRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String startPlace, String benutzerUsername) {
        return repository.findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, destinationPlace, startPlace, benutzerUsername);
    }

}
