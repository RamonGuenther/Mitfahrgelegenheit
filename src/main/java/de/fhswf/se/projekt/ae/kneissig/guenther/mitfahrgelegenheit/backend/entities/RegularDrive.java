package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Die Klasse RegularDrive enthält die Daten, die zum Erstellen einer regelmäßigen
 * Fahrt zusätzlich benötigt werden.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */

@Embeddable
public class RegularDrive {

    private DayOfWeek regularDriveDay;

    private LocalDate regularDriveDateEnd;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private Set<LocalDate> driveDates;

    public RegularDrive(DayOfWeek regularDriveDay, LocalDate regularDriveDateStart, LocalDate regularDriveDateEnd) {
        this.regularDriveDay = regularDriveDay;
        this.regularDriveDateEnd = regularDriveDateEnd;
        this.driveDates = new HashSet<>();
        calculateDriveDates(regularDriveDateStart);
    }

    public RegularDrive() {

    }

    public DayOfWeek getRegularDriveDay() {
        return regularDriveDay;
    }

    public void setRegularDriveDay(DayOfWeek regularDriveDay) {
        this.regularDriveDay = regularDriveDay;
    }

    public LocalDate getRegularDriveDateEnd() {
        return regularDriveDateEnd;
    }

    public void setRegularDriveDateEnd(LocalDate regularDriveDateEnd) {
        this.regularDriveDateEnd = regularDriveDateEnd;
    }

    public Set<LocalDate> getDriveDates() {
        return driveDates;
    }

    public void setDriveDates(Set<LocalDate> driveDates) {
        this.driveDates = driveDates;
    }

    private void calculateDriveDates(LocalDate regularDriveDateStart){
        LocalDate currentDate = regularDriveDateStart;

        while (!currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY).equals(regularDriveDay.label)){
            currentDate = currentDate.plusDays(1);
        }

        while(currentDate.isBefore(regularDriveDateEnd)){
            driveDates.add(currentDate);
            currentDate = currentDate.plusDays(7);
        }
    }
}
