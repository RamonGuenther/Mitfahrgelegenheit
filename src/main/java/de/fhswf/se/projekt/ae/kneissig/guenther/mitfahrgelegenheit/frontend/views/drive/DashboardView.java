package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
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

    private static final String SINGLE_DRIVE = "Einzelfahrten";
    private static final String REGULAR_DRIVE = "Regelmäßige Fahrten";

    private final UserService userService;
    private final DriveRouteService driveRouteService;
    private final BookingService bookingService;

    private DriveRoute driverSingleRoute;
    private Booking passengerSingleRoute;
    private DriveRoute driverRegularRoute;
    private Booking passengerRegularRoute;
    private Label driverViewDateValue;
    private Label driverViewStartValue;
    private Label driverViewDestinationValue;
    private TextArea driverViewNoteTextArea;
    private Anchor passengerViewDriverValue;
    private Label passengerViewDateValue;
    private Label passengerViewStartValue;
    private Label passengerViewDestinationValue;
    private TextArea passengerViewNoteTextArea;
    private Button buttonNewNote;


    public DashboardView(UserService userService, DriveRouteService driveRouteService, BookingService bookingService) {
        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.bookingService = bookingService;

        createDashboardView();
        passengerSingleDriveValues();
        driverSingleDriveValues();
    }

    private void createDashboardView() {
        H1 title = new H1("Meine nächsten Fahrten");
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(SINGLE_DRIVE, REGULAR_DRIVE);
        radioButtonGroup.setValue(SINGLE_DRIVE);
        radioButtonGroup.addValueChangeListener(event -> {
            switch (event.getValue()){
                case SINGLE_DRIVE -> {
                    passengerSingleDriveValues();
                    driverSingleDriveValues();
                }
                case REGULAR_DRIVE -> {
                    passengerRegularDriveValues();
                    driverRegularDriveValues();
                }
            }
        });

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
        Label passengerViewDate = new Label("Tag/Zeit:");
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

        passengerViewDriverValue = new Anchor();
        passengerViewDriverValue.setText("-");
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
        labelPassengerNote.setClassName("note-label");
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

        Label driverViewDate = new Label("Tag/Zeit:");
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
        labelDriverNote.setClassName("note-label");
        buttonNewNote = new Button(VaadinIcon.PENCIL.create());
        buttonNewNote.setId("button-new-note");
        buttonNewNote.setEnabled(false);
        buttonNewNote.addClickListener(event -> {
            EditNoteDialog dialog = new EditNoteDialog(driveRouteService, driverSingleRoute);
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

        Div div = new Div(title, radioButtonGroup, headerPassengerViewLayout, passengerViewLayout, headerDriverViewLayout, driverViewLayout);
        div.setClassName("content");

        add(div);
    }

    private void driverSingleDriveValues() {

        driveRouteService.getNextSingleDriveRouteByUser(userService.getCurrentUser()).ifPresent(driveRoute -> this.driverSingleRoute = driveRoute);

        if (driverSingleRoute != null) {
            driverViewDateValue.setText(driverSingleRoute.getFormattedDate() + ", " + driverSingleRoute.getFormattedTime());
            setBasicDriverValues(driverSingleRoute);
        }
        else{
            clearDriverValues();
        }
    }
    
    private void passengerSingleDriveValues() {
        bookingService.getNextSingleDriveBookingByPassenger(userService.getCurrentUser()).ifPresent(passengerRoute -> this.passengerSingleRoute = passengerRoute);

        if (passengerSingleRoute != null) {
            passengerViewDateValue.setText(passengerSingleRoute.getRegularDriveSingleDriveDate() == null ?
                    passengerSingleRoute.getDriveRoute().getFormattedDate() + ", " + passengerSingleRoute.getDriveRoute().getFormattedTime() :
                    passengerSingleRoute.getFormattedSingleDriveDate() + ", " + passengerSingleRoute.getDriveRoute().getFormattedTime());
            setBasicPassengerValues(passengerSingleRoute);
        }
        else{
            clearPassengerValues();
        }
    }

    private void driverRegularDriveValues(){
        driveRouteService.getNextRegularDriveRouteByUser(userService.getCurrentUser()).ifPresent(driveRoute -> this.driverRegularRoute = driveRoute);

        if (driverRegularRoute != null) {
            driverViewDateValue.setText(driverRegularRoute.getRegularDrive().getRegularDriveDay().label + ", " + driverRegularRoute.getFormattedTime());
            setBasicDriverValues(driverRegularRoute);
        }
        else{
            clearDriverValues();
        }
    }

    private void passengerRegularDriveValues(){
        bookingService.getNextRegularDriveBookingByPassenger(userService.getCurrentUser()).ifPresent(passengerRoute -> this.passengerRegularRoute = passengerRoute);

        if (passengerRegularRoute != null) {
            passengerViewDateValue.setText(passengerRegularRoute.getDriveRoute().getRegularDrive().getRegularDriveDay().label + ", " + passengerRegularRoute.getDriveRoute().getFormattedTime());
            setBasicPassengerValues(passengerRegularRoute);
        }
        else{
            clearPassengerValues();
        }
    }

    private void setBasicPassengerValues(Booking booking){
        passengerViewDriverValue.setHref("/profil/" + booking.getDriveRoute().getDriver().getUsername());
        passengerViewDriverValue.setText(booking.getDriveRoute().getDriver().getFullName());
        passengerViewStartValue.setText(booking.getDriveRoute().getStart().getFullAddressToString());
        passengerViewDestinationValue.setText(booking.getDriveRoute().getDestination().getFullAddressToString());
        passengerViewNoteTextArea.setValue(booking.getDriveRoute().getNote());
    }

    private void setBasicDriverValues(DriveRoute driveRoute){
        driverViewStartValue.setText(driveRoute.getStart().getFullAddressToString());
        driverViewDestinationValue.setText(driveRoute.getDestination().getFullAddressToString());
        driverViewNoteTextArea.setValue(driveRoute.getNote());
        buttonNewNote.setEnabled(true);
    }

    private void clearPassengerValues(){
        passengerViewDateValue.setText("-");
        passengerViewDriverValue.setHref("");
        passengerViewDriverValue.setText("-");
        passengerViewStartValue.setText("-");
        passengerViewDestinationValue.setText("-");
        passengerViewNoteTextArea.setValue("");
    }

    private void clearDriverValues(){
        driverViewDateValue.setText("-");
        driverViewStartValue.setText("-");
        driverViewDestinationValue.setText("-");
        driverViewNoteTextArea.setValue("");
        buttonNewNote.setEnabled(false);
    }
}

