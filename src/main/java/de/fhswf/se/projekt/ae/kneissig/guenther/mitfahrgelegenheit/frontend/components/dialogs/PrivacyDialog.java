package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("/themes/mitfahrgelegenheit/components/privacy-dialog.css")
public class PrivacyDialog extends Dialog {

    public PrivacyDialog() {

        setWidth("1000px");

        H2 title = new H2("Datenschutzerklärung");

        H4 span = new H4("Seit dem 25. Mai 2018 gilt die neue DSGVO der Europäischen Union. Um diese Applikation" +
                " nutzen zu können, lesen Sie bitte die unten aufgeführte Datenschutzrichtlinie sorgfältig. Falls Sie nicht " +
                "mit dieser einverstanden sind, können Sie diese Plattform nicht mehr nutzen.");
        span.setId("privacy-dialog");

        H4 span1 = new H4("Datenschutzerklärung: \n");
        span1.setId("privacy-dialog");


        H4 span2 = new H4("Die Fachhochschule Südwestfalen bietet Studenten der Hochschule mit dieser Web-Applikation," +
                " die Möglichkeit, schnell und einfach Fahrgemeinschaften zu bilden und Mitfahrgelegenheiten anzubieten bzw." +
                " zu nutzen.");
        span2.setId("privacy-dialog");

        H4 span3 = new H4("Ihre Daten:");
        span3.setId("privacy-dialog");

        H4 h4 = new H4("Benutzername, Vorname, Nachname, FH-Standort, Fachbereich, Adresse und E-Mail-Adresse werden gespeichert, " +
                "um Sie identifizieren zu können und um die Web-Applikation in voller Funktionsweise Nutzen zu können. \n" +
                "Ihr Nachname und die genaue Adresse sind für andere Nutzer nicht direkt bzw. nur in gekürzter Form sichtbar. " +
                " Sie können Start- und Zieladressen zum Anlegen von Fahrtangeboten oder Abholadressen bei Anfragen auf Wunsch so anpassen, " +
                " dass es sich nicht um Ihre direkte Wohnadresse handelt.");

        h4.setId("privacy-dialog");


        H4 h41 = new H4("Sie können Ihre Einwilligung jederzeit für die Zukunft widerrufen.\n" +
                "Soweit die Einwilligung nicht widerrufen wird, gilt sie bis zu Ihrer Exmatrikulation.");
        h41.setId("privacy-dialog");

        H4 h42 = new H4("Der Widerspruch ist zu richten an Herrn Prof. Dr. Uwe Klug, [klug.uwe@fh-swf.de], Frauenstuhlweg 31 58644 Iserlohn.\n");

        Button closeButton = new Button("Schließen");
        closeButton.setId("privacy-dialog-close_button");
        closeButton.addClickListener(e-> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
        buttonLayout.setId("privacy-dialog-button_layout");

        Div div = new Div(title, span, span1, span2, span3, h4, h41, h42, buttonLayout);

        add(div);

    }
}
