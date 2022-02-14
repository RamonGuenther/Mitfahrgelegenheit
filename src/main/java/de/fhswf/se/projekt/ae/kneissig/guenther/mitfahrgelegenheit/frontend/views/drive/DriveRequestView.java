package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.CheckboxRegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

/**
 * TODO: Überarbeiten
 */

/**
 * Die Klasse DriveView erstellt eine View zum erstellen einer
 * Anfrage zu einer Fahrt.
 *
 * @author Ivonne Kneißig
 */

@Route(value = "fahrtanfrage", layout = MainLayout.class)
@PageTitle("Fahrtanfrage stellen")
@CssImport("/themes/mitfahrgelegenheit/views/drive-request-view.css")
public class DriveRequestView extends VerticalLayout {


    /**
     * Der Konstruktor ist für das Erstellen der View zum
     * Erstellen einer Anfrage zuständig.
     */
    public DriveRequestView(){
        setId("driveRequestView");
        createDriveRequestView();
    }

    /**
     * In der Methode createDriveRequestView werden die einzelnen Komponenten
     * der View erzeugt und zusammengefügt.
     */
    private void createDriveRequestView(){

        H1 title = new H1("Anfrage stellen");
        title.setId("titleDriveRequest");

        // linke Seite der View
        VerticalLayout route = new VerticalLayout();
        setId("requestLayoutRoute");

        Label labelRoute = new Label("Route des Fahrers");
        setId("labelDriverRoute");

        TextField textFieldStartAddress = new TextField("Start");
        textFieldStartAddress.setReadOnly(true);
        textFieldStartAddress.setClassName("requestTextfields");
        TextField textFieldDestinationAdress = new TextField("Ziel");
        textFieldDestinationAdress.setReadOnly(true);
        textFieldDestinationAdress.setClassName("requestTextfields");

////        Anchor driverRoute = new Anchor(new RouteString("Diesterwegstraße 6 58095 Hagen",
////                "Paschestraße 28 58089 Hagen").getRoute(), "Fahrerroute anzeigen");
//        driverRoute.removeAll();
//        Button buttonDriverRoute = new Button("Route des Fahrers anzeigen", new Icon(VaadinIcon.CAR));
//        buttonDriverRoute.addClassName("requestMapLinks");
//        driverRoute.add(buttonDriverRoute);
//        driverRoute.setTarget("_blank");
//        driverRoute.setClassName("requestRouteLinks");

        Label labelDetour = new Label("Umweg anfragen / Abholpunkt festlegen");
        setId("labelMap");

        TextField textFieldDetourAddress = new TextField("Straße / Hausnummer");
        textFieldDetourAddress.setClassName("requestTextfields");

        TextField textFieldDetourPLZ = new TextField("Postleitzahl");
        textFieldDetourPLZ.setClassName("requestTextFieldsPlace");
        TextField textFieldDetourPlace = new TextField("Ort");
        textFieldDetourPlace.setClassName("requestTextFieldsPlace");
        HorizontalLayout plzPlace = new HorizontalLayout(textFieldDetourPLZ, textFieldDetourPlace);

//        Anchor detourRoute = new Anchor(new RouteString("Diesterwegstraße 6 58095 Hagen",
//                "Paschestraße 28 58089 Hagen").getRoute(), "Route mit Umweg anzeigen");
//        detourRoute.removeAll();
//        Button buttonDetourRoute = new Button("Route mit Umweg anzeigen", new Icon(VaadinIcon.CAR));
//        buttonDetourRoute.addClassName("requestMapLinks");
//        detourRoute.add(buttonDetourRoute);
//        detourRoute.setTarget("_blank");
//        detourRoute.setClassName("requestRouteLinks");

//        route.add(title, labelRoute, textFieldStartAddress, textFieldDestinationAdress,
//                driverRoute, labelDetour, textFieldDetourAddress, plzPlace,
//                detourRoute);

        // rechte Seite der View
        Label labelRequestSettings = new Label("Anfragedetails");
        VerticalLayout form = new VerticalLayout();
        form.setId("layoutRequestForm");

        CheckboxRegularDrive checkboxRegularDrive = new CheckboxRegularDrive();
        checkboxRegularDrive.setId("checkboxRegularDrive");

        HorizontalLayout periodOfTime = new HorizontalLayout();
        periodOfTime.setId("layoutPeriodOfTime");
        DatePicker dateFrom = new DatePicker("Zeitraum von");
        dateFrom.setId("dateFrom");
        DatePicker dateTo = new DatePicker("Bis");
        dateTo.setId("dateTo");
        periodOfTime.add(dateFrom, dateTo);

        TextArea textAreaMessage = new TextArea("Nachricht");
        textAreaMessage.setId("textAreaMessage");
        form.add(labelRequestSettings,checkboxRegularDrive, periodOfTime, textAreaMessage);

        // Zusammensetzen der gesamten View
        HorizontalLayout requestForm = new HorizontalLayout();
        requestForm.setId("layoutRequestFormFull");
        requestForm.add(route, form);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setId("layoutButtons");
        Button buttonRequest = new Button("Fahrt anfragen");
        buttonRequest.setClassName("buttonRequest");
        buttonRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setClassName("buttonRequest");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttons.add(buttonRequest, buttonCancel);

        add(requestForm, buttons);
    }

}
