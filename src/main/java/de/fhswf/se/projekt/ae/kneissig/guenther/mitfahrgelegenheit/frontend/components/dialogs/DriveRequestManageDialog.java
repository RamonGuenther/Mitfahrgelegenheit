package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.google.maps.errors.ApiException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateBookingException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleDistanceCalculation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse DriveRequestManageDialog erstellt einen Dialog, mit dem der Benutzer eine
 * Anfrage zu einem seiner Fahrtangebote verwalten kann. Er hat die Möglichkeit die Anfrage
 * anzunehmen, sofern noch freie Plätze vorhanden sind, oder die Anfrage abzulehnen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/drive-request-manage-dialog.css")
public class DriveRequestManageDialog extends Dialog {

    private final DriveRequestService driveRequestService;
    private final DriveRouteService driveRouteService;
    private final DriveRequest driveRequest;
    private final BookingService bookingService;

    public DriveRequestManageDialog(DriveRequestService driveRequestService, DriveRouteService driveRouteService,
                                    DriveRequest driveRequest, BookingService bookingService) {
        this.bookingService = bookingService;
        this.driveRequestService = driveRequestService;
        this.driveRouteService = driveRouteService;
        this.driveRequest = driveRequest;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        DriveRoute driveRoute = driveRouteService.findById(driveRequest.getDriveRoute().getId()).get();

        HorizontalLayout passengerInformationLayout = new HorizontalLayout();
        passengerInformationLayout.setClassName("drive-request-manage-dialog-passenger_information_layout");

        H2 title = new H2("Anfrage von " + driveRequest.getPassenger().getFullName());
        title.setId("drive-request-manage-dialog-title");

        StarsRating passengerRating = new StarsRating(driveRequest.getPassenger().getUserRating().getAveragePassengerRating());
        passengerRating.setId("drive-request-manage-dialog-passenger_rating");
        passengerRating.setManual(false);

        passengerInformationLayout.add(title, passengerRating);

        Button profileButton = new Button(VaadinIcon.USER.create());
        profileButton.setText("Profil");
        profileButton.setId("drive-request-manage-dialog-profile_button");
        profileButton.addClickListener(e -> {
            close();
            UI.getCurrent().navigate(ProfileView.class,
                    new RouteParameters(new RouteParam("id", driveRequest.getPassenger().getId().toString())));

        });

        HorizontalLayout titleLayout = new HorizontalLayout(passengerInformationLayout, profileButton);
        titleLayout.setClassName("drive-request-manage-dialog-title_layout");

        FormLayoutDriveRoute formLayoutDriveRoute = new FormLayoutDriveRoute(driveRoute.getDriveType());
        formLayoutDriveRoute.setData(driveRoute);
        formLayoutDriveRoute.setReadOnly(true);
        formLayoutDriveRoute.remove(formLayoutDriveRoute.getTitle());
        formLayoutDriveRoute.getButtonDetourRoute().setText("Route mit Zwischenstopp anzeigen");
        formLayoutDriveRoute.removeClickListener();
        formLayoutDriveRoute.getButtonDetourRoute().addClickListener(e -> UI.getCurrent().getPage().open(driveRequest.getCurrentRouteLink(), "_blank"));

        add(titleLayout, formLayoutDriveRoute);

        if(driveRequest.getRegularDriveSingleDriveDate() != null){
            Label singleDriveLabel = new Label("Achtung: " + driveRequest.getPassenger().getFirstName() +
                    " möchte nur am " + driveRequest.getFormattedSingleDriveDate() + " mitfahren.");
            singleDriveLabel.setId("drive-request-manage-dialog-label_single_drive");
            formLayoutDriveRoute.addComponentAtIndex(10, singleDriveLabel);
            formLayoutDriveRoute.setColspan(singleDriveLabel,4);
        }

        TextArea textArea = new TextArea("Nachricht: ");
        textArea.setReadOnly(true);
        textArea.setId("drive-request-manage-dialog-text_area");
        textArea.setValue(driveRequest.getNote().isEmpty() ? "Keine Nachricht" : driveRequest.getNote());

        Button acceptButton = new Button("Akzeptieren");
        acceptButton.setClassName("drive-request-manage-dialog-buttons");
        Button declineButton = new Button("Ablehnen");
        declineButton.setClassName("drive-request-manage-dialog-buttons");
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setClassName("drive-request-manage-dialog-buttons");

        HorizontalLayout buttonLayout = new HorizontalLayout(acceptButton, declineButton, cancelButton);
        buttonLayout.setClassName("drive-request-manage-dialog-button_layout");

        acceptButton.addClickListener(e -> {
            if(driveRequest.getDriveRoute().getSeatCount().equals(driveRequest.getDriveRoute().getBookings().size())){
                NotificationError.show("Keine Sitzplätze mehr verfügbar.");
                return;
            }
            saveDriveRequest(RequestState.ACCEPTED);
        });

        declineButton.addClickListener(e -> saveDriveRequest(RequestState.REJECTED));

        cancelButton.addClickListener(e -> close());

        add(textArea, buttonLayout);
    }

    /**
     * Die Methode saveDriveRequest speichert die Fahrtanfrage entsprechend der
     * Entscheidung des Fahrers. Wird die Fahrt abgelehnt, wird der Status gesetzt
     * und die Methode verlassen. Wird die Fahrt angenommen, wird eine Buchung für
     * den Mitfahrer erzeugt.
     *
     * @param requestState      Neuer Status der Anfrage
     */
    private void saveDriveRequest(RequestState requestState) {
        driveRequest.setRequestState(requestState);
        driveRequestService.save(driveRequest);
        if (requestState == RequestState.ACCEPTED) {
            try {
                Booking newBooking = new Booking(driveRequest.getDriveRoute(), driveRequest.getPassenger(), driveRequest.getStopover());

                /*  Wenn eine Einzelfahrt bei einer regelmäßigen Fahrt gebucht wird, muss das Datum, an dem der Mitfahrer mitfährt,
                    in der Buchung festgehalten werden. So kann am Ende geprüft werden, wann die Buchung abgelaufen ist und der
                    Platz im Auto wieder freigegeben wird.*/
                if(driveRequest.getRegularDriveSingleDriveDate() != null){
                    newBooking.setRegularDriveSingleDriveDate(driveRequest.getRegularDriveSingleDriveDate());
                }
                bookingService.save(newBooking);
                driveRequest.getDriveRoute().addBooking(newBooking);

                List<Stopover> stopoverList = new ArrayList<>();

                for (Booking booking : driveRequest.getDriveRoute().getBookings()) {
                    stopoverList.add(booking.getStopover());
                }

                GoogleDistanceCalculation googleDistanceCalculation = new GoogleDistanceCalculation();

                String result = googleDistanceCalculation.calculate(driveRequest.getDriveRoute().getStart(),
                        driveRequest.getDriveRoute().getDestination(), stopoverList);

                driveRequest.getDriveRoute().setCurrentRouteLink(result);

            } catch (DuplicateBookingException | InvalidAddressException ex ) {
                NotificationError.show(ex.getMessage());
                ex.printStackTrace();
            }
            catch (IOException | InterruptedException | ApiException otherException){
                otherException.printStackTrace();
            }
        }
        driveRouteService.save(driveRequest.getDriveRoute());
        close();
        UI.getCurrent().getPage().reload();
    }
}
