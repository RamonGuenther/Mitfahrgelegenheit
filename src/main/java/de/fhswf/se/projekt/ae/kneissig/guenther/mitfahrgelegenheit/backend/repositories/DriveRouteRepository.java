package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Das DriveRouteRepository ist für die Datenbankabfragen der DriveRoute-Tabelle zuständig. Die
 * Tabelle speichert die Fahrtangebote der Nutzer.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public interface DriveRouteRepository extends JpaRepository<DriveRoute, Integer> {

    Optional<List<DriveRoute>> findAllByDriver(User driver);

    Optional<List<DriveRoute>> findAllByDriverAndDriveType(User driver, DriveType driveType);

    Optional<List<DriveRoute>>findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(DriveType driveType, String startPlace, String username);

    Optional<List<DriveRoute>> findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(DriveType driveType, String destinationPlace, String username);

    Optional<List<DriveRoute>>findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(
            DriveType driveType, String destinationPlace, String startPlace, String username);

    Optional<List<DriveRoute>> findAllByDriverAndDrivingTimeBeforeAndAndBookings_Empty(User user, LocalDateTime dateTime);

    Optional<List<DriveRoute>> findAllByDriverAndRegularDrive_RegularDriveDateEnd_IsNull(User user);

    Optional<List<DriveRoute>> findAllByDriverAndRegularDrive_RegularDriveDateEnd_IsNotNull(User user);

}

