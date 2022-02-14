package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.FahrerRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Andere Namen für die Methoden ausdenken und Validieren in Searchdrive ob es überhaupt funktioniert also findRouten
 */
@Service
public class FahrerRouteService {

    private final FahrerRouteRepository repository;

    public FahrerRouteService(FahrerRouteRepository repository) {
        this.repository = repository;
    }

    public void save(FahrerRoute fahrerRoute){
        repository.save(fahrerRoute);
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

    public List<FahrerRoute> findRouten(Benutzer benutzer, DriveType driveType, String destinationPlace, String startPlace) {
        List<FahrerRoute> routen = findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, destinationPlace, benutzer.getUsername());

        return routen.size() > 0 ? routen : switch (driveType) {
            case HINFAHRT -> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, destinationPlace, benutzer.getUsername());
            case RUECKFAHRT -> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, benutzer.getUsername());
        };
    }

}
