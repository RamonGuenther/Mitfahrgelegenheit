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
import java.time.format.DateTimeFormatter;
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

    @ManyToOne
    private User driver;

    private LocalDateTime creationDate;

    private DriveType driveType;

    private boolean fuelParticipation;

    private boolean isRegularDrive;

    private DayOfWeek regularDriveDay;

    private LocalDateTime regularDriveDateEnd;

    private String note;

    private String currentRouteLink;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driveRoute", cascade = {CascadeType.REMOVE})
    private Set<DriveRequest> driveRequests;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driveRoute", cascade = CascadeType.REMOVE)
    private Set<Booking> bookings;

    public DriveRoute(Start start, Destination destination, boolean fuelParticipation, Integer seatCount, User driver,
            LocalDateTime creationDate, DriveType driveType, String currentRouteLink) {

        this.start = start;
        this.note = "";
        this.destination = destination;
        this.fuelParticipation = fuelParticipation;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
        this.currentRouteLink = currentRouteLink;
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
        id = hashCode();
    }

    //TODO: Regelmäßige Fahrt Konstruktor brauch ich den überhaupt oder kann ich den hier einfach für alles nutzen und bei null wird halt null einegtragen? XD
    public DriveRoute(Start start, Destination destination, boolean isRegularDrive, LocalDateTime regularDriveDateEnd,
                      DayOfWeek regularDriveDay, boolean fuelParticipation, Integer seatCount, User driver,
                      LocalDateTime creationDate, DriveType driveType, String currentRouteLink) {

        this.start = start;
        this.destination = destination;
        this.isRegularDrive = isRegularDrive;
        this.regularDriveDateEnd = regularDriveDateEnd;
        this.regularDriveDay = regularDriveDay;
        this.fuelParticipation = fuelParticipation;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
        this.currentRouteLink = currentRouteLink;
        driveRequests = new HashSet<>();
        bookings = new HashSet<>();
        id = hashCode();
    }


    //TODO: Update Konstruktor -> vllt doch über setter? oder überall über Konstruktor
    public DriveRoute(
            Integer id,
            Start start,
            Destination destination,
            boolean fuelParticipation,
            Integer seatCount,
            User driver,
            LocalDateTime creationDate,
            DriveType driveType,
            String note
    ) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.fuelParticipation = fuelParticipation;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
        this.note = note;
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

    public boolean isRegularDrive() {
        return isRegularDrive;
    }

    public LocalDateTime getRegularDriveDateEnd() {
        return regularDriveDateEnd;
    }

    public List<Booking> getBookings() {
        return bookings.stream().toList();
    }

    public void addDriveRequest(DriveRequest driveRequest) throws DuplicateRequestException {
        nullCheck(driveRequest);

        if (driveRequests.contains(driveRequest))
            throw new DuplicateRequestException();

        driveRequests.add(driveRequest);
    }

    //FIXME
    public void removeDriveRequest(DriveRequest driveRequest) {
        nullCheck(driveRequest);

        System.out.println("Vor delete: " + driveRequests.size());


        List<DriveRequest> driveRequestList = new ArrayList<>(driveRequests);

        for (int i = 0; i < driveRequestList.size(); i++) {
            if (Objects.equals(driveRequestList.get(i).getId(), driveRequest.getId())) {
                driveRequestList.remove(i);
                break;
            }
        }

        driveRequests = new HashSet<>(driveRequestList);
//        driveRequests.remove(driveRequest);
        System.out.println("Nach delete: " + driveRequests.size());
    }

    public void addBooking(Booking newBooking) throws DuplicateBookingException {
        nullCheck(newBooking);

        if (bookings.contains(newBooking))
            throw new DuplicateBookingException();

        bookings.add(newBooking);
    }

    //FIXME
    public void removeBooking(Booking booking) {
        nullCheck(booking);

        System.out.println("Vor delete: " + bookings.size());

        List<Booking> bookings = new ArrayList<>(this.bookings);

        for (int i = 0; i < bookings.size(); i++) {
            if (Objects.equals(bookings.get(i).getId(), booking.getId())) {
                bookings.remove(i);
                break;
            }
        }

        this.bookings = new HashSet<>(bookings);
//        bookings.remove(booking);
        System.out.println("Nach delete: " + this.bookings.size());
    }

    public void deleteRequestsAndBookings(){
        driveRequests.clear();
        bookings.clear();
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
