package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;

/**
 * Die Klasse EditNoteDialog erstellt einen Dialog, mit dem die Notiz zu einem
 * Fahrtangebot bearbeitet werden kann.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/edit-note-dialog.css")
public class EditNoteDialog extends Dialog {

    public EditNoteDialog (DriveRouteService driveRouteService, DriveRoute driveRoute){

        setId("edit-note-dialog");

        H1 title = new H1("Notiz bearbeiten");
        title.setId("edit-note-title");

        TextArea note = new TextArea("Notiz");
        note.setId("edit-note-textarea");
        note.setValue(driveRoute.getNote() != null ? driveRoute.getNote() : "");

        Button buttonSaveNote = new Button("Speichern");
        buttonSaveNote.setClassName("edit-note-buttons");
        buttonSaveNote.addClickListener(event -> {

            DriveRoute routeWithNewNote = new DriveRoute(
                    driveRoute.getId(),
                    driveRoute.getStart(),
                    driveRoute.getDestination(),
                    driveRoute.getDrivingTime(),
                    driveRoute.isFuelParticipation(),
                    driveRoute.getSeatCount(),
                    driveRoute.getDriver(),
                    driveRoute.getCreationDate(),
                    driveRoute.getDriveType(),
                    note.getValue()
            );
            driveRouteService.save(routeWithNewNote);
            this.close();
            UI.getCurrent().getPage().reload();
        });
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setClassName("edit-note-buttons");
        buttonCancel.addClickListener(event -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonSaveNote, buttonCancel);
        buttonLayout.setId("edit-notes-button-layout");

        VerticalLayout verticalLayout = new VerticalLayout(
                title,
                note,
                buttonLayout);

        add(verticalLayout);
    }
}
