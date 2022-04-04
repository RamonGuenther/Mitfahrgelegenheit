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
    private final Checkbox checkboxFuelParticipation;
    private final CheckboxRegularDrive driveDays;
    private final Checkbox detourCheckbox;
    private final Checkbox visibleCheckbox;
    private final H2 title;


    /**
     * Der Konstruktor erstellt das FormLayout für
     * die Klasse OfferDriveView
     */
    public FormLayoutDriveRoute(DriveType driveType) {

        title = new H2("Hinfahrt erstellen");

        setId("form-layout-drive-route-layout");

        address = new TextFieldAddress("Von");

        fhLocation = new SelectUniversityLocation();

        driveDateStart = new DatePicker("Tag der Fahrt");

        driveDateEnd = new DatePicker("Zeitraum bis");
        driveDateEnd.setReadOnly(true);

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrt");
        checkboxFuelParticipation = new Checkbox("Spritbeteiligung");

        driveTime = new TimePicker("Ankunftzeit");
        driveTime.setStep(Duration.ofMinutes(15));

        driveDays = new CheckboxRegularDrive();
        driveDays.setReadOnly(true);

        carSeatCount = new Select<>();
        carSeatCount.setLabel("Anzahl Sitzplätze");
        carSeatCount.setItems("1", "2", "3", "4");

        Button buttonDetourRoute = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
        buttonDetourRoute.setId("form-layout-drive-route-google_maps_button");

        detourCheckbox = new Checkbox("Umweg möglich?");

        visibleCheckbox = new Checkbox("Unsichtbar schalten");

        setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 4, ResponsiveStep.LabelsPosition.TOP));

        if(driveType == DriveType.OUTWARD_TRIP){
            add(title,address, fhLocation, driveDateStart, driveDateEnd, checkboxRegularDrive, checkboxFuelParticipation,
                    driveTime, driveDays, carSeatCount, detourCheckbox, visibleCheckbox, buttonDetourRoute);

        }
        else{
            fhLocation.setLabel("Von");
            address.setLabel("Nach");
            driveTime.setLabel("Abfahrtzeit");
            title.setText("Rückfahrt erstellen");
            add(title,fhLocation, address,driveDateStart, driveDateEnd, checkboxRegularDrive, checkboxFuelParticipation,
                    driveTime, driveDays, carSeatCount, detourCheckbox, visibleCheckbox, buttonDetourRoute);

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
        setColspan(buttonDetourRoute,4);
        setColspan(detourCheckbox, 1);
        setColspan(visibleCheckbox, 1);

        /* Listener*/

        fhLocation.addValueChangeListener(e -> fhLocation.setUniversityLocationAddress(e.getValue()));

        checkboxRegularDrive.addValueChangeListener(event -> {
            boolean checked = !event.getValue();
            driveDateStart.setLabel(checked ? "Tag der Fahrt" : "Zeitraum von");
            driveDateEnd.setReadOnly(checked);
            driveDays.setReadOnly(checked);
        });


        buttonDetourRoute.addClickListener(e -> {
            if(!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")){
                AddressConverter converterStart = new AddressConverter(address.getValue());
                AddressConverter converterZiel = new AddressConverter(fhLocation.getUniversityLocationAddress());

                RouteString routeString = new RouteString(
                        new Start(new Address(converterStart.getPostalCode(), converterStart.getPlace(), converterStart.getStreet(), converterStart.getNumber()), LocalDateTime.now()),
                        new Destination(new Address(converterZiel.getPostalCode(), converterZiel.getPlace(), converterZiel.getStreet(), converterZiel.getNumber()), LocalDateTime.now()),
                        Collections.emptyList());

                UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
            }
            else{
                Notification notification = new Notification("Bitte Start- und Zieladresse eingeben.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
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

    public Boolean getCheckboxRegularDrive() {
        return checkboxRegularDrive.getValue();
    }

    public Boolean getCheckboxFuelParticipation() {
        return checkboxFuelParticipation.getValue();
    }

    public Set<String> getDriveDays() {
        return driveDays.getValue();
    }

    public Boolean getDetourCheckbox() {
        return detourCheckbox.getValue();
    }

    public Boolean getVisibleCheckbox() {
        return visibleCheckbox.getValue();
    }

    public void setSitzplaetze(String anzahl){
        carSeatCount.setValue(anzahl);
    }

    public void setFhLocation(String fhStandort){
        fhLocation.setValue(fhStandort);
    }

    public void setDriveTime(LocalTime fahrZeit){
        driveTime.setValue(fahrZeit);
    }

    public void setDriveDateStart(LocalDate day){
        driveDateStart.setValue(day);
    }

    public void setAddress(String address){
        this.address.setValue(address);
    }

    public void setReadOnly(boolean check){
        address.setReadOnly(check);
        address.setReadOnly(check);
        fhLocation.setReadOnly(check);
        driveTime.setReadOnly(check);
        carSeatCount.setReadOnly(check);
        driveDateStart.setReadOnly(check);
        driveDateEnd.setReadOnly(check);
        checkboxRegularDrive.setReadOnly(check);
        checkboxFuelParticipation.setReadOnly(check);
        driveDays.setReadOnly(check);
        detourCheckbox.setReadOnly(check);
        visibleCheckbox.setReadOnly(check);
    }

}

