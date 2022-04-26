package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Die Klasse User enthält alle Daten zu einem Benutzer der Applikation
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Entity
public class User {
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    private String firstName;
    private String lastName;

    @Embedded
    private Address address;
    private String email;
    private String universityLocation;
    private String faculty;

    @Embedded
    private Languages languages;
    private LocalDateTime lastLogin;
    private boolean firstLogin;

    @Embedded
    private UserRating userRating;

    private boolean isDarkMode;

    public User(Long id,
                String username,
                String password,
                String firstName,
                String lastName,
                Address address,
                Languages languages,
                String universityLocation,
                String faculty,
                String email,
                LocalDateTime lastLogin,
                boolean firstLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.universityLocation = universityLocation;
        this.faculty = faculty;
        this.languages = languages;
        this.lastLogin = lastLogin;
        this.firstLogin = firstLogin;
        this.userRating = new UserRating();
        this.isDarkMode = false;
    }

    public User() {
        lastLogin = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Address getAddress() {
        return address;
    }

    public Languages getLanguages() {
        return languages;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getUniversityLocation() {
        return universityLocation;
    }

    public String getEmail() {
        return email;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public void setUniversityLocation(String universityLocation) {
        this.universityLocation = universityLocation;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * Die Methode getFullName gibt den Vornamen und den abgekürzten Nachnamen des
     * Benutzers zurück
     *
     * @return      Vorname + gekürzter Nachname des Benutzers
     */
    public String getFullName() {
        return firstName + " " + lastName.charAt(0) + ". ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return username.equals(((User) o).username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
