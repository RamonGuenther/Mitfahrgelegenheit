package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Adresse;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Sprachen;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BenutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootApplication
public class MitfahrgelegenheitApplication {

	@Autowired
	private BenutzerService benutzerService;

	public static void main(String[] args) {
		SpringApplication.run(MitfahrgelegenheitApplication.class, args);
	}

	@PostConstruct
	public void initData(){
		Benutzer benutzer1 = new Benutzer(1L,"user",new Adresse("58636","Iserlohn","Sundernallee","75"),new Sprachen("deutsch"), LocalDateTime.now());
		benutzerService.save(benutzer1);

		Benutzer benutzer2 = new Benutzer(2L,"user2",new Adresse("58636","Iserlohn","Sundernallee","75"),new Sprachen("deutsch"), LocalDateTime.now());
		benutzerService.save(benutzer2);

		Benutzer benutzer3 = new Benutzer(3L,"user3",new Adresse("58636","Iserlohn","Sundernallee","75"),new Sprachen("deutsch"), LocalDateTime.now());
		benutzerService.save(benutzer3);

	}

}
