package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;

@CssImport("/themes/mitfahrgelegenheit/components/ratings-dialog.css")
public class RatingDialog extends Dialog {

    public RatingDialog(UserService userService,
                        User userToRate,
                        DriveRouteService driveRouteService,
                        BookingService bookingService,
                        Booking booking,
                        Role role){
        setId("rating-dialog");
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        H1 title = new H1("Benutzer bewerten");
        title.setId("ratings-title");

        FormLayout ratings = new FormLayout();
        ratings.setId("ratings-form");

        StarsRating ratingPuncuality = new StarsRating(0);
        ratings.addFormItem(ratingPuncuality, "Pünktlichkeit");
        ratingPuncuality.setId("rating-stars-punctuality");

        StarsRating ratingReliability = new StarsRating(0);
        ratings.addFormItem(ratingReliability, "Zuverlässigkeit");
        ratingReliability.setId("rating-stars-reliability");

        Button buttonSavePassword = new Button("Speichern");
        buttonSavePassword.setClassName("rating-buttons");
        buttonSavePassword.addClickListener(event -> {
            userService.rateUser(userToRate,
                    ratingPuncuality.getRating(),
                    ratingReliability.getRating(),
                    role);

           // Todo Bookings löschen?! Problem, weil Fahrer und Mitfahrer bewerten müssen!

            this.close();
        });
        Button buttonCancel = new Button("Abbrechen");
        buttonCancel.setClassName("rating-buttons");
        buttonCancel.addClickListener(event -> close());

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonSavePassword, buttonCancel);
        buttonLayout.setId("rating-button-layout");

        VerticalLayout dialogLayout = new VerticalLayout(
                title,
                ratings,
                buttonLayout
        );

        add(dialogLayout);
    }

}
