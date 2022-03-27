package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.HashedPassword;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private HashedPassword password;

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
    private final UserRating userRating;

    public User(Long id,
                String username,
                String firstName,
                String lastName,
                Address address,
                Languages languages,
                String universityLocation,
                String faculty,
                String email,
                UserRating userRating,
                LocalDateTime lastLogin,
                boolean firstLogin
                ) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.languages = languages;
        this.lastLogin = lastLogin;
        this.firstLogin = firstLogin;
        this.universityLocation = universityLocation;
        this.faculty = faculty;
        this.email = email;
        this.userRating = userRating;
    }

    public User() {
        this.id = null;
        this.username = null;
        this.firstName = null;
        this.lastName = null;
        this.address = null;
        this.languages = null;
        this.lastLogin = null;
        this.firstLogin = false;
        this.universityLocation = null;
        this.faculty = null;
        this.email = null;
        this.userRating = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getUniversityLocation() {
        return universityLocation;
    }

    public void setUniversityLocation(String universityLocation) {
        this.universityLocation = universityLocation;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return firstName + " " +lastName.charAt(0) + ". ";
    }

}
