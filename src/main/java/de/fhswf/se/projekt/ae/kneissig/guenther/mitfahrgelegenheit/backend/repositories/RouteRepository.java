package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TODO: Datenbank löschen schauen ob es noch geht lel
 * Datenbank löschen und neu anlegen am Ende
 */
public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findAllByDriver(User driver);
    List<Route> findAllByDriverAndDriveType(User driver, DriveType driveType);
    List<Route> findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(DriveType driveType, String startPlace, String username);
    List<Route> findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(DriveType driveType, String destinationPlace, String username);
    List<Route> findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(
            DriveType driveType, String destinationPlace, String startPlace, String username);
}
