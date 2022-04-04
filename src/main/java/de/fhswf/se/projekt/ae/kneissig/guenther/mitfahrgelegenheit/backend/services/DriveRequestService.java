package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriveRequestService {

    private DriveRequestRepository repository;

    public DriveRequestService(DriveRequestRepository repository) {
        this.repository = repository;
    }

    public void save(DriveRequest newDriveRequest){
        repository.save(newDriveRequest);
    }

    public void delete(DriveRequest driveRequest){
        repository.delete(driveRequest);
    }

    public List<DriveRequest> findAllDriveRequestsDriver(User driver){
        return repository.findAllByDriveRoute_DriverAndRequestState(driver, RequestState.OPEN);
    }

    public List<DriveRequest> findAllDriveRequestsPassenger(User passenger){
        return repository.findAllByPassenger(passenger);
    }
}
