package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Die Klasse BookingService dient als Schnittstelle zwischen der Applikation und
 * der Datenbank. Sie bietet diverse Methoden zum Verwalten von Buchungen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
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

    public Optional<List<Booking>> getAllByPassengerAndDriveType(User user, DriveType driveType){
        return repository.findAllByPassengerAndDriveRoute_DriveType(user, driveType);
    }

    public Optional<Booking> getNextSingleDriveBookingByPassenger(User user){
        List<Booking> bookings = repository.findAllByPassenger(user).orElse(Collections.emptyList());

        bookings = bookings.stream().filter(booking ->
                booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() == null &&                           // keine regelmäßige Fahrt
                booking.getDriveRoute().getDrivingTime().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()) ||    // und Datum nach aktuellem Datum
                booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() == null &&                           // oder keine regelmäßige Fahrt
                booking.getDriveRoute().getDrivingTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()) &&     // und Datum = heutiges Datum
                booking.getDriveRoute().getDrivingTime().toLocalTime().isAfter(LocalDateTime.now().toLocalTime()) ||    // und Zeit > aktuelle Zeit
                booking.getRegularDriveSingleDriveDate() != null &&                                                     // oder regelmäßige Fahrt
                booking.getRegularDriveSingleDriveDate().isAfter(LocalDate.now()) ||                                    // und Tag der einzelnen Mitfahrt nach aktuellem Datum
                booking.getRegularDriveSingleDriveDate() != null &&                                                     // oder regelmäßige Fahrt
                booking.getRegularDriveSingleDriveDate().equals(LocalDate.now()) &&                                     // und Datum = heutiges Datum
                booking.getDriveRoute().getDrivingTime().toLocalTime().isAfter(LocalTime.now()))                        // Zeit > aktuelle Zeit
                .collect(Collectors.toList());

        bookings.sort(Comparator.comparing(booking -> booking.getRegularDriveSingleDriveDate() == null ?
                booking.getDriveRoute().getDrivingTime() :
                LocalDateTime.of(booking.getRegularDriveSingleDriveDate(), booking.getDriveRoute().getDrivingTime().toLocalTime())));

        return bookings.size() > 0 ? Optional.of(bookings.get(0)) : Optional.empty();
    }

    public Optional<Booking> getNextRegularDriveBookingByPassenger(User user){
        List<Booking> bookings = repository.findAllByPassengerAndDriveRoute_RegularDrive_RegularDriveDateEnd_IsNotNull(user).orElse(Collections.emptyList());

        bookings = bookings.stream().filter(booking ->
                booking.getRegularDriveSingleDriveDate() == null &&
                booking.getDriveRoute().getDrivingTime().toLocalDate().equals(LocalDate.now()) &&
                booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now()) ||
                booking.getRegularDriveSingleDriveDate() == null &&
                booking.getDriveRoute().getDrivingTime().toLocalDate().isAfter(LocalDate.now()) &&
                booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd().isAfter(LocalDate.now())
        ).collect(Collectors.toList());

        bookings.sort(Comparator.comparing(booking -> booking.getDriveRoute().getRegularDrive().getRegularDriveDay()));

        return bookings.size() > 0 ? Optional.of(bookings.get(0)) : Optional.empty();
    }

    public Optional<List<Booking>> getCompletedDriveRoutesByDriver(User user){
        return repository.findAllByDriverAndDrivingTimeAndRatedByDriver(user, LocalDateTime.now());
    }

    public Optional<List<Booking>> getCompletedDriveRoutesByPassenger(User user){
        return repository.findAllByPassengerAndDrivingTimeAndRatedByPassenger(user, LocalDateTime.now());
    }
}
