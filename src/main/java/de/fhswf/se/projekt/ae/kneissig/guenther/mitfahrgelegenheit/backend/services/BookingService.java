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

    /**
     * Die save-Methode speichert eine Buchung in der Datenbank. Eine bereits existierende
     * Buchung wird aktualisiert.
     *
     * @param newBooking        Buchung, die gespeichert werden soll
     */
    public void save(Booking newBooking){
        repository.save(newBooking);
    }

    /**
     * Die delete-Methode löscht eine Buchung aus der Datenbank.
     *
     * @param booking           Buchung, die gelöscht werden soll
     */
    public void delete(Booking booking){
        repository.delete(booking);
    }

    /**
     * Die Methode getAllByPassengerAndDriveType holt alle Buchungen zu einem Benutzer
     * aus der Datenbank, die dem gegebenen Fahrtentyp entsprechen.
     *
     * @param user              Benuter, dessen Buchungen gesucht werden
     * @param driveType         Fahrtentyp, nach dem gefiltert wird
     * @return                  Liste von Buchungen
     */
    public Optional<List<Booking>> getAllByPassengerAndDriveType(User user, DriveType driveType){
        return repository.findAllByPassengerAndDriveRoute_DriveType(user, driveType);
    }

    /**
     * Die Methode getNextSingleDriveBookingByPassenger gibt, falls vorhanden, die Buchung der
     * nächsten Einzelfahrt zurück, an welcher der gegebene Benutzer als Mitfahrer teilnimmt.
     *
     * @param user      Benutzer, dessen Buchung seiner nächsten Fahrt gesucht wird
     * @return          Nächste Fahrt des Benutzers
     */
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

    /**
     * Die Methode getNextRegularDriveBookingByPassenger gibt, falls vorhanden, die Buchung der
     * nächsten regelmäßigen Fahrt zurück, an welcher der gegebene Benutzer als Mitfahrer teilnimmt.
     *
     * @param user      Benutzer, dessen Buchung seiner nächsten Fahrt gesucht wird
     * @return          Nächste Fahrt des Benutzers
     */
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

    /**
     * Die Methode getCompletedDriveRoutesByDriver gibt alle Buchungen zu abgeschlossenen Fahrten
     * eines Benutzers in der Rolle als Fahrer zurück.
     *
     * @param user          Benutzer, dessen abgeschlossenen Fahrten gesucht werden
     * @return              Liste mit Buchungen der abgeschlossenen Fahrten des Fahrers
     */
    public Optional<List<Booking>> getCompletedDriveRoutesByDriver(User user){
        return repository.findAllByDriverAndDrivingTimeAndRatedByDriver(user, LocalDateTime.now());
    }

    /**
     * Die Methode getCompletedDriveRoutesByPassenger gibt alle Buchungen zu abgeschlossenen Fahrten
     * eines Benutzers in der Rolle als Mitfahrer zurück.
     *
     * @param user          Benutzer, dessen abgeschlossenen Fahrten gesucht werden
     * @return              Liste mit Buchungen der abgeschlossenen Fahrten eines Mitfahrers
     */
    public Optional<List<Booking>> getCompletedDriveRoutesByPassenger(User user){
        return repository.findAllByPassengerAndDrivingTimeAndRatedByPassenger(user, LocalDateTime.now());
    }
}
