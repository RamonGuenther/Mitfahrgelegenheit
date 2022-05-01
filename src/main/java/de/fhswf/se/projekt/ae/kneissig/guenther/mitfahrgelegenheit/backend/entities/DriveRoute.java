package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateBookingException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;

/**
 * Die Klasse DriveRoute repräsentiert ein Fahrtangebot eine Benutzers. Zu den Angaben der Fahrtangebots
 * werden zusätzlich auch alle Anfragen und Buchungen zu dieser Fahrt gespeichert.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Entity
public class DriveRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address.postal", column = @Column(name = "start_plz")),
            @AttributeOverride(name = "address.street", column = @Column(name = "start_strasse")),
            @AttributeOverride(name = "address.place", column = @Column(name = "start_ort")),
            @AttributeOverride(name = "address.houseNumber", column = @Column(name = "start_hausnummer"))
    })
    private Start start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address.postal", column = @Column(name = "ziel_plz")),
            @AttributeOverride(name = "address.street", column = @Column(name = "ziel_strasse")),
            @AttributeOverride(name = "address.place", column = @Column(name = "ziel_ort")),
            @AttributeOverride(name = "address.houseNumber", column = @Column(name = "ziel_hausnummer"))
    })
    private Destination destination;

    private Integer seatCount;

    @ManyToOne
    private User driver;

    private LocalDateTime creationDate;

    private LocalDateTime drivingTime;

    private DriveType driveType;

    private boolean fuelParticipation;

    @Embedded
    private RegularDrive regularDrive;

    private String note;

    private String currentRouteLink;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driveRoute", cascade = {CascadeType.REMOVE})
    private Set<DriveRequest> driveRequests;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driveRoute", cascade = CascadeType.REMOVE)
    private Set<Booking> bookings;

    /**
     * Konstruktor zum Erstellen eines Fahrtagebots.
     *
     * @param start                 Startadresse der Fahrt
     * @param destination           Zieladresse der Fahrt
     * @param drivingTime           Datum und Uhrzeit der Fahrt
     * @param fuelParticipation     Spritbeteiligung gewünscht
     * @param seatCount             Anzahl der Sitzplätze
     * @param driver                Fahrer
     * @param driveType             Fahrtentyp
     */
    public DriveRoute(Start start,
                      Destination destination,
                      LocalDateTime drivingTime,
                      boolean fuelParticipation,
                      Integer seatCount,
                      User driver,
                      DriveType driveType,
                      String currentRouteLink) {

        nullCheck(start, destination, drivingTime, seatCount, driver);

        this.start = start;
        this.destination = destination;
        this.drivingTime = drivingTime;
        this.fuelParticipation = fuelParticipation;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = LocalDateTime.now();
        this.currentRouteLink = currentRouteLink;
        this.driveType = driveType;
        this.note = "";
        this.regularDrive = null;
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
    }

    /**
     * Der Konstruktor ist zur Aktualisierung eines bestehenden
     * Fahrtangebots.
     *
     * @param id                    ID des Fahrtangebots
     * @param start                 Startadresse der Fahrt
     * @param destination           Zieladresse der Fahrt
     * @param drivingTime           Datum und Uhrzeit der Fahrt
     * @param fuelParticipation     Spritbeteiligung gewünscht
     * @param seatCount             Anzahl der Sitzplätze
     * @param driver                Fahrer
     * @param creationDate          Datum/Uhrzeit der Fahrterstellung
     * @param driveType             Fahrtentyp
     * @param note                  Notiz
     */
    public DriveRoute(
            Long id,
            Start start,
            Destination destination,
            LocalDateTime drivingTime,
            boolean fuelParticipation,
            Integer seatCount,
            User driver,
            LocalDateTime creationDate,
            DriveType driveType,
            String note
    ) {
        nullCheck(start, destination, drivingTime, seatCount, driver);

        this.id = id;
        this.start = start;
        this.destination = destination;
        this.fuelParticipation = fuelParticipation;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.drivingTime = drivingTime;
        this.driveType = driveType;
        this.note = note;
    }

    @PersistenceConstructor
    public DriveRoute() {

    }

    public Long getId() {
        return id;
    }

    public Start getStart() {
        return start;
    }

    public Destination getDestination() {
        return destination;
    }

    public User getDriver() {
        return driver;
    }

    public boolean isFuelParticipation() {
        return fuelParticipation;
    }

    public String getNote() {
        return note;
    }

    public String getCurrentRouteLink() {
        return currentRouteLink;
    }

    public List<DriveRequest> getDriveRequests() {
        return driveRequests.stream().toList();
    }

    public void setCurrentRouteLink(String currentRouteLink) {
        this.currentRouteLink = currentRouteLink;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public List<Booking> getBookings() {
        return bookings.stream().toList();
    }

    public LocalDateTime getDrivingTime() {
        return drivingTime;
    }

    public RegularDrive getRegularDrive() {
        return regularDrive;
    }

    public void setRegularDrive(RegularDrive regularDrive) {
        this.regularDrive = regularDrive;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return drivingTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return drivingTime.format(formatter) + " Uhr";
    }

    /**
     * Die Methode addDriveRequest fügt Anfragenliste der Fahrt eine Fahrtanfrage hinzu.
     *
     * @param newDriveRequest               Anfrage zur Fahrt
     * @throws DuplicateRequestException    Anfrage wurde bereits gestellt
     */
    public void addDriveRequest(DriveRequest newDriveRequest) throws DuplicateRequestException {
        nullCheck(newDriveRequest);

        Optional<DriveRequest> any = driveRequests.stream().filter(r -> r.getPassenger().equals(newDriveRequest.getPassenger()) &&
                r.getDriveRoute().equals(newDriveRequest.getDriveRoute())).findAny();

        if (any.isPresent()) {
            throw new DuplicateRequestException("Eine Anfrage für diese Fahrt wurde bereits gestellt.");
        }

        driveRequests.add(newDriveRequest);
    }

    /**
     * Die Methode removeDriveRequest entfernt eine Fahrtanfrage aus der Anfragenliste der Fahrt.
     *
     * @param driveRequest              Anfrage die entfernt werden soll
     */
    public void removeDriveRequest(DriveRequest driveRequest) {
        nullCheck(driveRequest);
        System.out.println("Vor delete: " + driveRequests.size());
        driveRequests.remove(driveRequest);
        System.out.println("Nach delete: " + driveRequests.size());
    }

    /**
     * Die Methode addBooking fügt der Buchungsliste der Fahrt eine neue Buchung hinzu.
     *
     * @param newBooking                    Buchung eines Nutzers
     * @throws DuplicateBookingException    Buchung bereits in der Liste vorhanden
     */
    public void addBooking(Booking newBooking) throws DuplicateBookingException {
        nullCheck(newBooking);

        Optional<Booking> any = bookings.stream().filter(b -> b.getPassenger().equals(newBooking.getPassenger()) &&
                b.getDriveRoute().equals(newBooking.getDriveRoute())).findAny();

        if (any.isPresent())
            throw new DuplicateBookingException("Mitfahrer wurde schon für das Fahrtangebot angenommen.");

        bookings.add(newBooking);
    }

    /**
     * Die Methode removeBooking entfernt eine Buchung aus der Buchungsliste einer Fahrt.
     *
     * @param booking                   Buchung, die aus der Buchungsliste entfernt werden soll
     */
    public void removeBooking(Booking booking) {
        nullCheck(booking);
        System.out.println("Vor delete: " + bookings.size());
        bookings.remove(booking);
        System.out.println("Nach delete: " + this.bookings.size());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DriveRoute && id.equals(((DriveRoute) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, destination, driver, creationDate);
    }
}
