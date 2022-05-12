package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse DeleteBookingDialog erstellt einen Dialog für den Fall,
 * dass ein Benutzer eine Mitfahrt löschen möchte. Mit dem Dialog
 * muss der Benutzer noch einmal bestätigen sich sicher zu sein, dass
 * er die Mitfahrt wirklich absagen möchte.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/delete-dialog.css")
public class DeleteBookingDialog extends Dialog {
    public DeleteBookingDialog(DriveRouteService driveRouteService, MailService mailService, BookingService bookingService, Booking booking) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        H2 header = new H2("Achtung!");
        header.setId("delete-dialog-header");

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Ich bin mir sicher, dass ich nicht mehr mitfahren möchte.");
        checkbox.setId("delete-dialog-checkbox");

        Button acceptButton = new Button("Akzeptieren");
        acceptButton.setClassName("delete-dialog-buttons");
        acceptButton.setEnabled(false);

        acceptButton.addClickListener(e -> {
            DriveRoute driveRoute = driveRouteService.findById(booking.getDriveRoute().getId()).get();
            String passenger = booking.getPassenger().getFullName();

            try {
                driveRoute.removeBooking(booking);

                List<Stopover> stopoverList = new ArrayList<>();

                for (Booking routeBooking : driveRoute.getBookings()) {
                    stopoverList.add(routeBooking.getStopover());
                }

                GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
                String result = googleDistanceCalculation.calculate(driveRoute.getStart(), driveRoute.getDestination(), stopoverList, driveRoute.getDriveType());

                driveRoute.setCurrentRouteLink(result);

                driveRouteService.save(driveRoute);
                bookingService.delete(booking);
                mailService.sendBookingCancellation(driveRoute, passenger);

                close();
                UI.getCurrent().getPage().reload();
            } catch (IOException | InterruptedException | InvalidAddressException | ApiException |
                     MessagingException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setClassName("delete-dialog-buttons");
        cancelButton.addClickListener(e -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(acceptButton, cancelButton);
        buttonLayout.setId("delete-dialog-button_layout");

        VerticalLayout verticalLayout = new VerticalLayout(header, checkbox, buttonLayout);

        add(verticalLayout);

        checkbox.addValueChangeListener(event -> acceptButton.setEnabled(event.getValue()));
    }
}
