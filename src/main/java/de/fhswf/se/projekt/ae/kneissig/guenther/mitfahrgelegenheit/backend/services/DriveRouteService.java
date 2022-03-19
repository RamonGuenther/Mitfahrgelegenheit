package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Andere Namen für die Methoden ausdenken und Validieren in Searchdrive ob es überhaupt funktioniert also findRouten
 */
@Service
public class DriveRouteService {

    private final DriveRouteRepository repository;

    public DriveRouteService(DriveRouteRepository repository) {
        this.repository = repository;
    }

    public void save(DriveRoute driveRoute){
        repository.save(driveRoute);
    }

    public List<DriveRoute> findAllFahrerRoutenByBenutzer(User user) {
        return repository.findAllByDriver(user);
    }

    public List<DriveRoute> findAllByBenutzerAndFahrtenTyp(User user, DriveType fahrtenTyp) {
        return repository.findAllByDriverAndDriveType(user, fahrtenTyp);
    }

    public List<DriveRoute> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String startPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, benutzerUsername);
    }

    public List<DriveRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, benutzerUsername);
    }

    public List<DriveRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String startPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, startPlace, benutzerUsername);
    }

    public List<DriveRoute> findRouten(User user, DriveType driveType, String destinationPlace, String startPlace) {
        List<DriveRoute> routen = findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, destinationPlace, user.getUsername());

        return routen.size() > 0 ? routen : switch (driveType) {
            case OUTWARD_TRIP -> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, destinationPlace, user.getUsername());
            case RETURN_TRIP -> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, user.getUsername());
        };
    }

}
