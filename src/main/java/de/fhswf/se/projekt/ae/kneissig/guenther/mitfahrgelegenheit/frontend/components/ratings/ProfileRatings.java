package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Rating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;

/**
 * Die Klasse AverageRatingsRatings erstellt eine Ansicht
 * für die Auswahl der Durchschnittsbewertungen für die
 * Fahrer- und Mitfahrerbewertungen des Nutzers.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class ProfileRatings extends VerticalLayout {

    private static final String DRIVER_RATING = "Fahrerbewertung";
    private static final String PASSENGER_RATING = "Mitfahrerbewertung";

    private final RadioButtonGroup<String> ratingsRadio;
    private final AverageRatings averageRatingsPassenger;
    private final AverageRatings averageRatingsDriver;
    private final User user;
    private Grid<Rating> ratingGrid;
    private HorizontalLayout ratingLayout;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Fahrer- bzw. Mitfahrerbewertungen und die entsprechende
     * Beschriftung zuständig.
     */
    public ProfileRatings(User user) {
        setId("profile-ratings");

        H2 ratingsTitle = new H2("Bewertungen");

        this.user = user;
        ratingsRadio = new RadioButtonGroup<>();
        ratingsRadio.setId("radioRatings");
        ratingsRadio.setItems(DRIVER_RATING, PASSENGER_RATING);
        ratingsRadio.setValue(DRIVER_RATING);
        ratingsRadio.addValueChangeListener(e -> setAverageRatingsLayout());

        averageRatingsDriver = new AverageRatings();
        averageRatingsDriver.setClassName("rating-stars");
        averageRatingsPassenger = new AverageRatings();
        averageRatingsPassenger.setClassName("rating-stars");

        ratingGrid = new Grid<>();
        ratingGrid.setItems(user.getUserRating().getDriverRatings());
        ratingGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        ratingGrid.setSelectionMode(Grid.SelectionMode.NONE);
        ratingGrid.addColumn(Rating::getFormattedDate).setHeader("Datum");
        SerializableBiConsumer<StarsRating, Rating> consumerPunctuality = (starsRating, rating) -> {
            starsRating.setRating(rating.getPunctuality());
            starsRating.setId("ratingReliability");
            starsRating.setManual(false);
        };
        ratingGrid.addColumn(new ComponentRenderer<>(StarsRating::new, consumerPunctuality)).setHeader("Pünktlichkeit");
        SerializableBiConsumer<StarsRating, Rating> consumerReliability = (starsRating, rating) -> {
            starsRating.setRating(rating.getReliability());
            starsRating.setId("ratingReliability");
            starsRating.setManual(false);
        };
        ratingGrid.addColumn(new ComponentRenderer<>(StarsRating::new, consumerReliability)).setHeader("Zuverlässigkeit");
        ratingGrid.setId("rating-grid");

        ratingLayout = new HorizontalLayout(averageRatingsDriver, ratingGrid);

        add(ratingsTitle, ratingsRadio, ratingLayout);
    }

    /**
     * Die Methode setAverageRatings ist für das Wechseln der Bewertungs-
     * anzeige, in Abhängigkeit vom gewählten Radiobutton zuständig.
     * Je nach Auswahl werden die durchschnittlichen Bewertungen als Fahrer
     * oder als Mitfahrer angezeigt.
     */
    public void setAverageRatingsLayout() {

        switch (ratingsRadio.getValue()) {
            case DRIVER_RATING -> {
                ratingLayout.remove(averageRatingsPassenger);
                ratingLayout.addComponentAsFirst(averageRatingsDriver);
                ratingGrid.setItems(user.getUserRating().getDriverRatings());
            }
            case PASSENGER_RATING -> {
                ratingLayout.remove(averageRatingsDriver);
                ratingLayout.addComponentAsFirst(averageRatingsPassenger);
                ratingGrid.setItems(user.getUserRating().getPassengerRatings());
            }
        }
    }

    public AverageRatings getAverageRatingsPassenger() {
        return averageRatingsPassenger;
    }

    public AverageRatings getAverageRatingsDriver() {
        return averageRatingsDriver;
    }
}
