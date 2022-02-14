package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.completeddrive;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

/**
 * Die Klasse ReportButton erstellt einen Button für die CompletedDriveView,
 * um Nutzer zu melden.
 *
 * @author Ramon Günther
 */
public class ReportButton extends Button {

    /**
     * Der Konstruktor erstellt den Button mit den geforderten
     * Attributen.
     */
    public ReportButton(){
        setText("Melden");
        setClassName("buttonsCompletedDrive");
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        addClickListener(event -> {
//            reportButtonEvent();
//        });

    }

    /**
     * Die Methode ratingButtonEvent löst das Event aus, um Nutzer
     * zu melden.
     */
    public void reportButtonEvent(){
        Notification notification = new Notification( "",3000);

        Label labelDialogReport = new Label("Benutzer Melden");

        TextArea textAreaMessage = new TextArea("Nachricht");
        textAreaMessage.setId("textAreaCompletedDrive");
        textAreaMessage.setPlaceholder("Bitte den Grund der Meldung angeben!"); //wenn Textfeld leer ist kein Speichern möglich

        Button saveReport = new Button("Speichern");
        saveReport.setClassName("buttonsReport");

        Button cancelReport = new Button("Abbrechen");
        cancelReport.setClassName("buttonsReport");
        HorizontalLayout buttonLayoutReport = new HorizontalLayout();
        buttonLayoutReport.setId("buttonLayoutReport");
        buttonLayoutReport.add(saveReport,cancelReport);
        Div div = new Div();
        div.add(labelDialogReport,textAreaMessage,buttonLayoutReport);

        Dialog dialog = new Dialog();
        dialog.add(div);
        dialog.setWidth("500px");
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();

        saveReport.addClickListener(saveEvent -> {
            if(!textAreaMessage.isEmpty()) {
                notification.setText("Benutzer wurde gemeldet.");
                notification.open();
                dialog.close();
            }
            else{
                notification.setText("Bitte den Grund der Meldung angeben!");
                notification.open();
            }

        });

        cancelReport.addClickListener(deleteEvent -> {
            dialog.close();
        });
    }
}
