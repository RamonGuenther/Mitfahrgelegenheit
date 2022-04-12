package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        System.out.println("Ich lösche ja");
        repository.delete(driveRequest);
    }

    public Optional<List<DriveRequest>> findAllDriveRequestsDriver(User driver){
        return repository.findAllByDriveRoute_DriverAndRequestState(driver, RequestState.OPEN);
    }

    public Optional<List<DriveRequest>> findAllDriveRequestsPassenger(User passenger){
        return repository.findAllByPassenger(passenger);
    }
}
