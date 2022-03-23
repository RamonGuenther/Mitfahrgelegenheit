package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import com.google.maps.errors.ApiException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.UserRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.RouteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MitfahrgelegenheitApplication {

	@Autowired
	private UserService userService;

	@Autowired
	private DriveRouteService driveRouteService;

	public static void main(String[] args) {
		SpringApplication.run(MitfahrgelegenheitApplication.class, args);
	}

	@PostConstruct
	public void initData() throws MessagingException, IOException, InterruptedException, ApiException {
		User user1 = new User(
				1L,
				"rague002",
				"Ramon",
				"Günther",
				new Address("58636","Iserlohn","Sundernalle","75"),
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
				new Address("58097","Hagen","Diesterwegstraße","6"),
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
				new Address("58636","Iserlohn","Sundernallee","75"),
				new Languages("Deutsch"),
				null,
				null,
				null,
				new UserRating(),
				LocalDateTime.now(),
				false
		);

		userService.save(user3);

		Start start = new Start(new Address("58636", "Iserlohn","Sundernallee","75"), LocalDateTime.now());
		Destination destination = new Destination(new Address("58644","Iserlohn","Frauenstuhlweg","31"), LocalDateTime.now());

		AddressConverter converterStart = new AddressConverter(start.getAdresse().getAddress());
		AddressConverter converterZiel = new AddressConverter(destination.getAdresse().getAddress());

		RouteString routeString = new RouteString(
				converterStart.getStreet(),
				converterStart.getNumber(),
				converterStart.getPostalCode(),
				converterStart.getPlace(),
				converterZiel.getStreet(),
				converterZiel.getNumber(),
				converterZiel.getPostalCode(),
				converterZiel.getPlace()
		);

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

		start = new Start(new Address("58644","Iserlohn","Frauenstuhlweg","31"), LocalDateTime.now());
		destination = new Destination(new Address("58636", "Iserlohn","Schulstraße","95"), LocalDateTime.now());

		converterStart = new AddressConverter(start.getAdresse().getAddress());
		converterZiel = new AddressConverter(destination.getAdresse().getAddress());

		routeString = new RouteString(
				converterStart.getStreet(),
				converterStart.getNumber(),
				converterStart.getPostalCode(),
				converterStart.getPlace(),
				converterZiel.getStreet(),
				converterZiel.getNumber(),
				converterZiel.getPostalCode(),
				converterZiel.getPlace()
		);

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


//		GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
//
//
//		List<String> origins = new ArrayList<>();
//		origins.add("Diesterwegstraße 6, 58095 Hagen");
//        origins.add("Sundernallee 75, 58636 Iserlohn");
//        origins.add("Schulstraße 95, 58636 Iserlohn");
//        origins.add("Im Wiesengrund, 58636 Iserlohn");
//
//		String target = "Frauenstuhlweg 31, 58644 Iserlohn";
//
//		List<String> result = googleDistanceCalculation.calculate(origins, target);
//
//		for(String res : result){
//			System.out.println(res);
//		}

	}



}
