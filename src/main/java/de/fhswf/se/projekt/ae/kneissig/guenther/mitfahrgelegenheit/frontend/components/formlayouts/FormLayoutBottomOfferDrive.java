package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Die Klasse FormLayoutBottomOfferDrive erstellt ein
 * FormLayout für die Klasse OfferDrive.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class FormLayoutBottomOfferDrive extends FormLayout {

    private final H2 title;
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
    private final Button flexButton;
    private final HorizontalLayout titleLayout;


    /**
     * Der Konstruktor erstellt das FormLayout für
     * die Klasse OfferDriveView
     */
    public FormLayoutBottomOfferDrive() {

        setId("formLayoutOfferDriveBottom");

        title = new H2("Hinfahrt erstellen");

        //nur für den SearchDriveResultViewDialog Dialog
        titleLayout = new HorizontalLayout();
        titleLayout.add(title);
        titleLayout.getStyle().set("height","60px");

        //Nur für den Dialog in OwnDriveOffersEditDialog & SearchDriveResultViewDialog
        flexButton = new Button();
        flexButton.setVisible(false);

        address = new TextFieldAddress("Nach");

        fhLocation = new SelectUniversityLocation();

        driveDateStart = new DatePicker("Tag der Fahrt");

        driveDateEnd = new DatePicker("Zeitraum bis");
        driveDateEnd.setReadOnly(true);

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrt");
        checkboxFuelParticipation = new Checkbox("Spritbeteiligung");

        driveTime = new TimePicker("Abfahrtszeit");
        driveTime.setStep(Duration.ofMinutes(15));

        driveDays = new CheckboxRegularDrive();
        driveDays.setReadOnly(true);

        carSeatCount = new Select<>();
        carSeatCount.setLabel("Anzahl Sitzplätze");
        carSeatCount.setItems("1", "2", "3", "4");

        Button buttonDetourRoute = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
        buttonDetourRoute.setId("buttonGoogleMapsOfferDrive");

        detourCheckbox = new Checkbox("Umweg möglich?");
        detourCheckbox.setId("detourCheckBox");

        visibleCheckbox = new Checkbox("Unsichtbar schalten");

        setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 4, ResponsiveStep.LabelsPosition.TOP));

        add(titleLayout, flexButton, fhLocation, address, driveDateStart, driveDateEnd, checkboxRegularDrive,
                checkboxFuelParticipation, driveTime, driveDays, carSeatCount, detourCheckbox, visibleCheckbox, buttonDetourRoute);

        setColspan(titleLayout, 4);
        setColspan(flexButton,2);
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

        visibleCheckbox.addValueChangeListener(event -> {
            boolean checked = !event.getValue();
            visibleCheckbox.setLabel(checked ? "Unsichtbar schalten" : "Sichtbar schalten");
        });

        buttonDetourRoute.addClickListener(e -> {
            if (!Objects.equals(address.getValue(), "") && !Objects.equals(fhLocation.getValue(), "")) {
                AddressConverter converterStart = new AddressConverter(fhLocation.getUniversityLocationAddress());
                AddressConverter converterZiel = new AddressConverter(address.getValue());
                RouteString routeString = new RouteString(converterStart.getStreet(), converterStart.getNumber(), converterStart.getPostalCode(), converterStart.getPlace(),
                        converterZiel.getStreet(), converterZiel.getNumber(), converterZiel.getPostalCode(), converterZiel.getPlace());

                UI.getCurrent().getPage().open(routeString.getRoute(), "_blank");
            } else {
                Notification notification = new Notification("Bitte Start- und Zieladresse eingeben.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });

    }

    public HorizontalLayout getTitleLayout() {
        return titleLayout;
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

    public void setTitle(String titel) {
        title.setText(titel);
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

    public Button getFlexButton() {
        return flexButton;
    }

    /**
     * Die Methode clearFields() setzt alle Felder der View
     * zurück bzw. leert sie.
     */
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
        detourCheckbox.clear();
        visibleCheckbox.clear();
    }

    public void setReadOnly(boolean check) {
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


