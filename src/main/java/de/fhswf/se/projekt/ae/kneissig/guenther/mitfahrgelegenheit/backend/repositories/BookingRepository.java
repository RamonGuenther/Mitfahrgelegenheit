package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<List<Booking>> findAllByPassengerAndDriveRoute_DriveType(User passenger, DriveType driveType);

    Optional<List<Booking>> findAllByPassenger(User passenger);

    @Query("SELECT b FROM Booking b WHERE b.driveRoute.driver = :user and b.driveRoute.drivingTime < :date")
    Optional<List<Booking>> findAllByDriverAndDrivingTime(User user, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.passenger = :user and b.driveRoute.drivingTime < :date")
    Optional<List<Booking>> findAllByPassengerAndDrivingTime(User user, LocalDateTime date);
}
