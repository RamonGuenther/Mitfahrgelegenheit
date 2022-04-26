package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Das DriveRequestRepository ist für die Datenbankabfragen der DriveRequest-Tabelle zuständig. Die
 * Tabelle speichert alle Anfragen zu Fahrtangeboten.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public interface DriveRequestRepository extends JpaRepository<DriveRequest, Integer> {

    Optional<List<DriveRequest>> findAllByDriveRoute_DriverAndRequestState(User driver, RequestState requestState);

    Optional<List<DriveRequest>> findAllByPassenger(User passenger);

}
