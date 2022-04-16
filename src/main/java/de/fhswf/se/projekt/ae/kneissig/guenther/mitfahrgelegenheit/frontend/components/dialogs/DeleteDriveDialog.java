package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;

import javax.mail.MessagingException;
import java.sql.Driver;
import java.util.List;

@CssImport("/themes/mitfahrgelegenheit/components/delete-dialog.css")
public class DeleteDriveDialog extends Dialog {
    public DeleteDriveDialog(DriveRoute driveRoute, DriveRouteService driveRouteService, MailService mailService) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        H2 header = new H2("Achtung!");
        header.setId("delete-dialog-header");

        TextArea optionalMessage = new TextArea("Nachricht an die Mitfahrer (optional)");
        optionalMessage.setId("delete-drive-dialog-optional_message");

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Ich bin mir sicher, dass ich dieses Fahrtangebot löschen möchte.");
        checkbox.setId("delete-dialog-checkbox");

        Button acceptButton = new Button("Akzeptieren");
        acceptButton.setClassName("delete-dialog-buttons");
        acceptButton.setEnabled(false);

        acceptButton.addClickListener(e -> {
//            try {
                driveRouteService.delete(driveRoute);
//                mailService.sendDriveDeleteMessage(driveRoute, optionalMessage.getValue());
//            } catch (MessagingException ex) {
//                ex.printStackTrace();
//            }
            UI.getCurrent().getPage().reload();
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setClassName("delete-dialog-buttons");
        cancelButton.addClickListener(e -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(acceptButton, cancelButton);
        buttonLayout.setId("delete-dialog-button_layout");


        VerticalLayout div = new VerticalLayout(header, optionalMessage, checkbox, buttonLayout);

        add(div);

        checkbox.addValueChangeListener(event -> acceptButton.setEnabled(event.getValue()));

    }
}
