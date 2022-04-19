package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.DriveRouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: Validieren in Searchdrive ob es überhaupt funktioniert also findRouten
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

    public void delete(DriveRoute driveRoute) {
        repository.delete(driveRoute);
    }

    public Optional<DriveRoute> findById(Integer id) {
        return repository.findById(id);
    }

    public List<DriveRoute> getDriveRoutesByUser(User user) {
        return repository.findAllByDriver(user).orElse(Collections.emptyList());
    }

    public Optional<List<DriveRoute>> getByUserAndDriveType(User user, DriveType fahrtenTyp) {
        return repository.findAllByDriverAndDriveType(user, fahrtenTyp);
    }

    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveType(DriveType driveType, String startPlace, String destinationPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, destinationPlace, benutzerUsername);
    }

    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveTypeAndStartPlace(DriveType driveType, String startPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndStart_Address_PlaceAndDriverUsernameNot(driveType, startPlace, benutzerUsername);
    }

    public Optional<List<DriveRoute>> getOtherUsersDriveRoutesByDriveTypeAndDestinationPlace(DriveType driveType, String destinationPlace, String benutzerUsername) {
        return repository.findAllByDriveTypeAndDestination_Address_PlaceAndDriverUsernameNot(driveType, destinationPlace, benutzerUsername);
    }

    public Optional<List<DriveRoute>> findRouten(User user, DriveType driveType, String destinationPlace, String startPlace) {
        List<DriveRoute> routen = getOtherUsersDriveRoutesByDriveType(driveType, startPlace, destinationPlace, user.getUsername()).orElse(Collections.emptyList());

        return routen.size() > 0 ? Optional.of(routen) : switch (driveType) {
            case OUTWARD_TRIP -> getOtherUsersDriveRoutesByDriveTypeAndDestinationPlace(driveType, destinationPlace, user.getUsername());
            case RETURN_TRIP -> getOtherUsersDriveRoutesByDriveTypeAndStartPlace(driveType, startPlace, user.getUsername());
        };
    }

    //TODO: Anschauen ob es noch Sinn ergibt, vllt Sql sortieren nach driveDate --  equals.currenttime ist unnötig? xD
    public List<DriveRoute> getDriveRoutesForSearchDrive(DriveType driveType, String startPlace, String destinationPlace, User user, LocalDateTime datetime, boolean regularDrive) {
        List<DriveRoute> driveRoutes = new ArrayList<>();
        List<DriveRoute> unfilteredRoutes = findRouten(user, driveType, destinationPlace, startPlace).orElse(Collections.emptyList());

        switch (driveType) {
            case OUTWARD_TRIP -> {
                if (regularDrive) {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getDrivingTime().toLocalDate().equals(datetime.toLocalDate()) ||
                                route.getDrivingTime().toLocalDate().isAfter(datetime.toLocalDate()) &&
                                        route.getDrivingTime().toLocalTime().isBefore(datetime.toLocalTime()) ||
                                route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                } else {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getDrivingTime().toLocalDate().equals(datetime.toLocalDate()) &&
                                route.getDrivingTime().toLocalTime().isBefore(datetime.toLocalTime()) ||
                                route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                }
            }
            case RETURN_TRIP -> {
                if (regularDrive) {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getDrivingTime().toLocalDate().equals(datetime.toLocalDate()) ||
                                route.getDrivingTime().toLocalDate().isAfter(datetime.toLocalDate()) &&
                                        route.getDrivingTime().toLocalTime().isAfter(datetime.toLocalTime()) ||
                                route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                } else {
                    for (DriveRoute route : unfilteredRoutes) {
                        if (route.getDrivingTime().toLocalDate().equals(datetime.toLocalDate()) &&
                                route.getDrivingTime().toLocalTime().isAfter(datetime.toLocalTime()) ||
                                route.getDrivingTime().toLocalTime().equals(datetime.toLocalTime())) {
                            driveRoutes.add(route);
                        }
                    }
                }
            }
        }
        return driveRoutes;
    }

    public Optional<DriveRoute> getNextDriveRouteByUser(User user) {
        List<DriveRoute> outwardTrips = repository.findAllByDriver(user).orElse(Collections.emptyList());
        outwardTrips.sort(Comparator.comparing(DriveRoute::getDrivingTime));
        outwardTrips = outwardTrips.stream().filter(driveRoute ->
                driveRoute.getDrivingTime().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()) ||
                        driveRoute.getDrivingTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()) &&
                                driveRoute.getDrivingTime().toLocalTime().isAfter(LocalDateTime.now().toLocalTime())).collect(Collectors.toList());

        return outwardTrips.size() > 0 ? Optional.of(outwardTrips.get(0)) : Optional.empty();
    }

    public void cleanCompletedDriveRoutesByUser(User user) {
        List<DriveRoute> driveRoutes = repository.findAllByDriverAndDrivingTimeBeforeAndAndBookings_Empty(user, LocalDateTime.now()).orElse(Collections.emptyList());
        for (DriveRoute driveRoute : driveRoutes) {
            delete(driveRoute);
        }
    }
}
