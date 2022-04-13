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
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//TODO:
// - Wenn regelmäßige Fahrt, dann statt Datum den Tag anzeigen wäre doch lässig?
// - Email für Aussteigen versenden

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

    public BookingsView(DriveRouteService driveRouteService, UserService userService, MailService mailService, BookingService bookingService){
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;
        this.bookingService = bookingService;
        this.user = userService.getCurrentUser();

        createBookingsView();
    }

    private void createBookingsView(){

        H1 title = new H1("Meine Mitfahrgelegenheiten");

        List<Booking> bookingsOutwardTrip = bookingService.getAllByPassengerAndDriveType(user, DriveType.OUTWARD_TRIP)
                .stream().filter(booking -> booking.getDriveRoute().getDrivingTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        List<Booking> bookingsReturnTrip = bookingService.getAllByPassengerAndDriveType(user, DriveType.RETURN_TRIP)
                .stream().filter(booking -> booking.getDriveRoute().getDrivingTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());

        radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(OUTWARD_TRIP, RETURN_TRIP);
        radioButtonGroup.setValue(OUTWARD_TRIP);
        radioButtonGroup.setClassName("radiobutton-group");

        gridBookings = new Grid<>();
        gridBookings.setItems(bookingsOutwardTrip);

        gridBookings.addColumn(new LocalDateTimeRenderer<>(item -> item.getDriveRoute().getDrivingTime(),
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader("Tag / Uhrzeit");

        gridBookings.addColumn(booking -> booking.getDriveRoute().getStart().getFullAddressToString()).setHeader("Start");
        gridBookings.addColumn(booking -> booking.getDriveRoute().getZiel().getFullAddressToString()).setHeader("Ziel");

        gridBookings.addComponentColumn(booking -> new Anchor("/profil/" + booking.getDriveRoute().getDriver().getUsername(), booking.getDriveRoute().getDriver().getFirstName())).setHeader("Fahrer");

        gridBookings.addComponentColumn(this::createLeaveDriveButton).setHeader("Weiter mitfahren?");

        gridBookings.getColumns().forEach(col -> col.setAutoWidth(true));

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case OUTWARD_TRIP -> gridBookings.setItems(bookingsOutwardTrip);
                case RETURN_TRIP -> gridBookings.setItems(bookingsReturnTrip);
            }
        });

        Div div = new Div(title, radioButtonGroup, gridBookings);
        div.setClassName("content");

        add(div);
    }

    private Button createLeaveDriveButton(Booking booking){
        Icon icon = new Icon(VaadinIcon.EXIT);
        Button button = new Button("Aussteigen");
        button.setIcon(icon);

        button.addClickListener(event -> {

            DriveRoute driveRoute = driveRouteService.findById(booking.getDriveRoute().getId()).get();
            String passenger = booking.getPassenger().getFullName();

            try {
                driveRoute.removeBooking(booking);
                bookingService.delete(booking);

                List<Stopover> stopoverList = new ArrayList<>();

                for (Booking routeBooking : driveRoute.getBookings()) {
                    stopoverList.add(routeBooking.getStopover());
                }

                GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();
                String result = googleDistanceCalculation.calculate(driveRoute.getStart(), driveRoute.getDestination(), stopoverList);

                driveRoute.setCurrentRouteLink(result);
                driveRouteService.save(driveRoute);

                gridBookings.setItems(radioButtonGroup.getValue().equals(OUTWARD_TRIP) ?
                        bookingService.getAllByPassengerAndDriveType(user, DriveType.OUTWARD_TRIP) :
                        bookingService.getAllByPassengerAndDriveType(user, DriveType.RETURN_TRIP));

                NotificationSuccess.show("Der Fahrer wird über deinen Ausstieg benachrichtigt");

                mailService.sendBookingCancellation(driveRoute, passenger);

            } catch (MessagingException | IOException | InterruptedException | InvalidAddressException | ApiException e) {
                e.printStackTrace();
            }
        });

        return button;
    }
}

