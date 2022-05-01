package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.*;
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
    public void createUser() throws DuplicateRequestException {

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

        User maren = new User(
                3L,
                "mapet001",
                passwordEncoder.encode("1234"),
                "Maren",
                "Peterson",
                new Address("58097", "Hagen", "Kratzkopfstraße", "10"),
                new Languages("Deutsch"),
                "Meschede",
                "Ingenieur- und Wirtschaftswissenschaften",
                "kneissig.ivonne@fh-swf.de",
                LocalDateTime.now(),
                true
        );
        userService.save(maren);


        User sebastian = new User(
                4L,
                "serap001",
                passwordEncoder.encode("1234"),
                "Sebastian",
                "Rapp",
                new Address("58840", "Plettenberg", "Eschener Weg", "45"),
                new Languages("Deutsch"),
                "Hagen",
                "Technische Betriebswirtschaft",
                "guenther.ramonantonio@fh-swf.de",
                LocalDateTime.now(),
                true
        );

        userService.save(sebastian);


        User emptyUser = new User(
                5L,
                "mamus001",
                passwordEncoder.encode("1234"),
                "Max",
                "Mustermann",
                new Address("58636", "Iserlohn", "Sundernallee", "75"),
                new Languages("Deutsch"),
                null,
                null,
                null,
                LocalDateTime.now(),
                false
        );

        userService.save(emptyUser);



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
                LocalDateTime.of(2022, 6, 1, 8, 30),
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
                LocalDateTime.of(2022, 6, 1, 16, 30),
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
                LocalDateTime.of(2022, 6, 2, 8, 30),
                false,
                5,
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
                LocalDateTime.of(2022, 6, 2, 16, 30),
                false,
                5,
                ramon,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(DriveRouteR4);


       /*-------------------------------------------------------------------------------------------------------------
                                                    Fahrtangebote - Ivonne
        -------------------------------------------------------------------------------------------------------------*/


        start = new Start(new Address("58095", "Hagen", "Diesterwegstraße", "6"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteI1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 6, 1, 8, 30),
                false,
                4,
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
                LocalDateTime.of(2022, 6, 1, 16, 30),
                false,
                4,
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
                LocalDateTime.of(2022, 4, 1, 8, 30),
                false,
                5,
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
                LocalDateTime.of(2022, 4, 1, 16, 30),
                false,
                5,
                ivonne,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteI4);

        RegularDrive regularDriveI5 = new RegularDrive(DayOfWeek.MONDAY, LocalDate.of(2022,4,1), LocalDate.of(2022,8, 30));

        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        destination = new Destination(new Address("58089", "Hagen", "Paschestraße", "28"));

        DriveRoute driveRouteI5 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 5, 1, 16, 30),
                true,
                1,
                ivonne,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteI5.setRegularDrive(regularDriveI5);

        driveRouteService.save(driveRouteI5);

        /*-------------------------------------------------------------------------------------------------------------
                                                    Fahrtangebote - Maren
        -------------------------------------------------------------------------------------------------------------*/

        start = new Start(new Address("58097", "Hagen", "Kratzkopfstraße", "10"));
        destination = new Destination(new Address("59872", "Meschede", "Lindenstraße", "53"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteM1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 6, 1, 8, 30),
                false,
                4,
                maren,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM1);


        start = new Start(new Address("59872", "Meschede", "Lindenstraße", "53"));
        destination = new Destination(new Address("58097", "Hagen", "Kratzkopfstraße", "10"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteM2 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 6, 1, 16, 30),
                false,
                4,
                maren,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteM2);


        start = new Start(new Address("58097", "Hagen", "Kratzkopfstraße", "10"));
        destination = new Destination(new Address("59872", "Meschede", "Lindenstraße", "53"));

        DriveRoute driveRouteM3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 8, 30),
                false,
                5,
                maren,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM3);


        start = new Start(new Address("59872", "Meschede", "Lindenstraße", "53"));
        destination = new Destination(new Address("58097", "Hagen", "Kratzkopfstraße", "10"));

        DriveRoute driveRouteM4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 16, 30),
                false,
                5,
                maren,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteM4);


        /*-------------------------------------------------------------------------------------------------------------
                                                Fahrtangebote - Sebastian
        -------------------------------------------------------------------------------------------------------------*/

        start = new Start(new Address("58840", "Plettenberg", "Eschener Weg", "45"));
        destination = new Destination(new Address("58095", "Hagen", "Haldener Str.", "128"));

        routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteS1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 6, 1, 8, 30),
                false,
                4,
                sebastian,
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
                LocalDateTime.of(2022, 6, 1, 16, 30),
                false,
                4,
                sebastian,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteS2);


        start = new Start(new Address("58840", "Plettenberg", "Eschener Weg", "45"));
        destination = new Destination(new Address("58095", "Hagen", "Haldener Str.", "128"));

        DriveRoute driveRouteS3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 8, 30),
                false,
                5,
                sebastian,
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteS3);


        start = new Start(new Address("58095", "Hagen", "Haldener Str.", "128"));
        destination = new Destination(new Address("58840", "Plettenberg", "Eschener Weg", "45"));

        DriveRoute driveRouteS4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 16, 30),
                false,
                5,
                sebastian,
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteS4);



        /*-------------------------------------------------------------------------------------------------------------
                                                      Bewertungen
        -------------------------------------------------------------------------------------------------------------*/

        Rating rating = new Rating(4, 5);

        UserRating userRating = new UserRating();

        userRating.addDriverRating(rating);

        rating = new Rating(5, 4);
        userRating.addDriverRating(rating);

        rating = new Rating(3, 2);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        ramon.setUserRating(userRating);

        userService.save(ramon);

        System.out.println(ramon.getUserRating().getAverageDriverRating());

        /*-------------------------------------------------------------------------------------------------------------
                                                      Fahrtanfragen
        -------------------------------------------------------------------------------------------------------------*/

        /*-------------------------------------------------------------------------------------------------------------
                                                  Fahrtanfragen an Ramon
        -------------------------------------------------------------------------------------------------------------*/

        DriveRequest driveRequest = new DriveRequest(
                driveRouteR3,
                ivonne,
                "Apfel Birne und so",
                "",
                new Stopover(new Address("58095", "Hagen", "Diesterwegstraße", "6"))
        );

        driveRouteR3.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR3);


        driveRequest = new DriveRequest(
                driveRouteR3,
                maren,
                "Maren",
                "",
                new Stopover(new Address("58636", "Iserlohn", "Sundernallee", "75"))
        );

        driveRouteR3.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR3);


        driveRequest = new DriveRequest(
                driveRouteR3,
                sebastian,
                "Maren",
                "",
                new Stopover(new Address("58636", "Iserlohn", "Sundernallee", "75"))
        );

        driveRouteR3.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR3);


        driveRequest = new DriveRequest(
                driveRouteI5,
                ramon,
                "regulär lel",
                "pups",
                new Stopover(new Address("58636", "Iserlohn", "Sundernallee", "75"))
        );

        driveRouteI5.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteI5);

        /**
         * BOOKING
         */

//        driveRequest.setRequestState(RequestState.ACCEPTED);
//        driveRequestService.save(driveRequest);
//        Booking newBooking = new Booking(driveRequest.getDriveRoute(), driveRequest.getPassenger(), LocalDateTime.now(), driveRequest.getStopover());
//        bookingService.save(newBooking);
//        driveRequest.getDriveRoute().addBooking(newBooking);
//        driveRouteService.save(driveRequest.getDriveRoute());


    }

}

