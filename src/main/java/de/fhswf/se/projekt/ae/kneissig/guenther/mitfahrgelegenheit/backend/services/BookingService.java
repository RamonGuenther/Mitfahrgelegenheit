package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public void save(Booking newBooking){
        repository.save(newBooking);
    }

    public void delete(Booking booking){
        repository.delete(booking);
    }

    public List<Booking> getAllByPassengerAndDriveType(User user, DriveType driveType){
        return repository.findAllByPassengerAndDriveRoute_DriveType(user, driveType);
    }

    public Booking getNextBookingByUser(User user){
        List<Booking> bookings = repository.findAllByPassenger(user).orElse(Collections.emptyList());
        bookings.sort(Comparator.comparing(booking -> booking.getDriveRoute().getDrivingTime()));
        bookings = bookings.stream().filter(booking ->
                booking.getDriveRoute().getDrivingTime().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()) ||
                booking.getDriveRoute().getDrivingTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()) &&
                booking.getDriveRoute().getDrivingTime().toLocalTime().isAfter(LocalDateTime.now().toLocalTime())).collect(Collectors.toList());

        return bookings.size() > 0 ? bookings.get(0) : null;
    }

    public Optional<List<Booking>> getCompletedDriveRoutesByDriver(User user){
        return repository.findAllByDriverAndDrivingTime(user, LocalDateTime.now());
    }

    public Optional<List<Booking>> getCompletedDriveRoutesByPassenger(User user){
        return repository.findAllByPassengerAndDrivingTime(user, LocalDateTime.now());
    }
}
