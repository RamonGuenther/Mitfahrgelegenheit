package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateBookingException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;

@Entity
public class DriveRoute {
    @Id
    private Integer id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "time", column = @Column(name = "start_zeit")),
            @AttributeOverride(name = "address.postal", column = @Column(name = "start_plz")),
            @AttributeOverride(name = "address.street", column = @Column(name = "start_strasse")),
            @AttributeOverride(name = "address.place", column = @Column(name = "start_ort")),
            @AttributeOverride(name = "address.houseNumber", column = @Column(name = "start_hausnummer"))
    })
    private Start start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "time", column = @Column(name = "ziel_zeit")),
            @AttributeOverride(name = "address.postal", column = @Column(name = "ziel_plz")),
            @AttributeOverride(name = "address.street", column = @Column(name = "ziel_strasse")),
            @AttributeOverride(name = "address.place", column = @Column(name = "ziel_ort")),
            @AttributeOverride(name = "address.houseNumber", column = @Column(name = "ziel_hausnummer"))
    })
    private Destination destination;

    private Integer seatCount;

    @ManyToOne(cascade = CascadeType.ALL)
    private User driver;

    private LocalDateTime creationDate;

    private DriveType driveType;

    private boolean fuelParticipation;

    private DayOfWeek regularDriveDay;

    private String note;

    private String currentRouteLink;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<DriveRequest> driveRequests;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    public DriveRoute(Start start, Destination destination, Integer seatCount, User driver, LocalDateTime creationDate, DriveType driveType, String currentRouteLink) {
        this.start = start;
        this.destination = destination;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
        this.currentRouteLink = currentRouteLink;
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
        id = hashCode();
    }

    public DriveRoute(Start start, Destination destination, Integer seatCount, User driver, LocalDateTime creationDate, DriveType driveType, String currentRouteLink, Set<DriveRequest> driveRequests) {
        this.start = start;
        this.destination = destination;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
        this.currentRouteLink = currentRouteLink;
        this.driveRequests = new HashSet<>(driveRequests);
        bookings = new HashSet<>();
        id = hashCode();
    }

    public DriveRoute(
            Integer id,
            Start start,
            Destination destination,
            Integer seatCount,
            User driver,
            LocalDateTime creationDate,
            DriveType driveType
    ) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
    }

    @PersistenceConstructor
    public DriveRoute() {
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
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

    public DayOfWeek getRegularDriveDay() {
        return regularDriveDay;
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

    public User getBenutzer() {
        return driver;
    }

    public void addDriveRequest(DriveRequest driveRequest) throws DuplicateRequestException {
        nullCheck(driveRequest);

        if (driveRequests.contains(driveRequest))
            throw new DuplicateRequestException();

        driveRequests.add(driveRequest);
    }

    public void addBooking(Booking newBooking) throws DuplicateBookingException {
        nullCheck(newBooking);

        if (bookings.contains(newBooking))
            throw new DuplicateBookingException();

        bookings.add(newBooking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, destination, seatCount, driver.getId());
    }
}