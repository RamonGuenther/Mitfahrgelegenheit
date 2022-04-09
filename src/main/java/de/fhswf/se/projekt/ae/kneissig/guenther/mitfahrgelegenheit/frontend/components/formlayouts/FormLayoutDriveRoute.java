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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.CheckboxRegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectUniversityLocation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@CssImport("/themes/mitfahrgelegenheit/components/form-layout-drive-route.css")
public class FormLayoutDriveRoute extends FormLayout {

    private final TextFieldAddress address;
    private final SelectUniversityLocation fhLocation;
    private final TimePicker driveTime;
    private final Select<String> carSeatCount;
    private final DatePicker driveDateStart;
    private final DatePicker driveDateEnd;
    private final Checkbox checkboxRegularDrive;


    private Checkbox checkboxFuelParticipation;
    private final CheckboxRegularDrive driveDays;
    private H2 title;

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
        driveDateStart.setRequiredIndicatorVisible(true);

        driveDateEnd = new DatePicker("Zeitraum bis");
        driveDateEnd.setReadOnly(true);

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrt");
        checkboxFuelParticipation = new Checkbox("Spritbeteiligung");

        driveTime = new TimePicker("Ankunftzeit");
        driveTime.setStep(Duration.ofMinutes(15));

        driveDays = new CheckboxRegularDrive();
        driveDays.setReadOnly(true);

        driveTime.setRequiredIndicatorVisible(true);

        carSeatCount = new Select<>();
        carSeatCount.setRequiredIndicatorVisible(true);
        carSeatCount.setLabel("Anzahl Sitzplätze");
        carSeatCount.setItems("1", "2", "3", "4");

        Button buttonDetourRoute = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
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
            if (!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")) {
                AddressConverter converterStart = new AddressConverter(address.getValue());
                AddressConverter converterZiel = new AddressConverter(fhLocation.getUniversityLocationAddress());

                RouteString routeString = new RouteString(
                        new Start(new Address(converterStart.getPostalCode(), converterStart.getPlace(), converterStart.getStreet(), converterStart.getNumber()), LocalDateTime.now()),
                        new Destination(new Address(converterZiel.getPostalCode(), converterZiel.getPlace(), converterZiel.getStreet(), converterZiel.getNumber()), LocalDateTime.now()),
                        Collections.emptyList());

                UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
            } else {
                Notification notification = new Notification("Bitte Start- und Zieladresse eingeben.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });
    }


    public H2 getTitle() {
        return title;
    }

    public void setTitle(String text) {
        this.title.setText(text);
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

    public Boolean getCheckboxRegularDrive() {
        return checkboxRegularDrive.getValue();
    }

    public Boolean getCheckboxFuelParticipation() {
        return checkboxFuelParticipation.getValue();
    }

    public Set<String> getDriveDays() {
        return driveDays.getValue();
    }


    public void setSitzplaetze(String anzahl) {
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




    //TODO: MUSS MIT ALLEN DATEN PASSIEREN AUCH BEI NULL
    public void setData(DriveRoute driveRoute) {
        if(driveRoute == null){
            throw new IllegalArgumentException();
        }

        setCheckboxFuelParticipation(driveRoute.isFuelParticipation());
        setSitzplaetze(driveRoute.getSeatCount().toString());
        setFhLocation(driveRoute.getZiel().getAddress().getPlace());
        setDriveTime(driveRoute.getZiel().getTime().toLocalTime());
        setDriveDateStart(driveRoute.getZiel().getTime().toLocalDate());
        setAddress(driveRoute.getStart().getAddress().getStreet() + " "
                + driveRoute.getStart().getAddress().getHouseNumber() + ", "
                + driveRoute.getStart().getAddress().getPostal() + " "
                + driveRoute.getStart().getAddress().getPlace() + ", "
                + "Deutschland");
    }

    public void setReadOnly(boolean readOnly) {
        address.setReadOnly(readOnly);
        address.setReadOnly(readOnly);
        fhLocation.setReadOnly(readOnly);
        driveTime.setReadOnly(readOnly);
        carSeatCount.setReadOnly(readOnly);
        driveDateStart.setReadOnly(readOnly);
        driveDateEnd.setReadOnly(readOnly);
        checkboxRegularDrive.setReadOnly(readOnly);
        checkboxFuelParticipation.setReadOnly(readOnly);
        driveDays.setReadOnly(readOnly);
    }

}

