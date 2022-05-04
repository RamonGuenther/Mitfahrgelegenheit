package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Rating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Die Klasse UserService dient als Schnittstelle zwischen der Applikation und
 * der Datenbank. Sie bietet diverse Methoden zum Verwalten von Benutzern.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Die save-speichert einen Benutzer in der Datenbank. Ein bereits existierender
     * Benutzer wird aktualisiert.
     *
     * @param user Benutzer, der gespeichert werden soll
     */
    public void save(User user) {
        repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Die Methode findBenutzerByUsername sucht den Benutzer mit dem gegebenen Benutzernamen.
     *
     * @param username Benutzername, zu dem der Benutzer gesucht wird
     * @return Benutzer mit dem gegebenen Benutzernamen
     */
    public User findBenutzerByUsername(String username) {
        return repository.findByUsername(username);
    }

    /**
     * Die Methode getCurrentUser gibt den aktuell angemeldeten Benutzer zurück
     */
    public User getCurrentUser() {
        return repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Die Methode rateUser fügt zu einem Benutzer eine neue Bewertung hinzu.
     *
     * @param userToRate        Benutzer der bewertet werden soll
     * @param ratingPunctuality Pünktlichkeits-bewertung
     * @param ratingReliability Zuverlässigkeits-Bewertung
     * @param role              Rolle des zu bewertenden Benutzers
     */
    public void rateUser(User userToRate, int ratingPunctuality, int ratingReliability, Role role) {
        Rating rating = new Rating(ratingPunctuality, ratingReliability);
        switch (role) {
            case DRIVER:
                userToRate.getUserRating().addDriverRating(rating);
                break;
            case PASSENGER:
                userToRate.getUserRating().addPassengerRating(rating);
                break;
        }
        save(userToRate);
    }
}
