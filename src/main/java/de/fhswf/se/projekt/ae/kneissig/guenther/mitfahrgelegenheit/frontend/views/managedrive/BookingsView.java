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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;


//TODO:
// - Wenn regelmäßige Fahrt, dann statt Datum den Tag anzeigen wäre doch lässig?
// - Email für Aussteigen versenden

@Route(value = "gebuchteFahrten", layout = MainLayout.class)
@PageTitle("Meine gebuchten Fahrten")
@CssImport("/themes/mitfahrgelegenheit/views/manage-drive-views.css")
public class BookingsView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private final UserService userService;
    private MailService mailService;
    private final BookingService bookingService;
    private final User user;
    private Grid<Booking> gridBookings;
    private RadioButtonGroup<String> radioButtonGroup;

    public BookingsView(DriveRouteService driveRouteService, UserService userService, MailService mailService, BookingService bookingService){
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.user = userService.getCurrentUser();

        createBookingsView();
    }

    private void createBookingsView(){

        H1 title = new H1("Meine Mitfahrgelegenheiten");

        List<Booking> bookingsOutwardTrip = bookingService.findAllByPassengerAndDriveRoute_DriveType(user, DriveType.OUTWARD_TRIP);
        List<Booking> bookingsReturnTrip = bookingService.findAllByPassengerAndDriveRoute_DriveType(user, DriveType.RETURN_TRIP);

        radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");
        radioButtonGroup.setClassName("radiobutton-group");

        gridBookings = new Grid();
        gridBookings.setItems(bookingsOutwardTrip);

        gridBookings.addColumn(new LocalDateTimeRenderer<>(item -> item.getDriveRoute().getDriveType()
                .equals(DriveType.OUTWARD_TRIP) ?
                item.getDriveRoute().getZiel().getTime() : item.getDriveRoute().getStart().getTime() ,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader("Tag / Uhrzeit");

        gridBookings.addColumn(booking -> booking.getDriveRoute().getStart().getFullAddressToString()).setHeader("Start");
        gridBookings.addColumn(booking -> booking.getDriveRoute().getZiel().getFullAddressToString()).setHeader("Ziel");

        gridBookings.addComponentColumn(booking -> new Anchor("/profil/" + booking.getDriveRoute().getDriver().getUsername(), booking.getDriveRoute().getDriver().getFirstName())).setHeader("Fahrer");

        gridBookings.addComponentColumn(this::createLeaveDriveButton).setHeader("Weiter mitfahren?");

        gridBookings.getColumns().forEach(col -> col.setAutoWidth(true));

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt":
                    gridBookings.setItems(bookingsOutwardTrip);
                    break;

                case "Rückfahrt":
                    gridBookings.setItems(bookingsReturnTrip);
                    break;
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

                gridBookings.setItems(radioButtonGroup.getValue().equals("Hinfahrt") ?
                        bookingService.findAllByPassengerAndDriveRoute_DriveType(user, DriveType.OUTWARD_TRIP) :
                        bookingService.findAllByPassengerAndDriveRoute_DriveType(user, DriveType.RETURN_TRIP));

                NotificationSuccess.show("Der Fahrer wird über deinen Ausstieg benachrichtigt");

                mailService.sendBookingCancellation(driveRoute, passenger);

            } catch (MessagingException | IOException | InterruptedException | InvalidAddressException | ApiException e) {
                e.printStackTrace();
            }
        });

        return button;
    }
}

