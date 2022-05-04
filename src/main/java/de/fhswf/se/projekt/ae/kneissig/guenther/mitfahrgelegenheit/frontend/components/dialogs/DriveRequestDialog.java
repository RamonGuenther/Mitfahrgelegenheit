package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse DriveRequestDialog erstellt einen Dialog, auf dem der Benutzer
 * bei einer Fahrtanfrage die gewünschte Abholadresse und ggf. eine Nachricht an
 * den Fahrer angeben kann.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/drive-request-dialog.css")
public class DriveRequestDialog extends Dialog {

    private DriveRequest newDriveRequest;

    public DriveRequestDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService, DriveRequestService driveRequestService, boolean isUserSearchsRegularDrive, LocalDate singleDriveDate) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        User currentUser = userService.getCurrentUser();

        H1 title = new H1("Fahrtanfrage stellen");
        title.setId("drive-request-dialog-title");

        TextFieldAddress textFieldAddress = new TextFieldAddress("Abholadresse");
        textFieldAddress.setRequiredIndicatorVisible(true);
        textFieldAddress.setId("drive-request-dialog-address");
        textFieldAddress.setValue(currentUser.getAddress().toString());
        textFieldAddress.setErrorMessage("Abholadresse bitte angeben");

        TextArea textAreaMessage = new TextArea("Nachricht an den Fahrer (optional)");
        textAreaMessage.setId("drive-request-dialog-message");

        Button buttonRequest = new Button("Fahrt anfragen");
        buttonRequest.setId("drive-request-dialog-request_button");
        buttonRequest.addClickListener(e -> {
            try {
                if (textFieldAddress.getValue().isEmpty()) {
                    textFieldAddress.setInvalid(true);
                    NotificationError.show("Abholadresse bitte angeben");
                    return;
                }

                AddressConverter addressConverter = new AddressConverter(textFieldAddress.getValue());

                Address address = new Address(addressConverter.getPostalCode(), addressConverter.getPlace(), addressConverter.getStreet(), addressConverter.getNumber());

                List<Stopover> stopoverList = new ArrayList<>();

                stopoverList.add(new Stopover(address));

                for (Booking routeBooking : driveRoute.getBookings()) {
                    stopoverList.add(routeBooking.getStopover());
                }

                GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
                String googleMapsLink = googleDistanceCalculation.calculate(driveRoute.getStart(), driveRoute.getDestination(), stopoverList);

                newDriveRequest = new DriveRequest(driveRoute, currentUser, textAreaMessage.getValue(), googleMapsLink, new Stopover(address));

                /*  Wenn der User keine regelmäßige Fahrt sucht, aber eine Anfrage für eine Einzelfahrt bei einer regelmäßigen Fahrt stellt,
                    muss das gewünschte Datum für die Buchung später mit festgehalten werden. */
                if (!isUserSearchsRegularDrive && driveRoute.getRegularDrive().getRegularDriveDateEnd() != null) {
                    newDriveRequest.setRegularDriveSingleDriveDate(singleDriveDate);
                }

                driveRoute.addDriveRequest(newDriveRequest);
                driveRequestService.save(newDriveRequest);
                driveRouteService.save(driveRoute);

                NotificationSuccess.show("Die Fahrt wurde angefragt");

                close();

                mailService.sendDriveRequestMail(
                        currentUser.getFullName(),
                        driveRoute.getDriver().getFirstName(),
                        textAreaMessage.getValue(),
                        driveRoute.getDriver().getEmail(),
                        googleMapsLink
                );
            } catch (DuplicateRequestException | InvalidAddressException ex) {
                NotificationError.show(ex.getMessage());
                ex.printStackTrace();
            } catch (IOException | InterruptedException | ApiException | MessagingException otherException) {
                otherException.printStackTrace();
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
