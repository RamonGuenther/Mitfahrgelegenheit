package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FahrerRouteRepository extends JpaRepository<FahrerRoute, Integer> {

    List<FahrerRoute> findAllByBenutzer(Benutzer benutzer);
    List<FahrerRoute> findAllByBenutzerAndFahrtenTyp(Benutzer benutzer, DriveType fahrtenTyp);
    List<FahrerRoute> findAllByFahrtenTypAndStart_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String startPlace, String benutzerUsername);
    List<FahrerRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndBenutzerUsernameNot(DriveType driveType, String destinationPlace, String benutzerUsername);
    List<FahrerRoute> findAllByFahrtenTypAndZiel_Adresse_OrtAndStart_Adresse_OrtAndBenutzerUsernameNot(
            DriveType driveType, String destinationPlace, String startPlace, String benutzerUsername);
}
