package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Rating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public void save(User user){
        repository.save(user);
    }

    public User findBenutzerByUsername(String username){
        return repository.findByUsername(username);
    }

    public User getCurrentUser(){
        return repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void rateUser(User userToRate, int ratingPuncuality, int ratingReliability, Role role){
        Rating rating = new Rating(LocalDate.now(),ratingPuncuality, ratingReliability);

        switch (role){
            case DRIVER -> userToRate.getUserRating().addDriverRating(rating);
            case PASSENGER -> userToRate.getUserRating().addPassengerRating(rating);
        }

        save(userToRate);
    }
}
