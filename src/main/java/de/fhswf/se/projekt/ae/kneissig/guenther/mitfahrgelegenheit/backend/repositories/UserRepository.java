package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Das UserRepository ist für die Datenbankabfragen der User-Tabelle zuständig. Die
 * Tabelle speichert die Benutzerdaten.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
