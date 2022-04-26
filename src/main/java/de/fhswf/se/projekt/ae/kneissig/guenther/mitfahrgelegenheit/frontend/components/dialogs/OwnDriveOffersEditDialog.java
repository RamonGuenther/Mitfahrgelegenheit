package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.RegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidRegularDrivePeriod;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Die Klasse OwnDriveOffersEditDialog erstellt einen Dialog für die Bearbeitung
 * des jeweils ausgewähltem Fahrtangebot aus der Tabelle von der View OfferDriveView.
 *
 * @author Ramon Günther & Ivonne Kneißg
 */
@CssImport("/themes/mitfahrgelegenheit/components/own-drive-offers-edit-dialog.css")
public class OwnDriveOffersEditDialog extends Dialog {

    private final DriveRouteService driveRouteService;
    private final MailService mailService;

    private final HorizontalLayout defaultButtonLayout;
    private final TextArea note;
    private final DriveRoute driveRoute;

    private FormLayoutDriveRoute formLayoutDriveRouteTop;
    private FormLayoutDriveRoute formLayoutDriveRouteBottom;


    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public OwnDriveOffersEditDialog(DriveRoute driveRoute, DriveRouteService driveRouteService, MailService mailService) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        this.driveRoute = driveRoute;
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;

        switch (driveRoute.getDriveType()) {
            case OUTWARD_TRIP -> {
                formLayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                formLayoutDriveRouteTop.setTitle("Hinfahrt bearbeiten");
                formLayoutDriveRouteTop.setData(driveRoute);
                formLayoutDriveRouteTop.setReadOnly(true);
                formLayoutDriveRouteTop.getButtonDetourRoute().addClickListener(e -> UI.getCurrent().getPage().open(driveRoute.getCurrentRouteLink(), "_blank"));
                add(formLayoutDriveRouteTop);
            }
            case RETURN_TRIP -> {
                formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                formLayoutDriveRouteBottom.setTitle("Rückfahrt bearbeiten");
                formLayoutDriveRouteBottom.setData(driveRoute);
                formLayoutDriveRouteBottom.setReadOnly(true);
                formLayoutDriveRouteBottom.getButtonDetourRoute().addClickListener(e -> UI.getCurrent().getPage().open(driveRoute.getCurrentRouteLink(), "_blank"));
                add(formLayoutDriveRouteBottom);
            }
        }


        note = new TextArea("Anmerkung");
        note.setReadOnly(true);
        note.setValue(driveRoute.getNote());
        note.setId("own-drive-offers-edit-dialog-note");

        HorizontalLayout passengerBadgeLayout = new HorizontalLayout(new Label("Mitfahrer:    "));
        passengerBadgeLayout.setId("own-drive-offers-edit-dialog-passenger_badge_layout");

        for (Booking booking : driveRoute.getBookings()) {
            Span pending = new Span(booking.getRegularDriveSingleDriveDate() == null ? booking.getPassenger().getFullName() :
                    booking.getPassenger().getFullName() + " (nur am " + booking.getFormattedSingleDriveDate() + ")");
            pending.getElement().getThemeList().add("badge");
            pending.getStyle().set("cursor", "pointer");
            pending.addClickListener(e -> {
                UI.getCurrent().navigate(ProfileView.class,
                        new RouteParameters(new RouteParam("username", booking.getPassenger().getUsername())));
                close();
            });
            passengerBadgeLayout.add(pending);
        }

        Button editButton = new Button("Bearbeiten");
        editButton.setIcon(VaadinIcon.PENCIL.create());
        editButton.setClassName("own-drive-offers-edit-dialog-buttons");
        editButton.addClickListener(e -> createEditButtons(driveRoute.getDriveType()));

        Button closeButton = new Button("Schließen");
        closeButton.setClassName("own-drive-offers-edit-dialog-buttons");
        closeButton.addClickListener(e -> close());

        defaultButtonLayout = new HorizontalLayout(editButton, closeButton);
        defaultButtonLayout.setId("own-drive-offers-edit-dialog-default_button_layout");

        add(note, passengerBadgeLayout, defaultButtonLayout);
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private void createEditButtons(DriveType fahrtenTyp) {
        Button saveButton = new Button("Speichern");
        saveButton.setClassName("own-drive-offers-edit-dialog-buttons_edit");

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setClassName("own-drive-offers-edit-dialog-buttons_edit");

        Button deleteButton = new Button("Fahrt löschen");
        deleteButton.setIcon(VaadinIcon.TRASH.create());
        deleteButton.setId("own-drive-offers-edit-dialog-delete_button");
        deleteButton.setClassName("own-drive-offers-edit-dialog-buttons_edit");
        deleteButton.addClickListener(e -> {
            DeleteDriveDialog deleteDriveDialog = new DeleteDriveDialog(driveRoute, driveRouteService, mailService);
            deleteDriveDialog.open();
        });

        HorizontalLayout editButtonLayout = new HorizontalLayout();
        editButtonLayout.setId("own-drive-offers-edit-dialog-edit_button_layout");
        editButtonLayout.add(saveButton, cancelButton, deleteButton);

        remove(defaultButtonLayout);
        add(editButtonLayout);

        note.setReadOnly(false);

        switch (fahrtenTyp) {
            case OUTWARD_TRIP -> {
                formLayoutDriveRouteTop.setReadOnly(driveRoute.getBookings().size() > 0);
                saveButton.addClickListener(event -> saveFormLayoutTop());
                cancelButton.addClickListener(event -> {
                    formLayoutDriveRouteTop.setData(driveRoute);
                    formLayoutDriveRouteTop.setReadOnly(true);
                    note.setReadOnly(true);
                    remove(editButtonLayout);
                    add(defaultButtonLayout);
                });
            }
            case RETURN_TRIP -> {
                formLayoutDriveRouteBottom.setReadOnly(driveRoute.getBookings().size() > 0);
                saveButton.addClickListener(event -> saveFormLayoutBottom());
                cancelButton.addClickListener(event -> {
                    formLayoutDriveRouteBottom.setData(driveRoute);
                    formLayoutDriveRouteBottom.setReadOnly(true);
                    note.setReadOnly(true);
                    remove(editButtonLayout);
                    add(defaultButtonLayout);
                });
            }

        }
        add(editButtonLayout);
    }

    private void saveFormLayoutTop() {
        try {
            if (formLayoutDriveRouteTop.checkInputFields()) {
                NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                return;
            }

            saveDrive(formLayoutDriveRouteTop.getAddressValue(),
                    formLayoutDriveRouteTop.getFhLocation(),
                    formLayoutDriveRouteTop.getDriveTime(),
                    formLayoutDriveRouteTop.getCheckboxFuelParticipation(),
                    formLayoutDriveRouteTop.getCarSeatCount(),
                    DriveType.OUTWARD_TRIP,
                    formLayoutDriveRouteTop.getDriveDateStart(),
                    note.getValue()
            );
        }
        catch (InvalidDateException | InvalidRegularDrivePeriod ex){
            NotificationError.show(ex.getMessage());
            ex.printStackTrace();
        }


    }

    private void saveFormLayoutBottom() {
        try {
            if (formLayoutDriveRouteBottom.checkInputFields()) {
                NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                return;
            }
            saveDrive(formLayoutDriveRouteBottom.getFhLocation(),
                    formLayoutDriveRouteBottom.getAddressValue(),
                    formLayoutDriveRouteBottom.getDriveTime(),
                    formLayoutDriveRouteBottom.getCheckboxRegularDriveValue(),
                    formLayoutDriveRouteBottom.getCarSeatCount(),
                    DriveType.RETURN_TRIP,
                    formLayoutDriveRouteBottom.getDriveDateStart(),
                    note.getValue()
            );
        }
        catch (InvalidDateException | InvalidRegularDrivePeriod ex){
            NotificationError.show(ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, boolean fuelParticipation,
                           Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate, String note) {
        try {
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = driveRoute.getDriver();

            DriveRoute updateDriveRoute = new DriveRoute(
                    driveRoute.getId(),
                    new Start(firstAddress),
                    new Destination(secondAddress),
                    driveTime.atDate(driveDate),
                    fuelParticipation,
                    carSeatCount,
                    user,
                    LocalDateTime.now(),
                    fahrtenTyp,
                    note
            );

            if (fahrtenTyp.equals(DriveType.OUTWARD_TRIP)) {
                if(formLayoutDriveRouteTop.getCheckboxRegularDriveValue()){
                    updateDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formLayoutDriveRouteTop.getDriveDays()),
                            formLayoutDriveRouteTop.getDriveDateStart(),
                            formLayoutDriveRouteTop.getDriveDateEnd())
                    );
                }
            }
            else{
                if(formLayoutDriveRouteBottom.getCheckboxRegularDriveValue()){
                    updateDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formLayoutDriveRouteBottom.getDriveDays()),
                            formLayoutDriveRouteBottom.getDriveDateStart(),
                            formLayoutDriveRouteBottom.getDriveDateEnd())
                    );
                }
            }

            driveRouteService.save(updateDriveRoute);

            UI.getCurrent().getPage().reload();
            this.close();

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
        }

    }

}
