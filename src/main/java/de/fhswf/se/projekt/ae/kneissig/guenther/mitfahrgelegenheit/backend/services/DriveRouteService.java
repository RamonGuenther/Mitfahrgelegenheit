package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Die Klasse DriveRouteService dient als Schnittstelle zwischen der Applikation und
 * der Datenbank. Sie bietet diverse Methoden zum Verwalten von Fahrten.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Service
public class DriveRouteService {

    private final DriveRouteRepository repository;

    public DriveRouteService(DriveRouteRepository repository) {
        this.repository = repository;
    }

    /**
     * Die save-Methode speichert eine Fahrt in der Datenbank. Eine bereits bestehende
     * Fahrt wird aktualisiert.
     *
     * @param driveRoute        Fahrt, die gespeichert werden soll
     */
    public void save(DriveRoute driveRoute) {
        repository.save(driveRoute);
    }


    /**
     * Die delete-Methode löscht eine Fahrt aus der Datenbank.
     *
     * @param driveRoute        Fahrt die gelöscht werden soll
     */
    public void delete(DriveRoute driveRoute) {
        repository.delete(driveRoute);
    }

    /**
     * Die Methode findById gibt die Fahrt mit der gegebenen Id zurück.
     *
     * @param id                Id der gesuchten Fahrt
     * @return                  Fahrt mit der gegebenen Id
     */
    public Optional<DriveRoute> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Die Methode getDriveRoutesByUser gibt alle angebotenen Fahrten eines
     * bestimmten Benutzers zurück.
     *
     * @param user              Benutzer dessen Fahrtangebote gesucht werden
     * @return                  Liste der Fahrtangebote des Benutzers
     */
    public Optional<List<DriveRoute>> getDriveRoutesByUser(User user) {
        return repository.findAllByDriver(user);
    }

    /**
     * Die Methode getByUserAndDriveType gibt die Fahrten eines Users mit einem bestimmten
     * Fahrtentyp zurück.
     *
     * @param user              Benutzer dessen Fahrtangebote gesucht werden
     * @param fahrtenTyp        Fahrtentyp, nach dem die Fahrtangebote gefiltert werden
     * @return                  Liste der Fahrtangebote des Benutzers
     */
    public Optional<List<DriveRoute>> getByUserAndDriveType(User user, DriveType fahrtenTyp) {
        return repository.findAllByDriverAndDriveType(user, fahrtenTyp);
    }

    /**
     * Die Methode getOtherUsersDriveRoutesByDriveType findet alle Fahrtangebote, die nicht die eigenen des
     * angemeldeten Benutzers sind. Die Fahrten werden außerdem nach Start- und Zieladresse und Fahrtentyp gefiltert.
     *
     * @param driveType         Fahrtentyp, nach dem gefiltert wird
     * @param startPlace        Startadresse, nach der gefiltert wird
     * @param destinationPlace  Zieladresse, nach der gefiltert wird
     * @param username  Benutzer, dessen Fahrtangebote bei der Suche ausgeschlossen werden
     * @return                  Liste mit gefilterten Fahrtangeboten
     */
    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveType(DriveType driveType, String startPlace, String destinationPlace, String username) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, startPlace, username);
    }

    /**
     * Die Methode getOtherUsersDriveRoutesByDriveTypeAndStartPlace findet alle Fahrtangebote, die nicht die eigenen des
     * angemeldeten Benutzers sind. Die Fahrten werden außerdem nach Startadresse und Fahrtentyp gefiltert.
     *
     * @param driveType         Fahrtentyp, nach dem gefiltert wird
     * @param startPlace        Startadresse, nach der gefiltert wird
     * @param username          Benutzer, dessen Fahrtangebote bei der Suche ausgeschlossen werden
     * @return                  Liste mit gefilterten Fahrtangeboten
     */
    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveTypeAndStartPlace(DriveType driveType, String startPlace, String username) {
        return repository.findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, username);
    }

    /**
     * Die Methode getOtherUsersDriveRoutesByDriveTypeAndDestinationPlace findet alle Fahrtangebote, die nicht die eigenen des
     * angemeldeten Benutzers sind. Die Fahrten werden außerdem nach Zieladresse und Fahrtentyp gefiltert.
     *
     * @param driveType         Fahrtentyp, nach dem gefiltert wird
     * @param destinationPlace  Zieladresse, nach der gefiltert wird
     * @param username          Benutzer, dessen Fahrtangebote bei der Suche ausgeschlossen werden
     * @return                  Liste mit gefilterten Fahrtangeboten
     */
    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveTypeAndDestinationPlace(DriveType driveType, String destinationPlace, String username) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, username);
    }

    /**
     * Die Methode findRouten versucht für die Fahrtsuche eines Benutzers zunächst passende Fahrtangebote zu finden, die
     * sowohl Start- als auch Zieladresse der Suche entsprechen. Wird keine passende Fahrt gefunden, wird als Grundlage,
     * je nachdem ob es sich um eine Hin- oder Rückfahrt handelt, nur nach passender Start- oder Zieladresse gesucht.
     *
     * @param user              Benutzer, dessen eigene Fahrtangebote herausgefiltert werden
     * @param driveType         Fahrtentyp, nach dem der Benutzer sucht
     * @param destinationPlace  Zieladresse, nach der gesucht wird
     * @param startPlace        Startadresse, nach der gesucht wird
     * @return                  Liste mit passenden Fahrtangeboten
     */
    public Optional<List<DriveRoute>> findRouten(User user, DriveType driveType, String destinationPlace, String startPlace) {
        List<DriveRoute> routen = getOtherUsersDriveRoutesByDriveType(driveType, startPlace, destinationPlace, user.getUsername()).orElse(Collections.emptyList());

        return routen.size() > 0 ? Optional.of(routen) : switch (driveType) {
            case OUTWARD_TRIP -> getOtherUsersDriveRoutesByDriveTypeAndDestinationPlace(driveType, destinationPlace, user.getUsername());
            case RETURN_TRIP -> getOtherUsersDriveRoutesByDriveTypeAndStartPlace(driveType, startPlace, user.getUsername());
        };
    }

    /**
     * Die Methode getDriveRoutesForSearchDrive ist für das Suchergebnis der Fahrtensuche eines Benutzers zuständig.
     * Durch den Aufruf der Methode findRouten werden zunächst passende Fahrten bezüglich des Fahrtentyps und der
     * Start-/Zieladresse gesucht. Anschließend wird nach dem passenden Datum und der passenden Zeit gesucht.
     * Dabei werden bei einer Einzelfahrtsuche auch regelmäßige Fahrten durchsucht, die ggf. das passende Datum
     * beinhalten.
     *
     * @param driveType             Fahrtentyp, nach dem gefiltert wird
     * @param startPlace            Startadresse des suchenden Benutzers
     * @param destinationPlace      Zieladresse des suchenden Benutzers
     * @param user                  Benutzer, der ein Fahrtangebot sucht
     * @param datetime              Datum und Uhrzeit, nach dem der Benutzer sucht
     * @param regularDrive          Gibt an, ob der Benutzer eine regelmäßige Fahrt oder eine Einzelfahrt such
     * @param dayOfWeek             Gibt bei einer regelmäßigen Fahrt den gewünschten Wochentag an
     * @return                      Liste mit passenden Fahrtangeboten
     */
    public List<DriveRoute> getDriveRoutesForSearchDrive(DriveType driveType, String startPlace, String destinationPlace, User user, LocalDateTime datetime, boolean regularDrive, DayOfWeek dayOfWeek) {

        List<DriveRoute> driveRoutes = new ArrayList<>();
        List<DriveRoute> unfilteredRoutes = findRouten(user, driveType, destinationPlace, startPlace)
                .orElse(Collections.emptyList())
                .stream()
                .filter(filter -> filter.getRegularDrive().getRegularDriveDateEnd() != null &&                          // wenn regelmäßige Fahrt
                        filter.getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now()) ||                   // und heutiges Datum liegt im Zeitraum der regelmäßigen Fahrt
                        filter.getDrivingTime().toLocalDate().isAfter(LocalDate.now()))                                 // oder Einzelfahrt liegt nach dem heutigen Datum
                .collect(Collectors.toList());

        if (regularDrive) {
            unfilteredRoutes = unfilteredRoutes.stream()
                    .filter(driveRoute -> driveRoute.getRegularDrive().getRegularDriveDateEnd() != null &&              // wenn regelmäßige Fahrt
                            driveRoute.getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now()) &&           // und heutiges Datum liegt im Zeitraum der regelmäßigen Fahrt
                            driveRoute.getRegularDrive().getRegularDriveDay().equals(dayOfWeek))                        // und gewünschter Wochentag stimmt überein
                    .collect(Collectors.toList());
        } else {
            unfilteredRoutes = unfilteredRoutes.stream()
                    .filter(driveRoute -> driveRoute.getRegularDrive().getRegularDriveDateEnd() == null &&              // wenn keine regelmäßige Fahrt
                            driveRoute.getDrivingTime().toLocalDate().equals(datetime.toLocalDate()) ||                 // und Tag der Fahrt entspricht Suchdatum
                            driveRoute.getRegularDrive().getRegularDriveDateEnd() != null &&                            // oder regelmäßige Fahrt
                                    driveRoute.getRegularDrive().getDriveDates().contains(datetime.toLocalDate()))              // und Suchdatum liegt im Zeitraum
                    .collect(Collectors.toList());
        }

        switch (driveType) {
            case OUTWARD_TRIP -> {
                for (DriveRoute route : unfilteredRoutes) {
                    if (route.getDrivingTime().toLocalTime().isBefore(datetime.toLocalTime()) ||
                            route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                        driveRoutes.add(route);
                    }
                }
            }
            case RETURN_TRIP -> {
                for (DriveRoute route : unfilteredRoutes) {
                    if (route.getDrivingTime().toLocalTime().isAfter(datetime.toLocalTime()) ||
                            route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                        driveRoutes.add(route);
                    }
                }
            }
        }
        driveRoutes = driveRoutes
                .stream()
                .filter(filter -> filter.getSeatCount() > filter.getBookings().size())
                .collect(Collectors.toList());

        return driveRoutes;
    }

    /**
     * Die Methode getNextSingleDriveRouteByUser gibt die nächste Einzelfahrt des Benutzers in der
     * Rolle als Fahrer zurück.
     *
     * @param user              Benutzer, dessen nächste Fahrt gesucht wird
     * @return                  Nächste Einzelfahrt des Benutzers
     */
    public Optional<DriveRoute> getNextSingleDriveRouteByUser(User user) {
        List<DriveRoute> outwardTrips = repository.findAllByDriverAndRegularDrive_RegularDriveDateEnd_IsNull(user).orElse(Collections.emptyList());

        outwardTrips = outwardTrips.stream().filter(driveRoute ->
                driveRoute.getDrivingTime().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()) ||
                driveRoute.getDrivingTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()) &&
                driveRoute.getDrivingTime().toLocalTime().isAfter(LocalDateTime.now().toLocalTime()))
                .collect(Collectors.toList());

        outwardTrips.sort(Comparator.comparing(DriveRoute::getDrivingTime));

        return outwardTrips.size() > 0 ? Optional.of(outwardTrips.get(0)) : Optional.empty();
    }

    /**
     * Die Methode getNextRegularDriveRouteByUser gibt die nächste regelmäßige Fahrt des Benutzers in der
     * Rolle als Fahrer zurück.
     *
     * @param user          Benutzer, dessen nächste Fahrt gesucht wird
     * @return              Nächste regelmäßige Fahrt des Benutzers
     */
    public Optional<DriveRoute> getNextRegularDriveRouteByUser(User user){
        List<DriveRoute> outwardTrips = repository.findAllByDriverAndRegularDrive_RegularDriveDateEnd_IsNotNull(user).orElse(Collections.emptyList());

        outwardTrips = outwardTrips.stream().filter(driveRoute ->
                driveRoute.getDrivingTime().toLocalDate().equals(LocalDate.now()) &&
                driveRoute.getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now()) ||
                driveRoute.getDrivingTime().toLocalDate().isAfter(LocalDate.now()) &&
                driveRoute.getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now())
        ).collect(Collectors.toList());

        outwardTrips.sort(Comparator.comparing(driveRoute -> driveRoute.getRegularDrive().getRegularDriveDay()));

        return outwardTrips.size() > 0 ? Optional.of(outwardTrips.get(0)) : Optional.empty();
    }

    /**
     * Die Methode cleanCompletedDriveRoutesByUser löscht alle abgeschlossenen Fahrten eines Benutzers, bei denen
     * keine Buchung mehr in der Buchungsliste ist.
     *
     * @param user          Benutzer, dessen abgeschlossenen Fahrten aus der Datenbank gelöscht werden sollen
     */
    public void cleanCompletedDriveRoutesByUser(User user) {
        List<DriveRoute> driveRoutes = repository.findAllByDriverAndDrivingTimeBeforeAndAndBookings_Empty(user, LocalDateTime.now()).orElse(Collections.emptyList());
        for (DriveRoute driveRoute : driveRoutes) {
            delete(driveRoute);
        }
    }
}
