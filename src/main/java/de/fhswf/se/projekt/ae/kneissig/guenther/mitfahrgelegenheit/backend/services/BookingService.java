package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public void save(Booking newBooking){
        repository.save(newBooking);
    }

    public void delete(Booking newBooking){
        repository.delete(newBooking);
    }

    public List<Booking> findAllByPassengerAndDriveRoute_DriveType(User user, DriveType driveType){
        return repository.findAllByPassengerAndDriveRoute_DriveType(user, driveType);
    }
}
