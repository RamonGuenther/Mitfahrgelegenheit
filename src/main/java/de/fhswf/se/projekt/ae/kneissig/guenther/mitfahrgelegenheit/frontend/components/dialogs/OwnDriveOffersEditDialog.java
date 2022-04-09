package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;


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
    private final HorizontalLayout defaultButtonLayout;
    private DriveRoute driveRoute;

    private FormLayoutDriveRoute formLayoutDriveRouteTop;
    private FormLayoutDriveRoute formLayoutDriveRouteBottom;


    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */

    //TODO: hier muss noch das Textfeld hin für die Änderungsnotiz
    public OwnDriveOffersEditDialog(DriveRoute driveRoute, DriveRouteService driveRouteService) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        this.driveRouteService = driveRouteService;
        this.driveRoute = driveRoute;

        Button deleteButton = new Button(VaadinIcon.TRASH.create());
        deleteButton.setId("own-drive-offers-edit-dialog-delete_button");
        add(deleteButton);

        deleteButton.addClickListener(e->{
           //DIALOG Für ob man sich sicher is da
            driveRouteService.delete(driveRoute);
        });

        switch (driveRoute.getDriveType()) {
            case OUTWARD_TRIP -> {
                formLayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                formLayoutDriveRouteTop.setReadOnly(true);
                formLayoutDriveRouteTop.setData(driveRoute);
                add(formLayoutDriveRouteTop);
            }
            case RETURN_TRIP -> {
                formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                formLayoutDriveRouteBottom.setReadOnly(true);
                formLayoutDriveRouteBottom.setData(driveRoute);
                add(formLayoutDriveRouteBottom);
            }
        }
        Button editButton = new Button("Bearbeiten");
        editButton.setIcon(VaadinIcon.PENCIL.create());
        editButton.setClassName("own-drive-offers-edit-dialog-buttons");
        editButton.addClickListener(e -> {
            createEditButtons(driveRoute.getDriveType());
        });

        Button closeButton = new Button("Schließen");
        closeButton.setClassName("own-drive-offers-edit-dialog-buttons");
        closeButton.addClickListener(e -> close());

        defaultButtonLayout = new HorizontalLayout(editButton, closeButton);
        defaultButtonLayout.setClassName("own-drive-offers-edit-dialog-button_layout");

        add(defaultButtonLayout);
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private void createEditButtons(DriveType fahrtenTyp) {
        Button saveButton = new Button("Speichern");
        saveButton.setClassName("own-drive-offers-edit-dialog-buttons");

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setClassName("own-drive-offers-edit-dialog-buttons");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("own-drive-offers-edit-dialog-button_layout");
        buttonLayout.add(saveButton, cancelButton);

        remove(defaultButtonLayout);
        add(buttonLayout);

        switch (fahrtenTyp) {
            case OUTWARD_TRIP -> {
                formLayoutDriveRouteTop.setReadOnly(false);
                saveButton.addClickListener(event -> {
                    saveFormLayoutTop();
                });
                cancelButton.addClickListener(event -> {
                    formLayoutDriveRouteTop.setReadOnly(true);
                    remove(buttonLayout);
                    add(defaultButtonLayout);
                });
            }
            case RETURN_TRIP -> {
                formLayoutDriveRouteBottom.setReadOnly(true);
                saveButton.addClickListener(event -> {
                    saveFormLayoutBottom();
                });
                cancelButton.addClickListener(event -> {
                    formLayoutDriveRouteBottom.setReadOnly(true);
                    remove(buttonLayout);
                    add(defaultButtonLayout);
                });
            }

        }
        add(buttonLayout);
    }

    private void saveFormLayoutTop() {
        saveDrive(formLayoutDriveRouteTop.getAddress(),
                formLayoutDriveRouteTop.getFhLocation(),
                formLayoutDriveRouteTop.getDriveTime(),
                formLayoutDriveRouteTop.getCheckboxFuelParticipation(),
                formLayoutDriveRouteTop.getCarSeatCount(),
                DriveType.OUTWARD_TRIP,
                formLayoutDriveRouteTop.getDriveDateStart()
        );
    }

    private void saveFormLayoutBottom() {
        saveDrive(formLayoutDriveRouteBottom.getFhLocation(),
                formLayoutDriveRouteBottom.getAddress(),
                formLayoutDriveRouteBottom.getDriveTime(),
                formLayoutDriveRouteBottom.getCheckboxRegularDrive(),
                formLayoutDriveRouteBottom.getCarSeatCount(),
                DriveType.RETURN_TRIP,
                formLayoutDriveRouteBottom.getDriveDateStart()
        );
    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, boolean fuelParticipation, Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try {
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = driveRoute.getDriver();

            DriveRoute updateDriveRoute = new DriveRoute(
                    driveRoute.getId(),
                    new Start(firstAddress, driveTime.atDate(driveDate)),
                    new Destination(secondAddress, driveTime.atDate(driveDate)),
                    fuelParticipation,
                    carSeatCount,
                    user,
                    LocalDateTime.now(),
                    fahrtenTyp
            );

            driveRouteService.save(updateDriveRoute);

            UI.getCurrent().getPage().reload();
            this.close();

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
        }

    }

}
