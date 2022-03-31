package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

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
                UserRating userRating,
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
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return firstLogin == user.firstLogin && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(address, user.address) && Objects.equals(email, user.email) && Objects.equals(universityLocation, user.universityLocation) && Objects.equals(faculty, user.faculty) && Objects.equals(languages, user.languages) && Objects.equals(lastLogin, user.lastLogin) && Objects.equals(userRating, user.userRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, lastName, address, email, universityLocation, faculty, languages, lastLogin, firstLogin, userRating);
    }
}
