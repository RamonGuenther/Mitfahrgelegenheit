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
                new UserRating(),
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
                new UserRating(),
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
                new Address("58636", "Iserlohn", "Schulstraße", "95"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "peterson.maren@fh-swf.de",
                new UserRating(),
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
                new Address("58097", "Hagen", "Kratzkopfstraße", "10"),
                new Languages("Deutsch"),
                "Iserlohn",
                "Informatik und Naturwissenschaften",
                "rapp.sebastian@fh-swf.de",
                new UserRating(),
                LocalDateTime.now(),
                false
        );

        userService.save(sebastian);



        User emptyUser = new User(
                5L,
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

        userService.save(emptyUser);



        /*-------------------------------------------------------------------------------------------------------------
                                                       Fahrtangebote
        -------------------------------------------------------------------------------------------------------------*/

       /*-------------------------------------------------------------------------------------------------------------
                                                          Ramon
        -------------------------------------------------------------------------------------------------------------*/

        Start start = new Start(new Address("58636", "Iserlohn", "Sundernallee", "75"));
        Destination destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));
        List<Stopover> stopoverList = new ArrayList<>();

        RouteString routeString = new RouteString(start, destination, stopoverList);

        DriveRoute driveRouteR1 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                false,
                4,
                ramon,
                LocalDateTime.now(),
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
                LocalDateTime.of(2022, 4, 1, 14, 30),
                false,
                4,
                ramon,
                LocalDateTime.now(),
                DriveType.RETURN_TRIP,
                routeString.getRoute()
        );


        driveRouteService.save(driveRouteR2);


        start = new Start(new Address("58636", "Iserlohn", "Schulstraße", "95"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute driveRouteR3 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                false,
                5,
                ramon,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRouteR3);


        start = new Start(new Address("58636", "Iserlohn", "Schulstraße", "95"));
        destination = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"));

        DriveRoute DriveRouteR4 = new DriveRoute(
                start,
                destination,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                false,
                5,
                ramon,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(DriveRouteR4);


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

        ramon.setUserRating(userRating);

        userService.save(ramon);

        System.out.println(ramon.getUserRating().getAverageDriverRating());

        /**
         * REQUEST
         */

        DriveRequest driveRequest = new DriveRequest(
                driveRouteR3,
                RequestState.OPEN,
                ivonne,
                "Apfel Birne und so",
                "",
                LocalDateTime.now(),
                new Stopover(new Address("58095", "Hagen", "Diesterwegstraße", "6"))
        );

        driveRouteR3.addDriveRequest(driveRequest);
        driveRequestService.save(driveRequest);
        driveRouteService.save(driveRouteR3);


//        driveRequest = new DriveRequest(
//                driveRoute2,
//                RequestState.OPEN,
//                user3,
//                "Maren",
//                "",
//                LocalDateTime.now(),
//                new Stopover(new Address("58636", "Iserlohn", "Sundernallee", "75"),
//                        LocalDateTime.now())
//        );
//
//        driveRoute2.addDriveRequest(driveRequest);
//        driveRequestService.save(driveRequest);
//        driveRouteService.save(driveRoute2);


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
