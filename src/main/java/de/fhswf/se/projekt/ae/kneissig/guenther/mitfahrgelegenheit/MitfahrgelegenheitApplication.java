package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.email.MailSender;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.UserRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class MitfahrgelegenheitApplication {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(MitfahrgelegenheitApplication.class, args);
	}

	@PostConstruct
	public void initData(){
		User user1 = new User(
				1L,
				"rague002",
				new Address("58636","Iserlohn","Sundernalle 47","75"),
				new Languages("deutsch"),
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
				new Address("58097","Hagen","Diesterwegstraße","6"),
				new Languages("deutsch"),
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
				new Address("58636","Iserlohn","Sundernallee","75"),
				new Languages("deutsch"),
				null,
				null,
				null,
				new UserRating(),
				LocalDateTime.now(),
				false
		);
		userService.save(user3);
	}



}
