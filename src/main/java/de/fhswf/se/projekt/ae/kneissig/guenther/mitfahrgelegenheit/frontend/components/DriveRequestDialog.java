package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.email.MailSender;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;

/**
 * Notifications auch oben ansiedeln oder unten und full wie labor ??
 */
@CssImport("/themes/mitfahrgelegenheit/components/drive-request-dialog.css")
public class DriveRequestDialog extends Dialog {

    public DriveRequestDialog(DriveRoute driveRoute) {

        setWidth("500px");

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
        buttonRequest.addClickListener(e->{
            try {
                if(textFieldAddress.getValue().isEmpty()) {
                    NotificationError.show("Abholadresse bitte angeben");
                    return;
                }
                MailSender.getInstance().sendMail(
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        driveRoute.getBenutzer().getUsername(),
                        textAreaMessage.getValue(),
                        "ramon.guenther@outlook.de",
                        driveRoute.getCurrentRouteLink()
                );

                close();

            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        });

        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setId("drive-request-dialog-cancel_button");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("drive-request-dialog-button_layout");
        buttonLayout.add(buttonRequest, buttonCancel);

        Div div = new Div(title,textFieldAddress,textAreaMessage,buttonLayout);

        add(div);
    }
}
