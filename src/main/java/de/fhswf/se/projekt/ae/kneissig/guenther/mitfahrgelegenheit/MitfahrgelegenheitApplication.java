package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class MitfahrgelegenheitApplication {

    @Autowired
    private UserService userService;

    @Autowired
    private DriveRouteService driveRouteService;

    public static void main(String[] args) {
        SpringApplication.run(MitfahrgelegenheitApplication.class, args);
    }

    @PostConstruct
    public void initData() throws IOException, InterruptedException, ApiException, DuplicateRequestException {
        User user1 = new User(
                1L,
                "rague002",
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
                "user3",
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

        userService.save(user3);

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
        ), LocalDateTime.of(2022, 4, 1, 14, 30));

        DriveRoute driveRoute2 = new DriveRoute(
                start,
                destination,
                5,
                user1,
                LocalDateTime.of(2022, 4, 1, 14, 30),
                DriveType.OUTWARD_TRIP,
                routeString.getRoute()
        );

        driveRouteService.save(driveRoute2);
        
        /**
         * TODO: Rating
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
         * TODO: GOOGLE DISTANCE BEISPIEL
         */

//        GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
//
//
//		List<String> origins = new ArrayList<>();
//        origins.add("Diesterwegstraße 6, 58095 Hagen, Deutschland");
//        origins.add("Sundernallee 75, 58636 Iserlohn, Deutschland");
//        origins.add("Schulstraße 95, 58636 Iserlohn, Deutschland");
//        origins.add("Im Wiesengrund, 58636 Iserlohn, Deutschland");
//
//		String target = "Frauenstuhlweg 31, 58644 Iserlohn, Deutschland";
//
//		List<String> result = googleDistanceCalculation.calculate(origins, target);
//
//
//        List<Stopover> stopoverList = new ArrayList<>();
//
//        for(String res : result){
//            AddressConverter addressConverter = new AddressConverter(res);
//            stopoverList.add(new Stopover(new Address(addressConverter.getPostalCode(),addressConverter.getPlace(),addressConverter.getStreet(), addressConverter.getNumber()), LocalDateTime.now()));
//		}
//
//        Start start1 = new Start(new Address("58095", "Hagen","Diesterwegstraße","6"), LocalDateTime.now());
//        Destination destination1 = new Destination(new Address("58644", "Iserlohn", "Frauenstuhlweg", "31"),LocalDateTime.now());
//
//
//        RouteString routeString4 = new RouteString(start1, destination1, stopoverList);
//
//        System.out.println(routeString4.getRoute());


        DriveRequest driveRequest = new DriveRequest(RequestState.OPEN,user2,"","", LocalDateTime.now(), new Stopover(new Address(), null));
        driveRoute.addDriveRequest(driveRequest);
        driveRouteService.save(driveRoute);

    }


}
