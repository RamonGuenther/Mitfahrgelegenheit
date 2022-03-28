package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Rating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Die Klasse AverageRatingsRatings erstellt eine Ansicht
 * für die Auswahl der Durchschnittsbewertungen für die
 * Fahrer- und Mitfahrerbewertungen des Nutzers.
 *
 * @author Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class ProfileRatings extends VerticalLayout {

    private RadioButtonGroup<String> ratingsRadio;
    private AverageRatings averageRatingsPassenger;
    private AverageRatings averageRatingsDriver;
    Grid<Rating> ratingGrid;
    private User user;
    HorizontalLayout ratingLayout;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Fahrer- bzw. Mitfahrerbewertungen und die entsprechende
     * Beschriftung zuständig.
     */
    public ProfileRatings(User user){
        setId("profile-ratings");

        H2 ratingsTitle = new H2("Bewertungen");

        this.user = user;
        ratingsRadio = new RadioButtonGroup<>();
        ratingsRadio.setId("radioRatings");
        ratingsRadio.setItems("Fahrerbewertung", "Mitfahrerbewertung");
        ratingsRadio.setValue("Fahrerbewertung");
        ratingsRadio.addValueChangeListener(e -> setAverageRatingsLayout(e.getValue()));

        averageRatingsDriver = new AverageRatings();
        averageRatingsDriver.setClassName("rating-stars");
        averageRatingsPassenger = new AverageRatings();
        averageRatingsPassenger.setClassName("rating-stars");

        ratingGrid = new Grid<>();
        ratingGrid.setItems(user.getUserRating().getDriverRatings());
        ratingGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        ratingGrid.setSelectionMode(Grid.SelectionMode.NONE);
        ratingGrid.addColumn(Rating::getFormattedDate).setHeader("Datum");
        ratingGrid.addColumn(Rating::getPunctuality).setHeader("Pünktlichkeit");
        ratingGrid.addColumn(Rating::getPunctuality).setHeader("Zuverlässigkeit");
        ratingGrid.setId("rating-grid");

        ratingLayout = new HorizontalLayout(averageRatingsDriver, ratingGrid);

        add(ratingsTitle, ratingsRadio, ratingLayout);
    }

    /**
     * Die Methode setAverageRatings ist für das Wechseln der Bewertungs-
     * anzeige in Abhängigkeit vom gewählten Radiobutton zuständig.
     * Je nach Auswahl werden die durchschnittlichen Bewertungen als Fahrer
     * oder als Mitfahrer angezeigt.
     *
     * @param ratingChoice  Auswahl des Nutzers bei den RadioButtons
     */
    public void setAverageRatingsLayout(String ratingChoice){

        if(ratingChoice == null){
            throw new IllegalArgumentException("AverageProfileRatings: String ratingChoice is null");
        }
        else{
            switch (ratingChoice){
                case "Fahrerbewertung":
                    ratingLayout.remove(averageRatingsPassenger);
                    ratingLayout.addComponentAsFirst(averageRatingsDriver);
                    ratingGrid.setItems(user.getUserRating().getDriverRatings());
                    break;
                case "Mitfahrerbewertung":
                    ratingLayout.remove(averageRatingsDriver);
                    ratingLayout.addComponentAsFirst(averageRatingsPassenger);
                    ratingGrid.setItems(user.getUserRating().getPassengerRatings());
                    break;
                default:
                    break;
            }
        }
    }

    public RadioButtonGroup<String> getRatingsRadio() {
        return ratingsRadio;
    }

    public void setRatingsRadio(RadioButtonGroup<String> ratingsRadio) {
        this.ratingsRadio = ratingsRadio;
    }

    public AverageRatings getAverageRatingsPassenger() {
        return averageRatingsPassenger;
    }

    public void setAverageRatingsPassenger(AverageRatings averageRatingsPassenger) {
        this.averageRatingsPassenger = averageRatingsPassenger;
    }

    public AverageRatings getAverageRatingsDriver() {
        return averageRatingsDriver;
    }

    public void setAverageRatingsLayout(AverageRatings averageRatings) {
        this.averageRatingsDriver = averageRatings;
    }
}
