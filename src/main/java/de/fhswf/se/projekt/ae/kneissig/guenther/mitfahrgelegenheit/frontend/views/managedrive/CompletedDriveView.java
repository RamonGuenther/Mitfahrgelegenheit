package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
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
 * Die Klasse CompletedDriveView erstellt eine View für das Anzeigen
 * der abgeschlossenen Fahrten, aus Fahrer- und Mitfahrersicht, um
 * eine Person zu bewerten oder zu melden.
 *
 * @author Ramon Günther & Ivonne Kneißig
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
        UI.getCurrent().setId(PageId.COMPLETED_DRIVE_VIEW.label);

        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.bookingService = bookingService;
        this.user = userService.getCurrentUser();

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
                case OFFERED_DRIVES:
                    completedDrivesGrid.setItems(completedDriveListDriver);
                    completedDrivesGrid.getColumns().get(3).setHeader("Mitfahrer");
                    break;
                case BOOKED_DRIVES:
                    completedDrivesGrid.setItems(completedDriveListPassenger);
                    completedDrivesGrid.getColumns().get(3).setHeader("Fahrer");
                    break;
            }
        });

        completedDrivesGrid = new Grid<>();
        completedDrivesGrid.setItems(completedDriveListDriver);
        completedDrivesGrid.addColumn(this::setDateTimeColumn).setHeader("Tag / Uhrzeit");
        completedDrivesGrid.addColumn(this::setStartAddressColumn).setHeader("Von");
        completedDrivesGrid.addColumn(this::setDestinationColumn).setHeader("Nach");
        completedDrivesGrid.addComponentColumn(booking ->
                radioButtonGroup.getValue().equals(OFFERED_DRIVES) ?
                    new Anchor("/drivetogether/profil/" + booking.getPassenger().getId(), booking.getPassenger().getFullName()) :
                    new Anchor("/drivetogether/profil/" + booking.getDriveRoute().getDriver().getId(), booking.getDriveRoute().getDriver().getFullName()))
                .setHeader("Benutzer");
        completedDrivesGrid.addComponentColumn(this::createRatingButton).setHeader("Bewertung");
        completedDrivesGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        Div div = new Div(title, radioButtonGroup, completedDrivesGrid);
        div.setClassName("content");

        add(div);
    }

    /**
     * Die Methode createRatingButton erstellt den Button zum Bewerten eines Fahrers bzw.
     * Mitfahrers
     *
     * @param booking       Buchung zu der Fahrt, die bewertet werden soll
     * @return              Button zum Bewerten des Fahrers/Mitfahrers
     */
    private Button createRatingButton(Booking booking){
        Icon icon = new Icon(VaadinIcon.STAR);
        Button button = new Button("Bewerten");
        button.setIcon(icon);

        button.addClickListener(event -> {
            switch (radioButtonGroup.getValue()) {
                case OFFERED_DRIVES:
                    ratingDialog = new RatingDialog(userService,
                            booking.getPassenger(),
                            driveRouteService,
                            bookingService,
                            booking,
                            Role.PASSENGER);
                    break;
                case BOOKED_DRIVES:
                    ratingDialog = new RatingDialog(userService,
                            booking.getDriveRoute().getDriver(),
                            driveRouteService,
                            bookingService,
                            booking,
                            Role.DRIVER);
                    break;
            }
            ratingDialog.open();
        });
        return button;
    }

    /**
     * Die Methode setDateTimeColumn ist für die Spalte zur Anzeige des Tages zuständig, an dem die
     * gebuchte Fahrt stattgefunden hat. Je nachdem ob es sich dabei um eine Einzelfahrt, eine Einzelfahrt bei
     * einer regelmäßigen Fahrt oder eine regelmäßige Fahrt handelt, wird entweder das Datum der Fahrt
     * oder der Wochentag angezeigt.
     *
     * @param booking           Buchung, dessen Datum oder Wochentag der Fahrt angezeigt werden soll.
     * @return                  Datum oder Wochentag der Fahrt
     */
    private String setDateTimeColumn(Booking booking){

        String dateTime = "";

        if(booking.getRegularDriveSingleDriveDate() == null && booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() == null){
            dateTime = booking.getDriveRoute().getFormattedDate() + ", " + booking.getDriveRoute().getFormattedTime();
        }
        else if(booking.getRegularDriveSingleDriveDate() != null){
            dateTime = booking.getRegularDriveSingleDriveDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ", " + booking.getDriveRoute().getFormattedTime();
        }
        else if(booking.getRegularDriveSingleDriveDate() == null && booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() != null){
            dateTime = booking.getDriveRoute().getRegularDrive().getRegularDriveDay().label + ", " + booking.getDriveRoute().getFormattedTime();
        }
        return dateTime;
    }

    /**
     * Die Methode setStartAddressColumn setzt die Startadresse für Fahrer bei einer Hinfahrt auf die eigene Adresse,
     * für Mitfahrer auf ihre Abholadresse. Bei einer Rückfahrt ist der FH-Standort die Start-Adresse.
     *
     * @param booking           abgeschlossene Buchung
     * @return                  Adresse, die angezeigt werden soll
     */
    private String setStartAddressColumn(Booking booking){
        if(!radioButtonGroup.getValue().equals(OFFERED_DRIVES) && booking.getDriveRoute().getDriveType().equals(DriveType.OUTWARD_TRIP)){
            return booking.getStopover().getFullAddressToString();
        }
        else{
            return booking.getDriveRoute().getStart().getFullAddressToString();
        }
    }

    /**
     * Die Methode setDestinationColumnsetzt die Zieladresse für Fahrer bei einer Rückfahrt auf die eigene Adresse,
     * für Mitfahrer auf ihre Abholadresse. Bei einer Hinfahrt ist der FH-Standort die Ziel-Adresse.
     *
     * @param booking           abgeschlossene Buchung
     * @return                  Adresse, die angezeigt werden soll
     */
    private String setDestinationColumn(Booking booking){
        if(!radioButtonGroup.getValue().equals(OFFERED_DRIVES) && booking.getDriveRoute().getDriveType().equals(DriveType.RETURN_TRIP)){
            return booking.getStopover().getFullAddressToString();
        }
        else{
            return booking.getDriveRoute().getDestination().getFullAddressToString();
        }
    }
}
