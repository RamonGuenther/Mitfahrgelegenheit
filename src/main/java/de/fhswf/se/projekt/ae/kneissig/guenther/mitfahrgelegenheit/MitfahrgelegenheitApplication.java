package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.theme.Theme;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.*;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
@CssImport(value = "/themes/mitfahrgelegenheit/components/petrol-buttons.css", themeFor = "vaadin-button")
public class MitfahrgelegenheitApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(MitfahrgelegenheitApplication.class, args);
    }

    @PostConstruct
    public void initData() throws DuplicateRequestException, DuplicateBookingException {

        /**
         * Benutzer
         */
        User user1 = new User(
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
                new UserRating(),
                LocalDateTime.now(),
                true
        );
        userService.save(user1);

        User user2 = new User(
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
                new UserRating(),
                LocalDateTime.now(),
                true
        );
        userService.save(user2);



        User user3 = new User(
                3L,
                "mapet001",
                passwordEncoder.encode("1234"),
                "Maren",
                "Peterson",
                new Address("58636", "Iserlohn", "Schulstraße", "95"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "peterson.maren@fh-swf.de",
                new UserRating(),
                LocalDateTime.now(),
                true
        );
        userService.save(user3);

        User user4 = new User(
                4L,
                "user4",
                passwordEncoder.encode("1234"),
                "Max",
                "Mustermann",
                new Address("58636", "Iserlohn", "Sundernallee", "75"),
                new Languages("Deutsch"),
                null,
                null,
                null,
                new UserRating(),
                LocalDateTime.now(),
                false
        );

        userService.save(user4);


        /**
         * Fahrten
         */

        Start start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"), LocalDateTime.now());
        Destination destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"), LocalDateTime.now());

//		AddressConverter converterStart = new AddressConverter(start.getAdresse().getAddress());
//		AddressConverter converterZiel = new AddressConverter(destination.getAdresse().getAddress());
        List<Stopover> test = new ArrayList<>();
        Stopover test1 = new Stopover(new Address(
                "58636",
                "Iserlohn",
                "Frauenstuhlweg",
                "31"
        ), LocalDateTime.now());
        test.add(test1);

        RouteString routeString = new RouteString(start, destination, test);

        DriveRoute driveRoute = new DriveRoute(
                start,
                destination,
                false,
                4,
                user1,
                LocalDateTime.now(),
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRoute);


        start = new Start(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"), LocalDateTime.now());
        destination = new Destination(new Address("58636", "Iserlohn", "Schulstraße", "95"), LocalDateTime.now());

        routeString = new RouteString(start, destination, test);

        DriveRoute driveRoute1 = new DriveRoute(
                start,
                destination,
                false,
                4,
                user1,
                LocalDateTime.now(),
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRoute1);


        start = new Start(new Address("58636", "Iserlohn", "Schulstraße", "95"),
                LocalDateTime.of(2022, 4, 1, 14, 30));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"
        ), LocalDateTime.of(2022, 4, 20, 14, 30));

        DriveRoute driveRoute2 = new DriveRoute(
                start,
                destination,
                false,
                5,
                user1,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRoute2);


        /**
         *  Rating
         */

        Rating rating = new Rating(LocalDate.now(), 4, 5);

        UserRating userRating = new UserRating();

        userRating.addDriverRating(rating);

        rating = new Rating(LocalDate.now(), 5, 4);
        userRating.addDriverRating(rating);

        rating = new Rating(LocalDate.now(), 3, 2);
        userRating.addDriverRating(rating);

        userRating.addDriverRating(rating);

        user1.setUserRating(userRating);

        userService.save(user1);

        System.out.println(user1.getUserRating().getAverageDriverRating());

        /**
         * REQUEST
         */

        DriveRequest driveRequest = new DriveRequest(
                driveRoute2,
                RequestState.OPEN,
                user2,
                "Apfel Birne und so",
                "",
                LocalDateTime.now(),
                new Stopover(new Address("58095", "Hagen", "Diesterwegstraße", "6"),
                        LocalDateTime.now())
        );

        driveRoute2.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRoute2);


        driveRequest = new DriveRequest(
                driveRoute2,
                RequestState.OPEN,
                user3,
                "Maren",
                "",
                LocalDateTime.now(),
                new Stopover(new Address("58636", "Iserlohn", "Sundernallee", "75"),
                        LocalDateTime.now())
        );

        driveRoute2.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRoute2);


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
