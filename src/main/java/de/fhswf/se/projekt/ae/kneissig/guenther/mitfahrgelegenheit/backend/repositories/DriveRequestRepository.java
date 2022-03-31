package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriveRequestRepository extends JpaRepository<DriveRequest, Integer> {

    List<DriveRequest> findAllByDriveRoute_DriverAndRequestState(User driver, RequestState requestState);

    List<DriveRequest> findAllByPassenger(User passenger);

}
