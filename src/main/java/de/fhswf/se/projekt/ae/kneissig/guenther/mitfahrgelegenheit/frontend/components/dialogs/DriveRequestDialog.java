package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateRequestException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.addressPatternCheck;

@CssImport("/themes/mitfahrgelegenheit/components/drive-request-dialog.css")
public class DriveRequestDialog extends Dialog {

    public DriveRequestDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService, DriveRequestService driveRequestService) {
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

                List<Stopover> stopoverList = new ArrayList<>();
                Address address = new Address(textFieldAddress.getPostal(), textFieldAddress.getPlace(), textFieldAddress.getStreet(), textFieldAddress.getNumber());

                stopoverList.add(new Stopover(address, LocalDateTime.now()));

                RouteString routeString = new RouteString(driveRoute.getStart(), driveRoute.getZiel(), stopoverList);

                DriveRequest driveRequest = new DriveRequest(driveRoute, RequestState.OPEN, currentUser, textAreaMessage.getValue(), "Apfel", LocalDateTime.now(), new Stopover(new Address(), null));
                driveRoute.addDriveRequest(driveRequest);
                driveRequestService.save(driveRequest);
                driveRouteService.save(driveRoute);

                close();

//                mailService.sendSimpleMessage(
//                        currentUser.getFullName(),
//                        driveRoute.getBenutzer().getFirstName(),
//                        textAreaMessage.getValue(),
//                        driveRoute.getBenutzer().getEmail(),
//                        routeString.getRoute()
//                );
            } catch (DuplicateRequestException | InvalidAddressException ex) {
                if(Objects.equals(ex.getClass().getSimpleName(), "DuplicateRequestException")){
                    NotificationError.show("Eine Anfrage für diese Fahrt wurde bereits gestellt.");
                }
                else if(Objects.equals(ex.getClass().getSimpleName(), "InvalidAddressException")){
                    NotificationError.show("Keine gültige Adresse.");
                }
                else{
                    NotificationError.show("Unbekannter Fehler.");
                }
                ex.printStackTrace();
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
