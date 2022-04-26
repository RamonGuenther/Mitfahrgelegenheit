package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Die Klasse DriveRequestService dient als Schnittstelle zwischen der Applikation und
 * der Datenbank. Sie bietet diverse Methoden zum Verwalten von Fahrt-Anfragen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Service
public class DriveRequestService {

    private DriveRequestRepository repository;

    public DriveRequestService(DriveRequestRepository repository) {
        this.repository = repository;
    }

    /**
     * Die save-Methode speichert eine Anfrage in der Datenbank. Eine bereits existierende
     * Anfrage wird aktualisiert.
     *
     * @param newDriveRequest        Anfrage, die gespeichert werden soll
     */
    public void save(DriveRequest newDriveRequest){
        repository.save(newDriveRequest);
    }

    /**
     * Die delete-Methode löscht eine Anfrage aus der Datenbank.
     *
     * @param driveRequest           Anfrage, die gelöscht werden soll
     */
    public void delete(DriveRequest driveRequest){
        repository.delete(driveRequest);
    }

    /**
     * Die Methode findAllDriveRequestsDriver gibt alle offenen Fahrtanfragen für einen
     * Benutzer in der Rolle des Fahrers zurück.
     *
     * @param driver         Benutzer in der Rolle als Fahrer
     * @return               Liste mit offenen Anfragen des Benutzers
     */
    public Optional<List<DriveRequest>> findAllDriveRequestsDriver(User driver){
        return repository.findAllByDriveRoute_DriverAndRequestState(driver, RequestState.OPEN);
    }

    /**
     * Die Methode findAllDriveRequestsPassenger gibt alle Fahrtanfragen, unabhängig vom
     * Status der Anfrage, für einen Benutzer in der Rolle als Mitfahrer zurück.
     *
     * @param passenger     Benutzer in der Rolle als Mitfahrer
     * @return              Liste mit Anfragen des Benutzers
     */
    public Optional<List<DriveRequest>> findAllDriveRequestsPassenger(User passenger){
        return repository.findAllByPassenger(passenger);
    }
}
