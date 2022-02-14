package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

/**
 * Die Klasse AverageRatingsRatings erstellt eine Ansicht
 * für die Auswahl der Durchschnittsbewertungen für die
 * Fahrer- und Mitfahrerbewertungen des Nutzers.
 *
 * @author Ivonne Kneißig
 */
public class AverageProfileRatings extends VerticalLayout {

    private RadioButtonGroup<String> ratingsRadio;
    private AverageRatingsPassenger averageRatingsPassenger;
    private AverageRatingsDriver averageRatingsDriver;

    /**
     * Der Konstruktor ist für das Erstellen der Bewertungssterne
     * für die Fahrer- bzw. Mitfahrerbewertungen und die entsprechende
     * Beschriftung zuständig.
     */
    public AverageProfileRatings(){
        ratingsRadio = new RadioButtonGroup<>();
        ratingsRadio.setId("radioRatings");
        ratingsRadio.setItems("Fahrerbewertung", "Mitfahrerbewertung");
        ratingsRadio.setValue("Fahrerbewertung");

        averageRatingsDriver = new AverageRatingsDriver();
        averageRatingsPassenger = new AverageRatingsPassenger();
        add(ratingsRadio, averageRatingsDriver);
    }

    /**
     * Die Methode setAverageRatings ist für das Wechseln der Bewertungs-
     * anzeige in Abhängigkeit vom gewählten Radiobutton zuständig.
     * Je nach Auswahl werden die durchschnittlichen Bewertungen als Fahrer
     * oder als Mitfahrer angezeigt.
     *
     * @param ratingChoice  Auswahl des Nutzers bei den RadioButtons
     */
    public void setAverageRatings(String ratingChoice){

        if(ratingChoice == null){
            throw new IllegalArgumentException("AverageProfileRatings: String ratingChoice is null");
        }
        else{
            switch (ratingChoice){
                case "Fahrerbewertung":
                    replace(this.getComponentAt(1), averageRatingsDriver);
                    break;
                case "Mitfahrerbewertung":
                    replace(this.getComponentAt(1), averageRatingsPassenger);
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

    public AverageRatingsPassenger getAverageRatingsPassenger() {
        return averageRatingsPassenger;
    }

    public void setAverageRatingsPassenger(AverageRatingsPassenger averageRatingsPassenger) {
        this.averageRatingsPassenger = averageRatingsPassenger;
    }

    public AverageRatingsDriver getAverageRatingsDriver() {
        return averageRatingsDriver;
    }

    public void setAverageRatingsDriver(AverageRatingsDriver averageRatingsDriver) {
        this.averageRatingsDriver = averageRatingsDriver;
    }
}
