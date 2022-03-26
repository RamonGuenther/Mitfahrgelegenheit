package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;

import javax.mail.MessagingException;

/**
 *      Todo:
 *             -Fahrtanfrage darf nicht mehrmals von einer Person möglich sein, siehe Sebastian krassen shit
 */
@CssImport("/themes/mitfahrgelegenheit/components/drive-request-dialog.css")
public class DriveRequestDialog extends Dialog {

    public DriveRequestDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService) {

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
        buttonRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonRequest.addClickListener(e -> {
            try {
                if (textFieldAddress.getValue().isEmpty()) {
                    NotificationError.show("Abholadresse bitte angeben");
                    return;
                }
                //TODO hier muss dann der neue RouteLink erstellt werden für DriveRequest mithilfe von GoogleDistanceCalculation und String gedöns
                // erst beim annehmen der Anfrage wird die Url in DriveRoute gespeichert
                // Fahrtanfrage darf nicht mehrmals von einer Person möglich sein, siehe Sebastian krassen shit

                DriveRequest driveRequest = new DriveRequest(RequestState.OPEN,currentUser,textAreaMessage.getValue(),"Apfel");

                driveRoute.addDriveRequest(driveRequest);

                driveRouteService.save(driveRoute);
                
                close();

                mailService.sendSimpleMessage(
                        currentUser.getFullName(),
                        driveRoute.getBenutzer().getFirstName(),
                        textAreaMessage.getValue(),
                        driveRoute.getBenutzer().getEmail(),
                        driveRoute.getCurrentRouteLink()
                );
            } catch (IllegalArgumentException | MessagingException ex) {
                ex.printStackTrace();
            }

        });

        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setId("drive-request-dialog-cancel_button");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("drive-request-dialog-button_layout");
        buttonLayout.add(buttonRequest, buttonCancel);

        VerticalLayout div = new VerticalLayout(title, textFieldAddress, textAreaMessage, buttonLayout);

        add(div);
    }
}
