package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class Booking {

    @ManyToOne
    private User passenger;

    private final LocalDateTime dateOfBooking;

    @Embedded
    private Stopover stopover;

    public Booking(User passenger, LocalDateTime dateOfBooking, Stopover stopover)
    {
        this.passenger = passenger;
        this.dateOfBooking = dateOfBooking;
        this.stopover = stopover;
    }

    public Booking() {
        passenger = null;
        dateOfBooking = null;
    }


    public User getPassenger()
    {
        return passenger;
    }


    public LocalDateTime getDateOfBooking()
    {
        return dateOfBooking;
    }

    public Stopover getStopover() {
        return stopover;
    }

    @Override
    public boolean equals (Object o) {
        return o instanceof Booking && passenger.getId().equals (((Booking) o).passenger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassenger().getId().hashCode());
    }
}
