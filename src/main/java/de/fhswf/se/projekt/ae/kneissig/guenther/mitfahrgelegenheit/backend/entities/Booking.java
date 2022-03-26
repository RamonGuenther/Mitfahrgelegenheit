package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Embeddable
public class Booking {

    @ManyToOne
    private final User mitfahrer;

    private final LocalDateTime dateOfBooking;

    public Booking(User mitfahrer, LocalDateTime dateOfBooking)
    {
        this.mitfahrer = mitfahrer;
        this.dateOfBooking = dateOfBooking;
    }

    public Booking() {
        mitfahrer = null;
        dateOfBooking = null;
    }


    public User getMitfahrer()
    {
        return mitfahrer;
    }


    public LocalDateTime getDateOfBooking()
    {
        return dateOfBooking;
    }
}
