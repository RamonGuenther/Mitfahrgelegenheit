package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public void delete(Booking booking){
        repository.delete(booking);
    }

    public List<Booking> findAllByPassengerAndDriveRoute_DriveType(User user, DriveType driveType){
        return repository.findAllByPassengerAndDriveRoute_DriveType(user, driveType);
    }

    public Booking findNextBookingByUserComparedByTime(User user){
        List<Booking> bookings = repository.findAllByPassenger(user);
        bookings.sort(Comparator.comparing(booking -> booking.getDriveRoute().getDrivingTime()));

        if(bookings.size() > 0){
            return bookings.get(0);
        }
        else{
            return null;
        }
    }
}
