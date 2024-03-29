package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.shared.Registration;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.RegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidRegularDrivePeriod;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidTimeException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectUniversityLocation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.*;

/**
 * Die Klasse FormLayoutDriveRoute ist eine Komponente, die mehrfach verwendet wird,
 * um Fahrtangebote anzuzeigen oder zu bearbeiten.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/form-layout-drive-route.css")
public class FormLayoutDriveRoute extends FormLayout {

    private TextFieldAddress address;
    private SelectUniversityLocation fhLocation;
    private TimePicker driveTime;
    private Select<String> carSeatCount;
    private DatePicker driveDateStart;
    private DatePicker driveDateEnd;
    private Checkbox checkboxRegularDrive;
    private Button buttonDetourRoute;
    private Checkbox checkboxFuelParticipation;
    private RadioButtonGroup<String> driveDays;
    private H2 title;
    private Registration registration;

    private DriveRoute driveRoute;
    private AddressConverter converterStart;
    private AddressConverter converterZiel;

    public FormLayoutDriveRoute(DriveRoute driveRoute) {
        createView(driveRoute.getDriveType());
        this.driveRoute = driveRoute;
    }

    public FormLayoutDriveRoute(DriveType driveType) { //FIXME String als Parameter
        createView(driveType);
    }

    private void createView(DriveType driveType) {

        title = new H2("Hinfahrt erstellen");

        setId("form-layout-drive-route-layout");

        address = new TextFieldAddress("Von");
        address.setErrorMessage("Adresse bitte eintragen");
        address.setRequiredIndicatorVisible(true);

        fhLocation = new SelectUniversityLocation();
        fhLocation.setRequiredIndicatorVisible(true);
        fhLocation.setErrorMessage("FH Standort bitte auswählen");

        driveDateStart = new DatePicker("Tag der Fahrt");
        driveDateStart.setRequiredIndicatorVisible(true);
        driveDateStart.setErrorMessage("Tag der Fahrt bitte angeben");

        driveDateEnd = new DatePicker("Zeitraum bis");
        driveDateEnd.setMin(LocalDate.now());
        driveDateEnd.setReadOnly(true);
        driveDateEnd.setErrorMessage("Enddatum der regelmäßigen Fahrt bitte angeben");

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrt");
        checkboxFuelParticipation = new Checkbox("Spritbeteiligung");

        driveTime = new TimePicker("Ankunftzeit");
        driveTime.setStep(Duration.ofMinutes(15));
        driveTime.setErrorMessage("Ankunftszeit/Abfahrtszeit bitte angeben");
        driveTime.setRequiredIndicatorVisible(true);


        driveDays = new RadioButtonGroup<>();
        driveDays.setLabel("Wochentag der regelmäßigen Fahrt");
        driveDays.setItems(DayOfWeek.getDayOfWeekList());
        driveDays.setReadOnly(true);

        carSeatCount = new Select<>();
        carSeatCount.setRequiredIndicatorVisible(true);
        carSeatCount.setLabel("Anzahl der Sitzplätze");
        carSeatCount.setItems("1", "2", "3", "4", "5");
        carSeatCount.setErrorMessage("Anzahl Sitzplätze bitte angeben");
        carSeatCount.setValue("3");

        buttonDetourRoute = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
        buttonDetourRoute.setId("form-layout-drive-route-google_maps_button");

        setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 4, ResponsiveStep.LabelsPosition.TOP));

        if (driveType == DriveType.OUTWARD_TRIP) {
            add(title, address, fhLocation, driveDateStart, driveDateEnd, checkboxRegularDrive, checkboxFuelParticipation,
                    driveTime, driveDays, carSeatCount, buttonDetourRoute);

        } else {
            fhLocation.setLabel("Von");
            address.setLabel("Nach");
            driveTime.setLabel("Abfahrtzeit");
            title.setText("Rückfahrt erstellen");
            add(title, fhLocation, address, driveDateStart, driveDateEnd, checkboxRegularDrive, checkboxFuelParticipation,
                    driveTime, driveDays, carSeatCount, buttonDetourRoute);

        }

        setColspan(title, 4);
        setColspan(address, 2);
        setColspan(fhLocation, 2);
        setColspan(driveDateStart, 1);
        setColspan(driveDateEnd, 1);
        setColspan(checkboxRegularDrive, 1);
        setColspan(driveTime, 1);
        setColspan(driveDays, 3);
        setColspan(carSeatCount, 1);
        setColspan(checkboxFuelParticipation, 1);
        setColspan(buttonDetourRoute, 4);

        fhLocation.addValueChangeListener(e -> fhLocation.setUniversityLocationAddress(e.getValue()));

        checkboxRegularDrive.addValueChangeListener(event -> {
            boolean checked = !event.getValue();
            driveDateStart.setLabel(checked ? "Tag der Fahrt" : "Zeitraum von");
            driveDateEnd.setReadOnly(checked);
            driveDateEnd.setRequiredIndicatorVisible(!checked);
            driveDays.setReadOnly(checked);
            driveDays.setRequiredIndicatorVisible(!checked);
            if (event.getValue()) {
                driveDays.setValue(DayOfWeek.MONDAY.label);
                driveDateStart.setErrorMessage("Startdatum der regelmäßigen Fahrt bitte angeben");
            } else {
                driveDays.clear();
                driveDateEnd.clear();
                driveDateStart.setErrorMessage("Tag der Fahrt bitte angeben");
            }
        });


        registration = buttonDetourRoute.addClickListener(e -> {
            if (driveRoute != null) {
                try {
                    if (!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")) {
                        if (driveRoute.getDriveType().equals(DriveType.OUTWARD_TRIP)) {
                            converterStart = new AddressConverter(driveRoute.getStart().getFullAddressToString());
                            converterZiel = new AddressConverter(fhLocation.getUniversityLocationAddress());
                        } else {
                            converterStart = new AddressConverter(fhLocation.getUniversityLocationAddress());
                            converterZiel = new AddressConverter(driveRoute.getDestination().getFullAddressToString());
                        }

                        RouteString routeString = new RouteString(
                                new Start(new Address(converterStart.getPostalCode(), converterStart.getPlace(), converterStart.getStreet(), converterStart.getNumber())),
                                new Destination(new Address(converterZiel.getPostalCode(), converterZiel.getPlace(), converterZiel.getStreet(), converterZiel.getNumber())),
                                Collections.emptyList());

                        UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
                    } else {
                        NotificationError.show("Bitte Start- und Zieladresse eingeben.");
                    }
                } catch (InvalidAddressException ex) {
                    ex.printStackTrace();
                }

            } else {
                try {
                    if (!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")) {

                        if (driveType.equals(DriveType.OUTWARD_TRIP)) {
                            converterStart = new AddressConverter(address.getValue());
                            converterZiel = new AddressConverter(fhLocation.getUniversityLocationAddress());
                        } else {
                            converterStart = new AddressConverter(fhLocation.getUniversityLocationAddress());
                            converterZiel = new AddressConverter(address.getValue());
                        }

                        RouteString routeString = new RouteString(
                                new Start(new Address(converterStart.getPostalCode(), converterStart.getPlace(), converterStart.getStreet(), converterStart.getNumber())),
                                new Destination(new Address(converterZiel.getPostalCode(), converterZiel.getPlace(), converterZiel.getStreet(), converterZiel.getNumber())),
                                Collections.emptyList());

                        UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
                    } else {
                        NotificationError.show("Bitte Start- und Zieladresse eingeben.");
                    }
                } catch (InvalidAddressException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /*-------------------------------------------------------------------------------------------------------------
                                                        Methoden
     -------------------------------------------------------------------------------------------------------------*/

    /**
     * Die Methode setData füllt das FormLayout mit den Daten einer gegebenen Fahrt.
     *
     * @param driveRoute Fahrt, dessen Daten in dem Formular angezeigt werden sollen.
     */
    public void setData(DriveRoute driveRoute) {
        if (driveRoute == null) {
            throw new IllegalArgumentException();
        }

        setCheckboxFuelParticipation(driveRoute.isFuelParticipation());
        setSeatCount(driveRoute.getSeatCount().toString());
        setDriveTime(driveRoute.getDrivingTime().toLocalTime());
        setDriveDateStart(driveRoute.getDrivingTime().toLocalDate());

        if (UI.getCurrent().getId().get().equals(PageId.OWN_DRIVE_OFFERS_VIEW.label)) {
            if (driveRoute.getDriveType().equals(DriveType.OUTWARD_TRIP)) {
                setFhLocation(driveRoute.getDestination().getAddress().getPlace());
                setAddress(driveRoute.getStart().getFullAddressToString());
            } else {
                setFhLocation(driveRoute.getStart().getAddress().getPlace());
                setAddress(driveRoute.getDestination().getFullAddressToString());
            }
        } else {
            if (driveRoute.getDriveType().equals(DriveType.OUTWARD_TRIP)) {
                setFhLocation(driveRoute.getDestination().getAddress().getPlace());
                setAddress(driveRoute.getStart().getAddress().getPlace());
            } else {
                setFhLocation(driveRoute.getStart().getAddress().getPlace());
                setAddress(driveRoute.getStart().getAddress().getPlace());
            }
        }

        if (driveRoute.getRegularDrive().getRegularDriveDay() != null) {
            setDriveDateEnd(driveRoute.getRegularDrive().getRegularDriveDateEnd());
            setCheckboxRegularDrive(true);
            setDriveDay(driveRoute.getRegularDrive().getRegularDriveDay().label);
        }
    }

    /**
     * Die Methode checkInputFields prüft, ob bei dem Formular alle Pflichteingaben
     * gemacht wurden.
     */
    public boolean checkInputFields() throws InvalidDateException, InvalidRegularDrivePeriod, InvalidAddressException, InvalidTimeException {
        if (address.isReadOnly()) {
            return false;
        }

        if (checkboxRegularDrive.getValue()) {
            checkSingleDrive();
            checkRegularDrive();
            return address.getValue().isEmpty() || fhLocation.isEmpty() || driveDateStart.isEmpty() ||
                    driveTime.isEmpty() || carSeatCount.isEmpty() || driveDateEnd.isEmpty() || driveDays.isEmpty();
        } else {
            checkSingleDrive();
            return address.getValue().isEmpty() || fhLocation.isEmpty() || driveDateStart.isEmpty() ||
                    driveTime.isEmpty() || carSeatCount.isEmpty();
        }

    }

    /**
     * Die Methode checkRegularDrive prüft die Gültigkeit der Datumsangaben beim Erstellen einer
     * regelmäßigen Fahrt.
     *
     * @throws InvalidDateException      Ungültiges Datum
     * @throws InvalidRegularDrivePeriod Ungültiger Zeitraum
     */
    private void checkRegularDrive() throws InvalidDateException, InvalidRegularDrivePeriod {
        if (driveDateEnd.isEmpty()) {
            driveDateEnd.setErrorMessage("Enddatum der regelmäßigen Fahrt bitte angeben");
            driveDateEnd.setInvalid(true);
        } else {
            if (localDateCheckBoolean(getDriveDateEndValue())) {
                driveDateEnd.setErrorMessage("Das Datum darf nicht in der Vergangenheit liegen");
                driveDateEnd.setInvalid(true);
                throw new InvalidDateException("");
            }
            RegularDrive newRegularDrive = new RegularDrive(DayOfWeek.getDayOfWeek(getDriveDays()), getDriveDateStartValue(), getDriveDateEndValue());
            if (newRegularDrive.getDriveDates().size() < 2) {
                driveDateStart.addFocusListener(e -> {
                    driveDateStart.setInvalid(false);
                    driveDateEnd.setInvalid(false);
                });
                driveDateEnd.addFocusListener(e -> {
                    driveDateStart.setInvalid(false);
                    driveDateEnd.setInvalid(false);
                });
                throw new InvalidRegularDrivePeriod("Bei dem angebenden Zeitraum für die Regelmäßige Fahrt, handelt es sich um eine Einzelfahrt!");
            }
        }
    }

    /**
     * Die Methode checkSingleDrive prüft die Eingaben einer Einzelfahrt.
     *
     * @throws InvalidDateException    Ungültiges Datum
     * @throws InvalidAddressException Ungültige Adresse
     */
    private void checkSingleDrive() throws InvalidDateException, InvalidAddressException, InvalidTimeException {
        if (address.getValue().isEmpty()) {
            address.setErrorMessage("Adresse bitte eintragen");
            address.setInvalid(true);
        } else {
            addressPatternCheck(address.getValue());
        }
        if (fhLocation.isEmpty()) {
            fhLocation.setInvalid(true);
        }
        if (driveTime.isEmpty()) {
            driveTime.setInvalid(true);
        }
        if (carSeatCount.isEmpty()) {
            carSeatCount.setInvalid(true);
        }
        if (driveDateStart.isEmpty()) {
            if (checkboxRegularDrive.getValue())
                driveDateStart.setErrorMessage("Startdatum der regelmäßigen Fahrt bitte angeben");
            else
                driveDateStart.setErrorMessage("Tag der Fahrt bitte angeben");
            driveDateStart.setInvalid(true);
        } else {
            if (UI.getCurrent().getId().get().equals(PageId.OWN_DRIVE_OFFERS_VIEW.label) &&
                    !checkboxRegularDrive.getValue() ||
                    UI.getCurrent().getId().get().equals(PageId.OFFER_DRIVE_VIEW.label)) {
                if (localDateCheckBoolean(getDriveDateStartValue())) {
                    driveDateStart.setErrorMessage("Das Datum darf nicht in der Vergangenheit liegen");
                    driveDateStart.setInvalid(true);
                    throw new InvalidDateException("");
                }
                else{
                    if(getDriveDateStartValue().equals(LocalDate.now())){
                        if(driveTime.getValue().equals(LocalTime.now()) || driveTime.getValue().isBefore(LocalTime.now())){
                            driveTime.setErrorMessage("Die Uhrzeit darf nicht in der Vergangenheit liegen");
                            driveTime.setInvalid(true);
                            throw new InvalidTimeException("");
                        }
                    }
                }
            }
        }

    }

    /**
     * Wechselt den Zustand der Komponenten auf dem FormLayout, zu "nur lesen" oder "bearbeitbar".
     *
     * @param isReadOnly boolean
     */
    public void setReadOnly(boolean isReadOnly) {
        address.setReadOnly(isReadOnly);
        address.setReadOnly(isReadOnly);
        fhLocation.setReadOnly(isReadOnly);
        driveTime.setReadOnly(isReadOnly);
        carSeatCount.setReadOnly(isReadOnly);
        driveDateStart.setReadOnly(isReadOnly);
        checkboxFuelParticipation.setReadOnly(isReadOnly);
        checkboxRegularDrive.setReadOnly(isReadOnly);

        if (checkboxRegularDrive.getValue()) {
            driveDateEnd.setReadOnly(isReadOnly);
            driveDays.setReadOnly(isReadOnly);
        }
    }

    /**
     * Wechselt den Zustand der Komponenten auf dem FormLayout, zu "ungültig" oder "gültig".
     *
     * @param isInvalid boolean
     */
    public void setInvalid(boolean isInvalid) {
        address.setInvalid(isInvalid);
        address.setInvalid(isInvalid);
        fhLocation.setInvalid(isInvalid);
        driveTime.setInvalid(isInvalid);
        carSeatCount.setInvalid(isInvalid);
        driveDateStart.setInvalid(isInvalid);
        driveDateEnd.setInvalid(isInvalid);
        driveDays.setInvalid(isInvalid);
    }

    /**
     * Löscht alle Eingabewerte bestimmter Komponenten auf dem Formlayout
     */
    public void clearFields() {
        driveTime.clear();
        driveDateStart.clear();
        driveDateEnd.clear();
        checkboxRegularDrive.clear();
        checkboxFuelParticipation.clear();
    }

    /*-------------------------------------------------------------------------------------------------------------
                                                 Getter/Setter
    -------------------------------------------------------------------------------------------------------------*/

    public H2 getTitle() {
        return title;
    }

    public String getAddressValue() {
        return address.getValue();
    }

    public TextFieldAddress getAddress() {
        return address;
    }

    public String getFhLocation() {
        return fhLocation.getUniversityLocationAddress();
    }

    public LocalTime getDriveTime() {
        return driveTime.getValue();
    }

    public Integer getCarSeatCount() {
        return Integer.parseInt(carSeatCount.getValue());
    }

    public LocalDate getDriveDateStartValue() {
        return driveDateStart.getValue();
    }

    public DatePicker getDriveDateStart() {
        return driveDateStart;
    }

    public LocalDate getDriveDateEndValue() {
        return driveDateEnd.getValue();
    }

    public DatePicker getDriveDateEnd() {
        return driveDateEnd;
    }

    public Boolean getCheckboxRegularDriveValue() {
        return checkboxRegularDrive.getValue();
    }

    public Boolean getCheckboxFuelParticipation() {
        return checkboxFuelParticipation.getValue();
    }

    public String getDriveDays() {
        return driveDays.getValue();
    }

    public Button getButtonDetourRoute() {
        return buttonDetourRoute;
    }

    public void setTitle(String text) {
        this.title.setText(text);
    }

    public void setSeatCount(String anzahl) {
        carSeatCount.setValue(anzahl);
    }

    public void setFhLocation(String fhStandort) {
        fhLocation.setValue(fhStandort);
    }

    public void setDriveTime(LocalTime fahrZeit) {
        driveTime.setValue(fahrZeit);
    }

    public void setDriveDateStart(LocalDate day) {
        driveDateStart.setValue(day);
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public void setCheckboxFuelParticipation(boolean isChecked) {
        this.checkboxFuelParticipation.setValue(isChecked);
    }

    public void setDriveDateEnd(LocalDate day) {
        driveDateEnd.setValue(day);
    }

    public void setCheckboxRegularDrive(boolean isChecked) {
        checkboxRegularDrive.setValue(isChecked);
    }

    public void setDriveDay(String driveDay) {
        driveDays.setValue(driveDay);
    }

    public void removeClickListener() {
        registration.remove();
    }
}

