package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.EditNoteDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.BookingsView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.OwnDriveOffersView;

@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
@CssImport("/themes/mitfahrgelegenheit/views/dashboard-view.css")
public class DashboardView extends VerticalLayout {

    private final UserService userService;
    private final DriveRouteService driveRouteService;
    private final BookingService bookingService;

    private DriveRoute driverRoute;
    private Booking passengerRoute;
    private Label driverViewDateValue;
    private Label driverViewStartValue;
    private Label driverViewDestinationValue;
    private TextArea driverViewNoteTextArea;
    private Label passengerViewDriverValue;
    private Label passengerViewDateValue;
    private Label passengerViewStartValue;
    private Label passengerViewDestinationValue;
    private TextArea passengerViewNoteTextArea;
    private Button buttonNewNote;

    public DashboardView(UserService userService, DriveRouteService driveRouteService, BookingService bookingService){
        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.bookingService = bookingService;

        createDashboardView();
        passengerValues();
        driverValues();
    }

    private void createDashboardView(){
        H1 title = new H1("Meine nächsten Fahrten");

        /*-------------------------------------------------------------------------------------------------------------
                                                    Bereich Mitfahrt
        -------------------------------------------------------------------------------------------------------------*/
        H2 passengerTitle = new H2("Meine nächste Mitfahrt");
        Button buttonBookingsView = new Button("Meine Buchungen");
        buttonBookingsView.setId("dashboard-header-button-passenger");
        buttonBookingsView.addClickListener(event -> UI.getCurrent().navigate(BookingsView.class));
        HorizontalLayout headerPassengerViewLayout = new HorizontalLayout(passengerTitle, buttonBookingsView);

        Label passengerViewDriver = new Label("Fahrt von: ");
        passengerViewDriver.setClassName("label");
        Label passengerViewDate = new Label("Datum/Tag:");
        passengerViewDate.setClassName("label");
        Label passengerViewStart = new Label("Start:");
        passengerViewStart.setClassName("label");
        Label passengerViewDestination = new Label("Ziel:");
        passengerViewDestination.setClassName("label");

        VerticalLayout passengerLabels = new VerticalLayout(
                passengerViewDriver,
                passengerViewDate,
                passengerViewStart,
                passengerViewDestination);
        passengerLabels.setClassName("labels-vl");

        passengerViewDriverValue = new Label("-");
        passengerViewDriverValue.setClassName("value");
        passengerViewDateValue = new Label("-");
        passengerViewDateValue.setClassName("value");
        passengerViewStartValue = new Label("-");
        passengerViewStartValue.setClassName("value");
        passengerViewDestinationValue = new Label("-");
        passengerViewDestinationValue.setClassName("value");

        VerticalLayout passengerValues = new VerticalLayout(
                passengerViewDriverValue,
                passengerViewDateValue,
                passengerViewStartValue,
                passengerViewDestinationValue
        );
        passengerValues.setClassName("values-vl");

        Label labelPassengerNote = new Label("Letzte Notiz");
        labelPassengerNote.setClassName("label");
        passengerViewNoteTextArea = new TextArea();
        passengerViewNoteTextArea.setReadOnly(true);
        passengerViewNoteTextArea.setClassName("note");
        VerticalLayout passengerViewNote = new VerticalLayout(labelPassengerNote, passengerViewNoteTextArea);

        HorizontalLayout passengerViewLayout = new HorizontalLayout(passengerLabels, passengerValues, passengerViewNote);
        passengerViewLayout.setClassName("details-background");

        /*-------------------------------------------------------------------------------------------------------------
                                                    Bereich Fahrtangebot
        -------------------------------------------------------------------------------------------------------------*/

        H2 driverTitle = new H2("Meine nächste Fahrt");
        Button buttonDriverView = new Button("Meine Fahrten");
        buttonDriverView.setId("dashboard-header-button-driver");
        buttonDriverView.addClickListener(event -> UI.getCurrent().navigate(OwnDriveOffersView.class));
        HorizontalLayout headerDriverViewLayout = new HorizontalLayout(driverTitle, buttonDriverView);

        Label driverViewDate = new Label("Datum/Tag:");
        driverViewDate.setClassName("label");
        Label driverViewStart = new Label("Start:");
        driverViewStart.setClassName("label");
        Label driverViewDestination = new Label("Ziel:");
        driverViewDestination.setClassName("label");

        VerticalLayout driverLabels = new VerticalLayout(
                driverViewDate,
                driverViewStart,
                driverViewDestination);
        driverLabels.setClassName("labels-vl");

        driverViewDateValue = new Label("-");
        driverViewDateValue.setClassName("value");
        driverViewStartValue = new Label("-");
        driverViewStartValue.setClassName("value");
        driverViewDestinationValue = new Label("-");
        driverViewDestinationValue.setClassName("value");

        VerticalLayout driverValues = new VerticalLayout(
                driverViewDateValue,
                driverViewStartValue,
                driverViewDestinationValue
        );
        driverValues.setClassName("values-vl");

        Label labelDriverNote = new Label("Letzte Notiz");
        labelDriverNote.setClassName("label");
        buttonNewNote = new Button(VaadinIcon.PENCIL.create());
        buttonNewNote.setId("button-new-note");
        buttonNewNote.setEnabled(false);
        buttonNewNote.addClickListener(event -> {
            EditNoteDialog dialog = new EditNoteDialog(driveRouteService, driverRoute);
            dialog.open();
        });

        HorizontalLayout driverNoteHeader = new HorizontalLayout(labelDriverNote, buttonNewNote);
        driverNoteHeader.setId("note-header");

        driverViewNoteTextArea = new TextArea();
        driverViewNoteTextArea.setReadOnly(true);
        driverViewNoteTextArea.setClassName("note");
        VerticalLayout driverViewNote = new VerticalLayout(driverNoteHeader, driverViewNoteTextArea);

        HorizontalLayout driverViewLayout = new HorizontalLayout(driverLabels, driverValues, driverViewNote);
        driverViewLayout.setClassName("details-background");

        /*-------------------------------------------------------------------------------------------------------------
                                                          Allgemein
        -------------------------------------------------------------------------------------------------------------*/

        Div div = new Div(title, headerPassengerViewLayout, passengerViewLayout, headerDriverViewLayout, driverViewLayout);
        div.setClassName("content");

        add(div);
    }

    private void driverValues(){

        this.driverRoute = driveRouteService.findNextDriveRouteByUserComparedByTime(userService.getCurrentUser());

        if(driverRoute != null){
            switch (driverRoute.getDriveType()){
                case OUTWARD_TRIP -> {
                    driverViewDateValue.setText(driverRoute.getZiel().getFormattedDate() + ", " + driverRoute.getZiel().getFormattedTime());
                    driverViewStartValue.setText(driverRoute.getStart().getFullAddressToString());
                    driverViewDestinationValue.setText(driverRoute.getZiel().getFullAddressToString());
                    driverViewNoteTextArea.setValue(driverRoute.getNote());
                }
                case RETURN_TRIP -> {
                    driverViewDateValue.setText(driverRoute.getZiel().getFormattedDate() + ", " + driverRoute.getStart().getFormattedTime());
                    driverViewStartValue.setText(driverRoute.getStart().getFullAddressToString());
                    driverViewDestinationValue.setText(driverRoute.getZiel().getFullAddressToString());
                    driverViewNoteTextArea.setValue(driverRoute.getNote());
                }
            }
            buttonNewNote.setEnabled(true);
        }
    }

    private void passengerValues(){
        this.passengerRoute = bookingService.findNextBookingByUserComparedByTime(userService.getCurrentUser());

        if(passengerRoute != null){
            passengerViewDriverValue.setText(passengerRoute.getDriveRoute().getDriver().getFullName());
            passengerViewDateValue.setText(passengerRoute.getStopover().getFormattedDate() + ", " + passengerRoute.getStopover().getFormattedTime());
            passengerViewStartValue.setText(passengerRoute.getDriveRoute().getStart().getFullAddressToString());
            passengerViewDestinationValue.setText(passengerRoute.getDriveRoute().getZiel().getFullAddressToString());
            passengerViewNoteTextArea.setValue(passengerRoute.getDriveRoute().getNote());
        }
    }

}

