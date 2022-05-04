package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.DeleteBookingDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse BookingsView erstellt eine View, auf der ein Benutzer seine
 * gebuchten Fahrten ansehen kann. Der Benutzer hat außerdem die Möglichkeit
 * aus einer gebuchten Fahrt "Auszusteigen", also die Mitfahrt abzusagen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Route(value = "gebuchteFahrten", layout = MainLayout.class)
@PageTitle("Meine gebuchten Fahrten")
@CssImport("/themes/mitfahrgelegenheit/views/manage-drive-views.css")
public class BookingsView extends VerticalLayout {

    private static final String OUTWARD_TRIP = "Hinfahrt";
    private static final String RETURN_TRIP = "Rückfahrt";
    private final DriveRouteService driveRouteService;
    private final MailService mailService;
    private final BookingService bookingService;
    private final User user;
    private Grid<Booking> gridBookings;
    private RadioButtonGroup<String> radioButtonGroup;

    public BookingsView(DriveRouteService driveRouteService, UserService userService, MailService mailService, BookingService bookingService) {
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;
        this.bookingService = bookingService;
        this.user = userService.getCurrentUser();

        createBookingsView();
    }

    private void createBookingsView() {

        H1 title = new H1("Meine Mitfahrgelegenheiten");

        radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(OUTWARD_TRIP, RETURN_TRIP);
        radioButtonGroup.setValue(OUTWARD_TRIP);
        radioButtonGroup.setClassName("radiobutton-group");

        gridBookings = new Grid<>();
        gridBookings.setItems(setBookingsByDriveType(DriveType.OUTWARD_TRIP));
        gridBookings.addColumn(this::setDateTimeColumn).setHeader("Tag / Uhrzeit");
        gridBookings.addColumn(booking -> booking.getDriveRoute().getDriveType().equals(DriveType.OUTWARD_TRIP) ?
                booking.getStopover().getFullAddressToString() :
                booking.getDriveRoute().getStart().getFullAddressToString()).setHeader("Von");
        gridBookings.addColumn(booking -> booking.getDriveRoute().getDriveType().equals(DriveType.OUTWARD_TRIP) ?
                booking.getDriveRoute().getDestination().getFullAddressToString() :
                booking.getStopover().getFullAddressToString()).setHeader("Nach");
        gridBookings.addComponentColumn(booking -> new Anchor("/drivetogether/profil/" + booking.getDriveRoute().getDriver().getId(), booking.getDriveRoute().getDriver().getFullName())).setHeader("Fahrer");
        gridBookings.addComponentColumn(this::createLeaveDriveButton).setHeader("Weiter mitfahren?");
        gridBookings.getColumns().forEach(col -> col.setAutoWidth(true));

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case OUTWARD_TRIP:
                     gridBookings.setItems(setBookingsByDriveType(DriveType.OUTWARD_TRIP));
                     break;
                case RETURN_TRIP:
                     gridBookings.setItems(setBookingsByDriveType(DriveType.RETURN_TRIP));
                     break;
            }
        });

        Div div = new Div(title, radioButtonGroup, gridBookings);
        div.setClassName("content");

        add(div);
    }

    private Button createLeaveDriveButton(Booking booking) {
        Icon icon = new Icon(VaadinIcon.EXIT);
        Button button = new Button("Aussteigen");
        button.setIcon(icon);

        button.addClickListener(event -> {
            DeleteBookingDialog deleteBookingDialog = new DeleteBookingDialog(driveRouteService, mailService, bookingService, booking);
            deleteBookingDialog.open();
        });

        return button;
    }

    /**
     * Die Methode setDateTimeColumn ist für die Spalte zur Anzeige des Tages zuständig, an dem die
     * gebuchte Fahrt stattfindet. Je nachdem ob es sich dabei um eine Einzelfahrt, eine Einzelfahrt bei
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
            dateTime = booking.getFormattedSingleDriveDate() + ", " + booking.getDriveRoute().getFormattedTime();
        }
        else if(booking.getRegularDriveSingleDriveDate() == null && booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() != null){
            dateTime = booking.getDriveRoute().getRegularDrive().getRegularDriveDay().label + ", " + booking.getDriveRoute().getFormattedTime();
        }

        return dateTime;
    }

    private List<Booking> setBookingsByDriveType(DriveType driveType){
        return bookingService.getAllByPassengerAndDriveType(user, driveType).orElse(Collections.emptyList())
                .stream().filter(booking ->
                                // Einfache Einzelfahrt Zeit/Datum, nach aktueller Zeit/Datum
                                booking.getDriveRoute().getDrivingTime().isAfter(LocalDateTime.now()) &&
                                booking.getRegularDriveSingleDriveDate() == null ||
                                // oder Einzelfahrt aus regelmäßiger Fahrt mit heutigem Datum, aber späterer Zeit
                                booking.getRegularDriveSingleDriveDate() != null &&
                                        booking.getRegularDriveSingleDriveDate().equals(LocalDate.now()) &&
                                        booking.getDriveRoute().getDrivingTime().toLocalTime().isAfter(LocalTime.now()) ||
                                // oder Einzelfahrt aus regelmäßiger Fahrt mit Datum nach aktuellem Datum
                                booking.getRegularDriveSingleDriveDate() != null &&
                                        booking.getRegularDriveSingleDriveDate().isAfter(LocalDate.now()) ||
                                // oder regelmäßige Fahrt bei dem das Enddatum noch nicht erreicht wurde
                                booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd() != null &&
                                        booking.getRegularDriveSingleDriveDate() == null &&
                                        LocalDateTime.of(booking.getDriveRoute().getRegularDrive().getRegularDriveDateEnd(),
                                               booking.getDriveRoute().getDrivingTime().toLocalTime()). isAfter(LocalDateTime.now())
                ).collect(Collectors.toList());
    }
}

