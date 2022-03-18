package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.RouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * TODO: - Mitfahrer anzeigen falls welche vorhanden?
 *       - Seite muss neugeladen werden, wenn EIne Fahrt bearbeitet wurde
 */


/**
 * Die Klasse OwnDriveOffersEditDialog erstellt einen Dialog für die Bearbeitung
 * des jeweils ausgewähltem Fahrtangebot aus der Tabelle von der View OfferDriveView.
 *
 * @author Ramon Günther & Ivonne Kneißg
 */
@CssImport("/themes/mitfahrgelegenheit/components/own-drive-offers-edit-dialog.css")
public class OwnDriveOffersEditDialog extends Dialog {

    private final Icon EDIT_ICON = new Icon(VaadinIcon.PENCIL);

    private final RouteService routeService;
    private Route route;
    private FormLayoutTopOfferDrive formLayoutHinfahrt;
    private FormLayoutBottomOfferDrive formLayoutRueckfahrt;
    private Notification notification;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public OwnDriveOffersEditDialog(Route route, RouteService routeService) {

        setId("own-drive-offers-edit-dialog");
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        this.routeService = routeService;
        this.route = route;

        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Button closeButton = new Button();
        closeButton.setId("own-drive-offers-edit-dialog-close_button");
        closeButton.setIcon(icon);
        closeButton.addClickListener(e -> close());
        add(closeButton);

        switch (route.getDriveType()) {
            case OUTWARD_TRIP -> {
                formLayoutHinfahrt = new FormLayoutTopOfferDrive();

                formLayoutHinfahrt.setReadOnly(true);
                //der edit Button ist auf dem Formlayout nur für den Dialog!
                formLayoutHinfahrt.getFlexButton().setVisible(true);
                formLayoutHinfahrt.getFlexButton().setText("Bearbeiten");
                formLayoutHinfahrt.getFlexButton().setIcon(EDIT_ICON);
                formLayoutHinfahrt.getFlexButton().setId("own-drive-offers-edit-dialog-edit_button");
                formLayoutHinfahrt.setColspan(formLayoutHinfahrt.getTitleLayout(), 2);

                formLayoutHinfahrt.getFlexButton().addClickListener(e -> {
                    formLayoutHinfahrt.setReadOnly(false);
                    formLayoutHinfahrt.getFlexButton().setVisible(false);
                    formLayoutHinfahrt.getTitleLayout().getStyle().set("height", "100px");
                    formLayoutHinfahrt.setColspan(formLayoutHinfahrt.getTitleLayout(), 4);
                    createButtons(DriveType.OUTWARD_TRIP);
                });

                formLayoutHinfahrt.setTitle("Hinfahrt bearbeiten");
                formLayoutHinfahrt.setSitzplaetze(route.getSeatCount().toString());
                formLayoutHinfahrt.setFhLocation(route.getZiel().getAdresse().getPlace());
                formLayoutHinfahrt.setDriveTime(route.getZiel().getTime().toLocalTime());
                formLayoutHinfahrt.setDriveDateStart(route.getZiel().getTime().toLocalDate());
                formLayoutHinfahrt.setAddress(route.getStart().getAdresse().getStreet() + " "
                        + route.getStart().getAdresse().getHouseNumber() + ", "
                        + route.getStart().getAdresse().getPostal() + " "
                        + route.getStart().getAdresse().getPlace() + ", "
                        + "Deutschland");
                add(formLayoutHinfahrt);
            }
            case RETURN_TRIP -> {
                formLayoutRueckfahrt = new FormLayoutBottomOfferDrive();

                formLayoutRueckfahrt.setReadOnly(true);

                //der edit Button ist auf dem Formlayout nur für den Dialog!
                formLayoutRueckfahrt.getFlexButton().setVisible(true);
                formLayoutRueckfahrt.getFlexButton().setText("Bearbeiten");
                formLayoutRueckfahrt.getFlexButton().setIcon(EDIT_ICON);
                formLayoutRueckfahrt.getFlexButton().setId("own-drive-offers-edit-dialog-edit_button");
                formLayoutRueckfahrt.setColspan(formLayoutRueckfahrt.getTitleLayout(), 2);

                formLayoutRueckfahrt.getFlexButton().addClickListener(e -> {
                    formLayoutRueckfahrt.setReadOnly(false);
                    formLayoutRueckfahrt.getFlexButton().setVisible(false);
                    formLayoutRueckfahrt.getTitleLayout().getStyle().set("height", "100px");
                    formLayoutRueckfahrt.setColspan(formLayoutRueckfahrt.getTitleLayout(), 4);
                    createButtons(DriveType.RETURN_TRIP);
                });

                formLayoutRueckfahrt.setTitle("Rückfahrt bearbeiten");
                formLayoutRueckfahrt.setSitzplaetze(route.getSeatCount().toString());
                formLayoutRueckfahrt.setFhLocation(route.getStart().getAdresse().getPlace());
                formLayoutRueckfahrt.setDriveTime(route.getStart().getTime().toLocalTime());
                formLayoutRueckfahrt.setDriveDateStart(route.getStart().getTime().toLocalDate());
                formLayoutRueckfahrt.setAddress(route.getZiel().getAdresse().getStreet() + " "
                        + route.getZiel().getAdresse().getHouseNumber() + ", "
                        + route.getZiel().getAdresse().getPostal() + " "
                        + route.getZiel().getAdresse().getPlace() + ", "
                        + "Deutschland");
                add(formLayoutRueckfahrt);
            }
        }
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private void createButtons(DriveType fahrtenTyp) {
        Button saveButton = new Button("Speichern");
        saveButton.setId("optionButtonOwnDriveEdit");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setId("optionButtonOwnDriveEdit");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("buttonLayoutOwnDriveEdit");
        buttonLayout.add(saveButton, cancelButton);

        switch (fahrtenTyp) {
            case OUTWARD_TRIP -> {
                saveButton.addClickListener(event -> {
                    saveFormLayoutTop();
                });
                cancelButton.addClickListener(event -> {
                    formLayoutHinfahrt.setReadOnly(true);
                    formLayoutHinfahrt.getFlexButton().setVisible(true);
                    formLayoutHinfahrt.getFlexButton().setId("own-drive-offers-edit-dialog-edit_button");
                    formLayoutHinfahrt.getTitleLayout().getStyle().set("height", "60px");
                    formLayoutHinfahrt.setColspan(formLayoutHinfahrt.getTitleLayout(), 2);
                    remove(buttonLayout);
                });
            }
            case RETURN_TRIP -> {
                saveButton.addClickListener(event -> {
                    saveFormLayoutBottom();
                });
                cancelButton.addClickListener(event -> {
                    formLayoutRueckfahrt.setReadOnly(true);
                    formLayoutRueckfahrt.getFlexButton().setVisible(true);
                    formLayoutRueckfahrt.getFlexButton().setId("own-drive-offers-edit-dialog-edit_button");
                    formLayoutRueckfahrt.setColspan(formLayoutRueckfahrt.getTitleLayout(), 2);
                    formLayoutRueckfahrt.getTitleLayout().getStyle().set("height", "60px");
                    remove(buttonLayout);
                });
            }

        }
        add(buttonLayout);
    }

    private void saveFormLayoutTop() {
        saveDrive(formLayoutHinfahrt.getAddress(), formLayoutHinfahrt.getFhLocation(), formLayoutHinfahrt.getDriveTime(), formLayoutHinfahrt.getCarSeatCount(), DriveType.OUTWARD_TRIP, formLayoutHinfahrt.getDriveDateStart());
    }

    private void saveFormLayoutBottom() {
        saveDrive(formLayoutRueckfahrt.getFhLocation(), formLayoutRueckfahrt.getAddress(), formLayoutRueckfahrt.getDriveTime(), formLayoutRueckfahrt.getCarSeatCount(), DriveType.RETURN_TRIP, formLayoutRueckfahrt.getDriveDateStart());
    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try {
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = route.getBenutzer();

            Route updateRoute = new Route(
                    route.getId(),
                    new Start(firstAddress, driveTime.atDate(driveDate)),
                    new Destination(secondAddress, driveTime.atDate(driveDate)),
                    carSeatCount, user,LocalDateTime.now(),fahrtenTyp);

            routeService.save(updateRoute);

            NotificationSuccess.show("Fahrt wurde gespeichert!");
            this.close();

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
        }

    }

}
