package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.completeddrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.AverageRatingsDriver;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.AverageRatingsPassenger;

/**
 * Die Klasse RatingButton erstellt einen Button für die CompletedDriveView,
 * um Nutzer zu bewerten.
 *
 * @author Ramon Günther
 */
public class RatingButton extends Button {

    /**
     * Der Konstruktor erstellt den Button mit den geforderten
     * Attributen.
     */
    public RatingButton() {
        setText("Bewerten");
        setClassName("buttonsCompletedDrive");
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Die Methode ratingButtonEvent löst das Event aus, um Nutzer anhand der
     * Rolle zu bewerten.
     *
     * @param role Rolle des Nutzers (Fahrer/Mitfahrer)
     */
    public void ratingButtonEvent(Role role) {
        Div div = new Div();
        Label labelRatingDialog = new Label("Benutzer bewerten");

        HorizontalLayout buttonLayoutRating = new HorizontalLayout();
        buttonLayoutRating.setId("buttonLayoutRating");

        Button saveRating = new Button("Speichern");
        saveRating.setClassName("buttonsRating");
        Button cancelRating = new Button("Abbrechen");
        cancelRating.setClassName("buttonsRating");
        buttonLayoutRating.add(saveRating, cancelRating);

        div.add(labelRatingDialog);
        if (role == Role.FAHRER) {
            AverageRatingsPassenger ratingPassenger = new AverageRatingsPassenger();
            div.add(ratingPassenger);
        } else if (role == Role.MITFAHRER) {
            AverageRatingsDriver ratingDriver = new AverageRatingsDriver();
            div.add(ratingDriver);
        }
        div.add(buttonLayoutRating);
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.add(div);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();

        saveRating.addClickListener(saveEvent -> {
//                if(alle Kriterien bewertet) {}
            dialog.close();

//                else{
//                    notification.open();
//                }

        });

        cancelRating.addClickListener(deleteEvent -> {
            dialog.close();
        });

    }
}
