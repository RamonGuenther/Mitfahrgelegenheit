package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;

/**
 * Die Klasse RatingDialog erstellt einen Dialog, mit dem ein Benutzer einen
 * anderen Benutzer anhand von bestimmten Kriterien bewerten kann.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/ratings-dialog.css")
public class RatingDialog extends Dialog {

    private final StarsRating ratingReliability;
    private final StarsRating ratingPuncuality;
    private final Button buttonSaveRating;

    public RatingDialog(UserService userService,
                        User userToRate,
                        DriveRouteService driveRouteService,
                        BookingService bookingService,
                        Booking booking,
                        Role role) {
        setId("rating-dialog");
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        H1 title = new H1("Benutzer bewerten");
        title.setId("ratings-title");

        FormLayout ratings = new FormLayout();
        ratings.setId("ratings-form");

        ratingPuncuality = new StarsRating(0);
        ratings.addFormItem(ratingPuncuality, "Pünktlichkeit");
        ratingPuncuality.setId("rating-stars-punctuality");
        ratingPuncuality.addValueChangeListener(event -> setSaveButtonEnabled());

        ratingReliability = new StarsRating(0);
        ratings.addFormItem(ratingReliability, "Zuverlässigkeit");
        ratingReliability.setId("rating-stars-reliability");
        ratingReliability.addValueChangeListener(event -> setSaveButtonEnabled());

        buttonSaveRating = new Button("Speichern");
        buttonSaveRating.setEnabled(false);
        buttonSaveRating.setClassName("rating-buttons");
        buttonSaveRating.addClickListener(event -> {
            userService.rateUser(userToRate,
                    ratingPuncuality.getRating(),
                    ratingReliability.getRating(),
                    role);

            switch (role) {
                case DRIVER:
                     booking.setRatedByPassenger(true);
                     break;
                case PASSENGER:
                     booking.setRatedByDriver(true);
                     break;
            }

            if (booking.isRatedByDriver() && booking.isRatedByPassenger()) {
                DriveRoute driveRoute = booking.getDriveRoute();
                driveRoute.removeBooking(booking);
                driveRouteService.save(driveRoute);
                bookingService.delete(booking);
            } else {
                bookingService.save(booking);
            }
            this.close();
            UI.getCurrent().getPage().reload();
        });
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setClassName("rating-buttons");
        buttonCancel.addClickListener(event -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonSaveRating, buttonCancel);
        buttonLayout.setId("rating-button-layout");

        VerticalLayout dialogLayout = new VerticalLayout(
                title,
                ratings,
                buttonLayout
        );

        add(dialogLayout);
    }

    /**
     * Die Methode setSaveButtonEnabled gibt den Speichern-Button erst frei,
     * wenn der Benutzer die gegebenen Kriterien bewertet hat.
     */
    private void setSaveButtonEnabled() {
        if (ratingPuncuality.getRating() != 0 && ratingReliability.getRating() != 0) {
            buttonSaveRating.setEnabled(true);
        }
    }
}
