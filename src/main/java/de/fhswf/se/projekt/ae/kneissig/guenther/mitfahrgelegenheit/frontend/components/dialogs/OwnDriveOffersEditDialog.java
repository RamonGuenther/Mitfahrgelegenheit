package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Adresse;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Ziel;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.FahrerRouteService;
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

    private final FahrerRouteService fahrerRouteService;
    private FahrerRoute fahrerRoute;
    private FormLayoutTopOfferDrive formLayoutHinfahrt;
    private FormLayoutBottomOfferDrive formLayoutRueckfahrt;
    private Notification notification;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public OwnDriveOffersEditDialog(FahrerRoute fahrerRoute, FahrerRouteService fahrerRouteService) {

        setId("own-drive-offers-edit-dialog");
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        this.fahrerRouteService = fahrerRouteService;
        this.fahrerRoute = fahrerRoute;

        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Button closeButton = new Button();
        closeButton.setId("own-drive-offers-edit-dialog-close_button");
        closeButton.setIcon(icon);
        closeButton.addClickListener(e -> close());
        add(closeButton);

        switch (fahrerRoute.getFahrtenTyp()) {
            case HINFAHRT -> {
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
                    createButtons(DriveType.HINFAHRT);
                });

                formLayoutHinfahrt.setTitle("Hinfahrt bearbeiten");
                formLayoutHinfahrt.setSitzplaetze(fahrerRoute.getSitzplaetze().toString());
                formLayoutHinfahrt.setFhLocation(fahrerRoute.getZiel().getAdresse().getOrt());
                formLayoutHinfahrt.setDriveTime(fahrerRoute.getZiel().getZeit().toLocalTime());
                formLayoutHinfahrt.setDriveDateStart(fahrerRoute.getZiel().getZeit().toLocalDate());
                formLayoutHinfahrt.setAddress(fahrerRoute.getStart().getAdresse().getStrasse() + " "
                        + fahrerRoute.getStart().getAdresse().getHausnummer() + ", "
                        + fahrerRoute.getStart().getAdresse().getPlz() + " "
                        + fahrerRoute.getStart().getAdresse().getOrt() + ", "
                        + "Deutschland");
                add(formLayoutHinfahrt);
            }
            case RUECKFAHRT -> {
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
                    createButtons(DriveType.RUECKFAHRT);
                });

                formLayoutRueckfahrt.setTitle("Rückfahrt bearbeiten");
                formLayoutRueckfahrt.setSitzplaetze(fahrerRoute.getSitzplaetze().toString());
                formLayoutRueckfahrt.setFhLocation(fahrerRoute.getStart().getAdresse().getOrt());
                formLayoutRueckfahrt.setDriveTime(fahrerRoute.getStart().getZeit().toLocalTime());
                formLayoutRueckfahrt.setDriveDateStart(fahrerRoute.getStart().getZeit().toLocalDate());
                formLayoutRueckfahrt.setAddress(fahrerRoute.getZiel().getAdresse().getStrasse() + " "
                        + fahrerRoute.getZiel().getAdresse().getHausnummer() + ", "
                        + fahrerRoute.getZiel().getAdresse().getPlz() + " "
                        + fahrerRoute.getZiel().getAdresse().getOrt() + ", "
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
            case HINFAHRT -> {
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
            case RUECKFAHRT -> {
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
        saveDrive(formLayoutHinfahrt.getAddress(), formLayoutHinfahrt.getFhLocation(), formLayoutHinfahrt.getDriveTime(), formLayoutHinfahrt.getCarSeatCount(), DriveType.HINFAHRT, formLayoutHinfahrt.getDriveDateStart());
    }

    private void saveFormLayoutBottom() {
        saveDrive(formLayoutRueckfahrt.getFhLocation(), formLayoutRueckfahrt.getAddress(), formLayoutRueckfahrt.getDriveTime(), formLayoutRueckfahrt.getCarSeatCount(), DriveType.RUECKFAHRT, formLayoutRueckfahrt.getDriveDateStart());
    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try {
            AddressConverter converter = new AddressConverter(address);
            Adresse firstAddress = new Adresse(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Adresse secondAddress = new Adresse(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            Benutzer benutzer = fahrerRoute.getBenutzer();

            FahrerRoute updateFahrerRoute = new FahrerRoute(
                    fahrerRoute.getId(),
                    new Start(firstAddress, driveTime.atDate(driveDate)),
                    new Ziel(secondAddress, driveTime.atDate(driveDate)),
                    carSeatCount, benutzer,LocalDateTime.now(),fahrtenTyp);

            fahrerRouteService.save(updateFahrerRoute);

            NotificationSuccess.show("Fahrt wurde gespeichert!");
            this.close();

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
        }

    }

}
