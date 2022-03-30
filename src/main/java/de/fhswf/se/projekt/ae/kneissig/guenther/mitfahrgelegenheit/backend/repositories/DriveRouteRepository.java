package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TODO: Datenbank löschen schauen ob es noch geht lel
 * Datenbank löschen und neu anlegen am Ende
 */
public interface DriveRouteRepository extends JpaRepository<DriveRoute, Integer> {

    List<DriveRoute> findAllByDriver(User driver);

    List<DriveRoute> findAllByDriverAndDriveType(User driver, DriveType driveType);

    List<DriveRoute> findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(DriveType driveType, String startPlace, String username);

    List<DriveRoute> findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(DriveType driveType, String destinationPlace, String username);

    List<DriveRoute> findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(
            DriveType driveType, String destinationPlace, String startPlace, String username);
}
