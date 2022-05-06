package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateBookingException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse TestDataInit erzeugt Testdaten für die Entwicklung der Applikation.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Component
public class TestDataInit {

    @Autowired
    private UserService userService;

    @Autowired
    private DriveRouteService driveRouteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DriveRequestService driveRequestService;

    @Autowired
    private BookingService bookingService;


    @PostConstruct
    public void createUser() throws DuplicateRequestException, DuplicateBookingException {

        /*-------------------------------------------------------------------------------------------------------------
                                                       User
        -------------------------------------------------------------------------------------------------------------*/
        User ramon = new User(
                1L,
                "rague002",
                passwordEncoder.encode("1234"),
                "Ramon",
                "Günther",
                new Address("58636", "Iserlohn", "Sundernalle", "75"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "guenther.ramonantonio@fh-swf.de",
                LocalDateTime.now(),
                true
        );
        userService.save(ramon);

        User ivonne = new User(
                2L,
                "ivkne001",
                passwordEncoder.encode("1234"),
                "Ivonne",
                "Kneißig",
                new Address("58097", "Hagen", "Diesterwegstraße", "6"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "kneissig.ivonne@fh-swf.de",
                LocalDateTime.now(),
                true
        );
        userService.save(ivonne);

        User alina = new User(
                3L,
                "alnie001",
                passwordEncoder.encode("1234"),
                "Alina",
                "Nieswand",
                new Address("45145", "Essen", "Am Alfredspark", "7"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "kneissig.ivonne@fh-swf.de",
                LocalDateTime.now(),
                true
        );
        userService.save(alina);


        User rolf = new User(
                4L,
                "rowil001",
                passwordEncoder.encode("1234"),
                "Rolf",
                "Wilke",
                new Address("58840", "Plettenberg", "Eschener Weg", "45"),
                new Languages("Deutsch"),
                "Hagen",
                "Technische Betriebswirtschaft",
                "guenther.ramonantonio@fh-swf.de",
                LocalDateTime.now(),
                true
        );

        userService.save(rolf);


        User max = new User(
                5L,
                "mamus001",
                passwordEncoder.encode("1234"),
                "Max",
                "Mustermann",
                new Address("58093", "Hagen", "Winkelstück", "34"),
                new Languages("Deutsch"),
                null,
                null,
                null,
                LocalDateTime.now(),
                false
        );

        userService.save(max);



        /*-------------------------------------------------------------------------------------------------------------
                                                       Fahrtangebote
        -------------------------------------------------------------------------------------------------------------*/

       /*-------------------------------------------------------------------------------------------------------------
                                                    Fahrtangebote - Ramon
        -------------------------------------------------------------------------------------------------------------*/

        Start start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"));
        Destination destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        List<Stopover> stopoverList = new ArrayList<>();

        RouteString routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteR1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 8, 30),
                false,
                4,
                ramon,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteR1);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58636", "Iserlohn", "Sundernallee", "75"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteR2 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 17, 0),
                false,
                4,
                ramon,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteR2);


        start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteR3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 8, 30),
                false,
                4,
                ramon,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteR3);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58636", "Iserlohn", "Sundernallee", "75"));

        DriveRoute DriveRouteR4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 16, 30),
                false,
                4,
                ramon,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(DriveRouteR4);

        start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteR4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 9, 15),
                false,
                4,
                ramon,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteR4);


        RegularDrive regularDriveR1 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 5, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteR5 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 14, 0),
                true,
                1,
                ivonne,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteR5.setRegularDrive(regularDriveR1);

        driveRouteService.save(driveRouteR5);


       /*-------------------------------------------------------------------------------------------------------------
                                                    Fahrtangebote - Ivonne
        -------------------------------------------------------------------------------------------------------------*/


        start = new Start(new Address("58095", "Hagen", "Diesterwegstraße", "6"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteI1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 10, 45),
                false,
                1,
                ivonne,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteI1);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58644", "Hagen", "Diesterwegstraße", "6"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteI2 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 18, 30),
                false,
                1,
                ivonne,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteI2);


        start = new Start(new Address("58095", "Hagen", "Diesterwegstraße", "6"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteI3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 7, 15),
                false,
                1,
                ivonne,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteI3);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58095", "Hagen", "Diesterwegstraße", "6"));

        DriveRoute driveRouteI4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 13, 0),
                false,
                1,
                ivonne,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteI4);


        RegularDrive regularDriveI5 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58089", "Hagen", "Diesterwegstraße", "6"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteI5 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 8, 0),
                true,
                1,
                ivonne,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteI5.setRegularDrive(regularDriveI5);

        driveRouteService.save(driveRouteI5);


        RegularDrive regularDriveI6 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58089", "Hagen", "Diesterwegstraße", "6"));

        DriveRoute driveRouteI6 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 14, 0),
                true,
                1,
                ivonne,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteI6.setRegularDrive(regularDriveI6);

        driveRouteService.save(driveRouteI6);



        /*-------------------------------------------------------------------------------------------------------------
                                                    Fahrtangebote - Alina
        -------------------------------------------------------------------------------------------------------------*/

        start = new Start(new Address("45145", "Essen", "Am Alfredspark", "7"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteM1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 6, 45),
                false,
                4,
                alina,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM1);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("45145", "Essen", "Am Alfredspark", "7"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteM2 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 17, 45),
                false,
                4,
                alina,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteM2);


        start = new Start(new Address("45145", "Essen", "Am Alfredspark", "7"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteM3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 11, 0),
                false,
                4,
                alina,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM3);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("45145", "Essen", "Am Alfredspark", "7"));

        DriveRoute driveRouteM4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 18, 20),
                false,
                4,
                alina,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM4);


        RegularDrive regularDriveM5 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("45145", "Essen", "Am Alfredspark", "7"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteM5 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 10, 45),
                true,
                4,
                alina,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteM5.setRegularDrive(regularDriveM5);

        driveRouteService.save(driveRouteM5);


        RegularDrive regularDriveM6 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("45145", "Essen", "Am Alfredspark", "7"));

        DriveRoute driveRouteM6 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 16, 15),
                true,
                4,
                alina,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteM6.setRegularDrive(regularDriveM6);

        driveRouteService.save(driveRouteM6);


        /*-------------------------------------------------------------------------------------------------------------
                                                Fahrtangebote - Rolf
        -------------------------------------------------------------------------------------------------------------*/

        start = new Start(new Address("58840", "Plettenberg", "Eschener Weg", "45"));
        destination = new Destination(new Address("58095", "Hagen", "Haldener Str.", "128"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteS1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 10, 50),
                false,
                4,
                rolf,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteS1);


        start = new Start(new Address("58095", "Hagen", "Haldener Str.", "128"));
        destination = new Destination(new Address("58840", "Plettenberg", "Eschener Weg", "45"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteS2 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 1, 13, 45),
                false,
                4,
                rolf,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteS2);


        start = new Start(new Address("58840", "Plettenberg", "Eschener Weg", "45"));
        destination = new Destination(new Address("58095", "Hagen", "Haldener Str.", "128"));

        DriveRoute driveRouteS3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 7, 15),
                false,
                4,
                rolf,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteS3);


        start = new Start(new Address("58095", "Hagen", "Haldener Str.", "128"));
        destination = new Destination(new Address("58840", "Plettenberg", "Eschener Weg", "45"));

        DriveRoute driveRouteS4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 7, 8, 19, 20),
                false,
                4,
                rolf,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteS4);


        RegularDrive regularDriveS5 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58840", "Plettenberg", "Eschener Weg", "45"));
        destination = new Destination(new Address("58095", "Hagen", "Haldener Str.", "128"));

        DriveRoute driveRouteS5 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 9, 50),
                true,
                1,
                rolf,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteS5.setRegularDrive(regularDriveS5);

        driveRouteService.save(driveRouteS5);


        RegularDrive regularDriveS6 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 30));

        start = new Start(new Address("58095", "Hagen", "Haldener Str.", "128"));
        destination = new Destination(new Address("58840", "Plettenberg", "Eschener Weg", "45"));

        DriveRoute driveRouteS6 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 18, 0),
                true,
                1,
                rolf,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteS6.setRegularDrive(regularDriveS6);

        driveRouteService.save(driveRouteS6);


        /*-------------------------------------------------------------------------------------------------------------
                                                      Bewertungen - Ramon
        -------------------------------------------------------------------------------------------------------------*/

        UserRating userRating = new UserRating();

        Rating rating = new Rating(4, 5);

        userRating.addDriverRating(rating);

        rating = new Rating(5, 4);
        userRating.addDriverRating(rating);

        rating = new Rating(3, 2);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        ramon.setUserRating(userRating);

        userService.save(ramon);


        /*-------------------------------------------------------------------------------------------------------------
                                                      Bewertungen - Ivonne
        -------------------------------------------------------------------------------------------------------------*/


        userRating = new UserRating();

        rating = new Rating(5, 5);

        userRating.addDriverRating(rating);

        rating = new Rating(5, 5);
        userRating.addDriverRating(rating);

        rating = new Rating(1, 1);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        ivonne.setUserRating(userRating);

        userService.save(ivonne);

        /*-------------------------------------------------------------------------------------------------------------
                                                      Bewertungen - Alina
        -------------------------------------------------------------------------------------------------------------*/

        userRating = new UserRating();

        rating = new Rating(3, 5);

        userRating.addDriverRating(rating);

        rating = new Rating(5, 2);
        userRating.addDriverRating(rating);

        rating = new Rating(1, 1);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        alina.setUserRating(userRating);

        userService.save(alina);

        /*-------------------------------------------------------------------------------------------------------------
                                                      Bewertungen - Rolf
        -------------------------------------------------------------------------------------------------------------*/

        userRating = new UserRating();

        rating = new Rating(3, 1);

        userRating.addDriverRating(rating);

        rating = new Rating(4, 2);
        userRating.addDriverRating(rating);

        rating = new Rating(4, 3);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        rolf.setUserRating(userRating);

        userService.save(rolf);


        /*-------------------------------------------------------------------------------------------------------------
                                                  Fahrtanfragen an Ramon
        -------------------------------------------------------------------------------------------------------------*/

        DriveRequest driveRequest = new DriveRequest(
                driveRouteR1,
                ivonne,
                "Du kannst mich bei Fragen gerne per WhatsApp kontaktieren. Meine Nummer ist 12345/1234567",
                "",
                new Stopover(new Address("58095", "Hagen", "Diesterwegstraße", "6"))
        );

        driveRouteR1.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR1);


        driveRequest = new DriveRequest(
                driveRouteR1,
                alina,
                "Wäre cool, wenn ich mitfahren darf :)",
                "",
                new Stopover(new Address("45145", "Essen", "Am Alfredspark", "7"))
        );

        driveRouteR1.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR1);


        driveRequest = new DriveRequest(
                driveRouteR1,
                rolf,
                "",
                "",
                new Stopover(new Address("58840", "Plettenberg", "Eschener Weg", "45"))
        );

        driveRouteR1.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR1);


        //Fahrtanfrage

        driveRequest = new DriveRequest(
                driveRouteR2,
                max,
                "",
                "",
                new Stopover(new Address("58636", "Iserlohn", "Zur Sonnenhöhe", "102"))
        );

        driveRouteR2.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR2);

        //Buchung

        driveRequest.setRequestState(RequestState.ACCEPTED);
        driveRequestService.save(driveRequest);
        Booking newBooking = new Booking(driveRequest.getDriveRoute(), driveRequest.getPassenger(), driveRequest.getStopover());
        bookingService.save(newBooking);
        driveRequest.getDriveRoute().addBooking(newBooking);
        driveRouteService.save(driveRequest.getDriveRoute());


    }
}



