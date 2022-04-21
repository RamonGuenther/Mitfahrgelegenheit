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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectUniversityLocation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.localDateCheck;

@CssImport("/themes/mitfahrgelegenheit/components/form-layout-drive-route.css")
public class FormLayoutDriveRoute extends FormLayout {

    private final TextFieldAddress address;
    private final SelectUniversityLocation fhLocation;
    private final TimePicker driveTime;
    private final Select<String> carSeatCount;
    private final DatePicker driveDateStart;
    private final DatePicker driveDateEnd;
    private final Checkbox checkboxRegularDrive;
    private final Button buttonDetourRoute;


    private final Checkbox checkboxFuelParticipation;
    private final RadioButtonGroup<String> driveDays;
    private final H2 title;

    /**
     * Der Konstruktor erstellt das FormLayout für
     * die Klasse OfferDriveView
     */
    public FormLayoutDriveRoute(DriveType driveType) {

        title = new H2("Hinfahrt erstellen");

        setId("form-layout-drive-route-layout");

        address = new TextFieldAddress("Von");
        address.setRequiredIndicatorVisible(true);

        fhLocation = new SelectUniversityLocation();
        fhLocation.setRequiredIndicatorVisible(true);

        driveDateStart = new DatePicker("Tag der Fahrt");
        driveDateStart.setMin(LocalDate.now());
        driveDateStart.setRequiredIndicatorVisible(true);

        driveDateEnd = new DatePicker("Zeitraum bis");
        driveDateEnd.setMin(LocalDate.now());
        driveDateEnd.setReadOnly(true);

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrt");
        checkboxFuelParticipation = new Checkbox("Spritbeteiligung");

        driveTime = new TimePicker("Ankunftzeit");
        driveTime.setStep(Duration.ofMinutes(15));

        driveDays = new RadioButtonGroup<>();
        driveDays.setItems(DayOfWeek.getDayOfWeekList());
        driveDays.setReadOnly(true);

        driveTime.setRequiredIndicatorVisible(true);

        carSeatCount = new Select<>();
        carSeatCount.setRequiredIndicatorVisible(true);
        carSeatCount.setLabel("Anzahl Sitzplätze");
        carSeatCount.setItems("1", "2", "3", "4");

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

        /* Listener*/

        fhLocation.addValueChangeListener(e -> fhLocation.setUniversityLocationAddress(e.getValue()));

        checkboxRegularDrive.addValueChangeListener(event -> {
            boolean checked = !event.getValue();
            driveDateStart.setLabel(checked ? "Tag der Fahrt" : "Zeitraum von");
            driveDateEnd.setReadOnly(checked);
            driveDateEnd.setRequiredIndicatorVisible(!checked);
            driveDays.setReadOnly(checked);
            driveDays.setRequiredIndicatorVisible(!checked);
        });


        buttonDetourRoute.addClickListener(e -> {
            try {
                if (!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")) {
                    AddressConverter converterStart = new AddressConverter(address.getValue());
                    AddressConverter converterZiel = new AddressConverter(fhLocation.getUniversityLocationAddress());

                    RouteString routeString = new RouteString(
                            new Start(new Address(converterStart.getPostalCode(), converterStart.getPlace(), converterStart.getStreet(), converterStart.getNumber())),
                            new Destination(new Address(converterZiel.getPostalCode(), converterZiel.getPlace(), converterZiel.getStreet(), converterZiel.getNumber())),
                            Collections.emptyList());

                    UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
                } else {
                    Notification notification = new Notification("Bitte Start- und Zieladresse eingeben.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
            } catch (InvalidAddressException ex) {
                ex.printStackTrace();
            }
        });
    }


    public H2 getTitle() {
        return title;
    }

    public String getAddress() {
        return address.getValue();
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

    public LocalDate getDriveDateStart() {
        return driveDateStart.getValue();
    }

    public LocalDate getDriveDateEnd() {
        return driveDateEnd.getValue();
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

    public void setDriveDateEnd(LocalDate day){
        driveDateEnd.setValue(day);
    }

    public void setCheckboxRegularDrive(boolean isChecked){
        checkboxRegularDrive.setValue(isChecked);
    }

    public void setDriveDay(String driveDay){
        driveDays.setValue(driveDay);
    }


    //TODO: MUSS MIT ALLEN DATEN PASSIEREN AUCH BEI NULL
    public void setData(DriveRoute driveRoute) {
        if (driveRoute == null) {
            throw new IllegalArgumentException();
        }
        setCheckboxFuelParticipation(driveRoute.isFuelParticipation());
        setSeatCount(driveRoute.getSeatCount().toString());
        setDriveTime(driveRoute.getDrivingTime().toLocalTime());
        setDriveDateStart(driveRoute.getDrivingTime().toLocalDate());

        if(driveRoute.getDriveType().equals(DriveType.OUTWARD_TRIP)){
            setFhLocation(driveRoute.getDestination().getAddress().getPlace());
            setAddress(driveRoute.getStart().getFullAddressToString());
        }
        else{
            setFhLocation(driveRoute.getStart().getAddress().getPlace());
            setAddress(driveRoute.getDestination().getFullAddressToString());
        }

        if(driveRoute.getRegularDrive().getRegularDriveDay() != null){
            setDriveDateEnd(driveRoute.getRegularDrive().getRegularDriveDateEnd());
            driveDateEnd.setReadOnly(true);
            setCheckboxRegularDrive(true);
            checkboxRegularDrive.setReadOnly(true);
            setDriveDay(driveRoute.getRegularDrive().getRegularDriveDay().label);
            driveDays.setReadOnly(true);
        }
    }

    public boolean checkData() throws InvalidDateException {

        if (!driveDateStart.isEmpty())
                localDateCheck(getDriveDateStart());

        if (checkboxRegularDrive.getValue()) {

            if(!driveDateEnd.isEmpty())
                localDateCheck(getDriveDateEnd());

            return address.getValue().isEmpty() || fhLocation.isEmpty() || driveDateStart.isEmpty() ||
                    driveTime.isEmpty() || carSeatCount.isEmpty() || driveDateEnd.isEmpty() || driveDays.isEmpty();

        } else {
            return address.getValue().isEmpty() || fhLocation.isEmpty() || driveDateStart.isEmpty() ||
                    driveTime.isEmpty() || carSeatCount.isEmpty();
        }
    }

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

    public void clearFields() {
        address.setValue(" ");
        address.setOptions(Collections.emptyList());
        fhLocation.clear();
        driveTime.clear();
        carSeatCount.clear();
        driveDateStart.clear();
        driveDateEnd.clear();
        checkboxRegularDrive.clear();
        checkboxFuelParticipation.clear();
        driveDays.clear();
    }

}

