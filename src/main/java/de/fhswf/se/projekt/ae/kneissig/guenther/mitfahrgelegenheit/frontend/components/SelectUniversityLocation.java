package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.select.Select;

/**
 * Die Klasse SelectSubjectArea erstellt ein Auswahlfeld für
 * die Wahl des Fachbereiches.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class SelectUniversityLocation extends Select<String> {

    private String locationAddress;
    /**
     * Der Konstruktor setzt das Label und die Items der Auswahl zusammen.
     */

    public SelectUniversityLocation(){
        setLabel("FH Standort");
        setItems("Hagen", "Iserlohn", "Lüdenscheid", "Meschede", "Soest");
        setPlaceholder("Standort wählen");
    }

    /**
     * Die Methode setFhLocationAddress legt die Adresse des vom
     * Benutzer gewählten FH-Standorts fest.
     *
     * @param universityLocation        Standort der vom Benutzer gewählten
     *                                  Fachhochschule
     */
    public void setUniversityLocationAddress(String universityLocation){
        try{
            switch (universityLocation) {
                case "Hagen" -> locationAddress = "Haldener Str. 182, 58095 Hagen, Deutschland";
                case "Iserlohn" -> locationAddress = "Frauenstuhlweg 31, 58644 Iserlohn, Deutschland";
                case "Lüdenscheid" -> locationAddress = "Bahnhofsallee 5, 58507 Lüdenscheid, Deutschland";
                case "Meschede" -> locationAddress = "Lindenstraße 53, 59872 Meschede, Deutschland";
                case "Soest" -> locationAddress = "Lübecker Ring 2, 59494 Soest, Deutschland";
                default -> locationAddress = "";
            }
        }
        catch(Exception e){
            locationAddress = " ";
        }
    }

    /**
     * Die Methode getFhLocationAddress gibt die Adresse des
     * vom Benutzer ausgewählten FH-Standortes zurück.
     *
     * @return          Adresse des gewählten FH-Standortes
     */
    public String getUniversityLocationAddress(){
        return locationAddress;
    }

}
