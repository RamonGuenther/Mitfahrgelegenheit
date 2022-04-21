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

@Entity
public class DriveRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    //FIXME Driving time überprüfung ob Vergangenheit auch bei Edit
    public DriveRoute(Start start, Destination destination, LocalDateTime drivingTime, boolean fuelParticipation, Integer seatCount, User driver,
                      DriveType driveType, String currentRouteLink) {

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

    public DriveRoute(Start start, Destination destination, LocalDateTime drivingTime, boolean fuelParticipation, Integer seatCount, User driver,
                      DriveType driveType, String currentRouteLink, RegularDrive regularDrive) {

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
        this.regularDrive = regularDrive;
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
    }

    //TODO: Update Konstruktor -> vllt doch über setter? oder überall über Konstruktor
    public DriveRoute(
            Integer id,
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

    public Integer getId() {
        return id;
    }

    public Start getStart() {
        return start;
    }

    public Destination getZiel() {
        return destination;
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

    public void addDriveRequest(DriveRequest newDriveRequest) throws DuplicateRequestException {
        nullCheck(newDriveRequest);

        Optional<DriveRequest> any = driveRequests.stream().filter(r -> r.getPassenger().equals(newDriveRequest.getPassenger()) &&
                r.getDriveRoute().equals(newDriveRequest.getDriveRoute())).findAny();

        if (any.isPresent()) {
            throw new DuplicateRequestException();
        }

        driveRequests.add(newDriveRequest);
    }

    public void setRegularDrive(RegularDrive regularDrive) {
        this.regularDrive = regularDrive;
    }

    public void removeDriveRequest(DriveRequest driveRequest) {
        nullCheck(driveRequest);
        System.out.println("Vor delete: " + driveRequests.size());
        driveRequests.remove(driveRequest);
        System.out.println("Nach delete: " + driveRequests.size());
    }

    public void addBooking(Booking newBooking) throws DuplicateBookingException {
        nullCheck(newBooking);

        Optional<Booking> any = bookings.stream().filter(b -> b.getPassenger().equals(newBooking.getPassenger()) &&
                b.getDriveRoute().equals(newBooking.getDriveRoute())).findAny();

        if (any.isPresent())
            throw new DuplicateBookingException();

        bookings.add(newBooking);
    }

    public void removeBooking(Booking booking) {
        nullCheck(booking);
        System.out.println("Vor delete: " + bookings.size());
        bookings.remove(booking);
        System.out.println("Nach delete: " + this.bookings.size());
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return drivingTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return drivingTime.format(formatter) + " Uhr";
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
