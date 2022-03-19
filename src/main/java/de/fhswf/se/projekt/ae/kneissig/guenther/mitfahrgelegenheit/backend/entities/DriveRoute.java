package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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


    public DriveRoute(Start start, Destination destination, Integer seatCount, User driver, LocalDateTime creationDate, DriveType driveType) {
        this.start = start;
        this.destination = destination;
        this.seatCount = seatCount;
        this.driver = driver;
        this.creationDate = creationDate;
        this.driveType = driveType;
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
        this.id = null;
        this.start = null;
        this.destination = null;
        this.seatCount = null;
        this.driver = null;
        this.creationDate = null;
        this.driveType = null;
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

    @Override
    public int hashCode() {
        return Objects.hash(start, destination, seatCount, driver.getId());
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
}
