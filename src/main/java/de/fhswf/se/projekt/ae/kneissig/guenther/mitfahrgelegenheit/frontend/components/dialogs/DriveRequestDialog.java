package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.addressPatternCheck;

@CssImport("/themes/mitfahrgelegenheit/components/drive-request-dialog.css")
public class DriveRequestDialog extends Dialog {

    private DriveRequest driveRequest;

    public DriveRequestDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService, DriveRequestService driveRequestService, boolean isUserSearchsRegularDrive, LocalDate singleDriveDate) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        User currentUser = userService.getCurrentUser();

        H1 title = new H1("Fahrtanfrage stellen");
        title.setId("drive-request-dialog-title");

        TextFieldAddress textFieldAddress = new TextFieldAddress("Abholadresse");
        textFieldAddress.setRequiredIndicatorVisible(true);
        textFieldAddress.setId("drive-request-dialog-address");

        TextArea textAreaMessage = new TextArea("Nachricht");
        textAreaMessage.setId("drive-request-dialog-message");

        Button buttonRequest = new Button("Fahrt anfragen");
        buttonRequest.setId("drive-request-dialog-request_button");
        buttonRequest.addClickListener(e -> {
            try {
                if (textFieldAddress.getValue().isEmpty()) {
                    NotificationError.show("Abholadresse bitte angeben");
                    return;
                }

                addressPatternCheck(textFieldAddress.getValue());

                Address address = new Address(textFieldAddress.getPostal(), textFieldAddress.getPlace(), textFieldAddress.getStreet(), textFieldAddress.getNumber());

                List<Stopover> stopoverList = new ArrayList<>();

                stopoverList.add(new Stopover(address));

                for (Booking routeBooking : driveRoute.getBookings()) {
                    stopoverList.add(routeBooking.getStopover());
                }

                GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
                String googleMapsLink = googleDistanceCalculation.calculate(driveRoute.getStart(), driveRoute.getDestination(), stopoverList);

                //TODO: Vllt umdrehen das schon vorher bekannt ob anfrage schon gestellt bevor Google api gedöns
                driveRequest = new DriveRequest(driveRoute, currentUser, textAreaMessage.getValue(), googleMapsLink, new Stopover(address));

                /*  Wenn der User keine regelmäßige Fahrt sucht, aber eine Anfrage für eine Einzelfahrt bei einer regelmäßigen Fahrt stellt,
                    muss das gewünschte Datum für die Buchung später mit festgehalten werden. */
                if(!isUserSearchsRegularDrive && driveRoute.getRegularDrive().getRegularDriveDateEnd() != null){
                    driveRequest.setRegularDriveSingleDriveDate(singleDriveDate);
                }
                driveRoute.addDriveRequest(driveRequest);
                driveRequestService.save(driveRequest);
                driveRouteService.save(driveRoute);

                NotificationSuccess.show("Die Fahrt wurde angefragt");

                close();

//                mailService.sendSimpleMessage(
//                        currentUser.getFullName(),
//                        driveRoute.getBenutzer().getFirstName(),
//                        textAreaMessage.getValue(),
//                        driveRoute.getBenutzer().getEmail(),
//                        routeString.getRoute()
//                );
            } catch (DuplicateRequestException | InvalidAddressException ex) {
                if (Objects.equals(ex.getClass().getSimpleName(), "DuplicateRequestException")) {
                    NotificationError.show("Eine Anfrage für diese Fahrt wurde bereits gestellt.");
                } else if (Objects.equals(ex.getClass().getSimpleName(), "InvalidAddressException")) {
                    NotificationError.show("Keine gültige Adresse.");
                } else {
                    NotificationError.show("Unbekannter Fehler.");
                }
                ex.printStackTrace();
            } catch (IOException | InterruptedException | ApiException ioException) {
                ioException.printStackTrace();
            }

        });

        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.addClickListener(e -> close());
        buttonCancel.setId("drive-request-dialog-cancel_button");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("drive-request-dialog-button_layout");
        buttonLayout.add(buttonRequest, buttonCancel);

        VerticalLayout div = new VerticalLayout(title, textFieldAddress, textAreaMessage, buttonLayout);

        add(div);
    }
}
