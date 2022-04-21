package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.RatingDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;


/**
 * Die Klasse CompletedDriveView erstellt eine View für das anzeigen
 * der abgeschlossenen Fahrten, aus Fahrer- und Mitfahrersicht, um
 * eine Person zu bewerten oder zu melden.
 */
@com.vaadin.flow.router.Route(value = "abgeschlosseneFahrten", layout = MainLayout.class)
@PageTitle("Abgeschlossene Fahrten")
@CssImport("/themes/mitfahrgelegenheit/views/completed-drive-view.css")
public class CompletedDriveView extends VerticalLayout {

    private static final String OFFERED_DRIVES = "angebotene Fahrten";
    private static final String BOOKED_DRIVES = "mitgefahrene Fahrten";

    private final UserService userService;
    private final DriveRouteService driveRouteService;
    private final BookingService bookingService;

    private final User user;
    private Grid<Booking> completedDrivesGrid;
    private RadioButtonGroup<String> radioButtonGroup;
    private RatingDialog ratingDialog;
    private List<Booking> completedDriveListDriver;
    private List<Booking> completedDriveListPassenger;


    /**
     * Der Konstruktor ist für das Erstellen der View zuständig
     */
    CompletedDriveView(UserService userService, DriveRouteService driveRouteService, BookingService bookingService) {
        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.bookingService = bookingService;
        this.user = userService.getCurrentUser();

        driveRouteService.cleanCompletedDriveRoutesByUser(user);

        createCompletedDriveView();
    }

    /**
     * In der Methode createCompletedDriveView werden die Methoden
     * zum hinzufügen der Komponenten für das Layout, aufgerufen.
     */
    private void createCompletedDriveView() {

        H1 title = new H1("Abgeschlossene Fahrten");

        completedDriveListDriver = bookingService.getCompletedDriveRoutesByDriver(this.user).orElse(Collections.emptyList());
        completedDriveListPassenger = bookingService.getCompletedDriveRoutesByPassenger(this.user).orElse(Collections.emptyList());

        radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(OFFERED_DRIVES, BOOKED_DRIVES);
        radioButtonGroup.setValue(OFFERED_DRIVES);
        radioButtonGroup.setClassName("radiobutton-group");
        radioButtonGroup.addValueChangeListener(event -> {
            switch (event.getValue()) {
                case OFFERED_DRIVES -> completedDrivesGrid.setItems(completedDriveListDriver);
                case BOOKED_DRIVES -> completedDrivesGrid.setItems(completedDriveListPassenger);
            }
        });

        completedDrivesGrid = new Grid<>();
        completedDrivesGrid.setItems(completedDriveListDriver);

        completedDrivesGrid.addColumn(new LocalDateTimeRenderer<>(item -> item.getDriveRoute().getDrivingTime(),
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader("Tag / Uhrzeit");
        completedDrivesGrid.addColumn(booking -> booking.getDriveRoute().getStart().getFullAddressToString()).setHeader("Start");
        completedDrivesGrid.addColumn(booking -> booking.getDriveRoute().getDestination().getFullAddressToString()).setHeader("Ziel");
        completedDrivesGrid.addComponentColumn(booking ->
                radioButtonGroup.getValue().equals(OFFERED_DRIVES) ?
                    new Anchor("/profil/" + booking.getPassenger().getUsername(), booking.getPassenger().getFirstName()) :
                    new Anchor("/profil/" + booking.getDriveRoute().getDriver().getUsername(), booking.getDriveRoute().getDriver().getFullName()))
                .setHeader("Benutzer");
        completedDrivesGrid.addComponentColumn(this::createRatingButton).setHeader("Bewertung");
        completedDrivesGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        Div div = new Div(title, radioButtonGroup, completedDrivesGrid);
        div.setClassName("content");

        add(div);
    }


    private Button createRatingButton(Booking booking){
        Icon icon = new Icon(VaadinIcon.STAR);
        Button button = new Button("Bewerten");
        button.setIcon(icon);

        button.addClickListener(event -> {
            switch (radioButtonGroup.getValue()){
                case OFFERED_DRIVES -> ratingDialog = new RatingDialog(userService,
                        booking.getPassenger(),
                        driveRouteService,
                        bookingService,
                        booking,
                        Role.PASSENGER);
                case BOOKED_DRIVES -> ratingDialog = new RatingDialog(userService,
                        booking.getDriveRoute().getDriver(),
                        driveRouteService,
                        bookingService,
                        booking,
                        Role.DRIVER);
            }
            ratingDialog.open();
        });

        return button;
    }
}
