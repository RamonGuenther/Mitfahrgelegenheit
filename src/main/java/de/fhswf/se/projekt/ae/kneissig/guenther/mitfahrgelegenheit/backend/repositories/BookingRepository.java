package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Das BookingRepository ist für die Datenbankabfragen der Bookings-Tabelle zuständig. Die
 * Tabelle speichert alle Fahrten-Buchungen von Benutzern.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<List<Booking>> findAllByPassengerAndDriveRoute_DriveType(User passenger, DriveType driveType);

    Optional<List<Booking>> findAllByPassenger(User passenger);

    @Query("SELECT b FROM Booking b WHERE b.driveRoute.driver = :user and b.ratedByDriver = false and b.driveRoute.drivingTime < :date or b.driveRoute.driver = :user and b.ratedByDriver = false and b.regularDriveSingleDriveDate < :date")
    Optional<List<Booking>> findAllByDriverAndDrivingTimeAndRatedByDriver(User user, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.passenger = :user and b.ratedByPassenger = false and b.driveRoute.drivingTime < :date or b.passenger = :user and b.ratedByPassenger = false and b.regularDriveSingleDriveDate < :date")
    Optional<List<Booking>> findAllByPassengerAndDrivingTimeAndRatedByPassenger(User user, LocalDateTime date);

    Optional<List<Booking>> findAllByPassengerAndDriveRoute_RegularDrive_RegularDriveDateEnd_IsNotNull(User user);
}
