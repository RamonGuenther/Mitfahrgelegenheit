package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * TODO: Andere Namen für die Methoden ausdenken und Validieren in Searchdrive ob es überhaupt funktioniert also findRouten
 */
@Service
public class DriveRouteService {

    private final DriveRouteRepository repository;


    public DriveRouteService(DriveRouteRepository repository) {
        this.repository = repository;
    }

    public void save(DriveRoute driveRoute) {
        repository.save(driveRoute);
    }

    public void delete(DriveRoute driveRoute){
        repository.delete(driveRoute);
    }

    public List<DriveRoute> findAllFahrerRoutenByBenutzer(User user) {
        return repository.findAllByDriver(user);
    }

    public List<DriveRoute> findAllByBenutzerAndFahrtenTyp(User user, DriveType fahrtenTyp) {
        return repository.findAllByDriverAndDriveType(user, fahrtenTyp);
    }

    public List<DriveRoute> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String startPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, benutzerUsername);
    }

    public List<DriveRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, benutzerUsername);
    }

    public List<DriveRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String startPlace, String destinationPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, destinationPlace, benutzerUsername);
    }

    public List<DriveRoute> findRouten(User user, DriveType driveType, String destinationPlace, String startPlace) {
        List<DriveRoute> routen = findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, destinationPlace, user.getUsername());

        return routen.size() > 0 ? routen : switch (driveType) {
            case OUTWARD_TRIP -> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(driveType, destinationPlace, user.getUsername());
            case RETURN_TRIP -> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(driveType, startPlace, user.getUsername());
        };
    }

    public Optional<DriveRoute> findById(Integer id) {
        return repository.findById(id);
    }

    public List<DriveRoute> findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNotAndDestination_Time(DriveType driveType, String startPlace, String destinationPlace, User user, LocalDateTime datetime, boolean regularDrive) {
        List<DriveRoute> driveRoutes = new ArrayList<>();
        List<DriveRoute> unfilteredRoutes = findRouten(user, driveType, destinationPlace, startPlace);

        switch (driveType) {
            case OUTWARD_TRIP -> {
                if (regularDrive) {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getZiel().getTime().toLocalDate().equals(datetime.toLocalDate()) ||
                                route.getZiel().getTime().toLocalDate().isAfter(datetime.toLocalDate()) &&
                                        route.getZiel().getTime().toLocalTime().isBefore(datetime.toLocalTime()) ||
                                route.getZiel().getTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                } else {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getZiel().getTime().toLocalDate().equals(datetime.toLocalDate()) &&
                                route.getZiel().getTime().toLocalTime().isBefore(datetime.toLocalTime()) ||
                                route.getZiel().getTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                }
            }
            case RETURN_TRIP -> {
                if (regularDrive) {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getZiel().getTime().toLocalDate().equals(datetime.toLocalDate()) ||
                                route.getZiel().getTime().toLocalDate().isAfter(datetime.toLocalDate()) &&
                                        route.getZiel().getTime().toLocalTime().isAfter(datetime.toLocalTime()) ||
                                route.getZiel().getTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                } else {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getZiel().getTime().toLocalDate().equals(datetime.toLocalDate()) &&
                                route.getZiel().getTime().toLocalTime().isAfter(datetime.toLocalTime()) ||
                                route.getZiel().getTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                }
            }
        }

        return driveRoutes;
    }

    public DriveRoute findNextDriveRouteByUserComparedByTime(User user) {

        List<DriveRoute> outwardTrips = repository.findAllByDriverAndDriveType(user, DriveType.OUTWARD_TRIP);
        outwardTrips.sort(Comparator.comparing(driveRoute -> driveRoute.getZiel().getTime()));

        List<DriveRoute> returnTrips = repository.findAllByDriverAndDriveType(user, DriveType.RETURN_TRIP);
        returnTrips.sort(Comparator.comparing(driveRoute -> driveRoute.getStart().getTime()));

        if (!outwardTrips.isEmpty() && !returnTrips.isEmpty()) {
            if (outwardTrips.get(0).getZiel().getTime().isBefore(returnTrips.get(0).getStart().getTime())) {
                return outwardTrips.get(0);
            } else {
                return returnTrips.get(0);
            }
        } else if (outwardTrips.isEmpty() && !returnTrips.isEmpty()) {
            return returnTrips.get(0);
        } else if (returnTrips.isEmpty() && !outwardTrips.isEmpty()) {
            return outwardTrips.get(0);
        } else {
            return null;
        }
    }
}
