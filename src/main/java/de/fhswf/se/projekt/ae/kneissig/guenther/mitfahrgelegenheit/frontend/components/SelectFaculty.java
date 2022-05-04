package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.select.Select;

/**
 * Die Klasse SelectSubjectArea erstellt ein Auswahlfeld für
 * die Wahl des Fachbereiches.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */

public class SelectFaculty extends Select<String> {

    /**
     * Der Konstruktor setzt das Label und einen Placeholder für die Auswahl.
     */
    public SelectFaculty(){
        setLabel("Fachbereich");
        setPlaceholder("Fachbereich wählen");
        setItems("Kein Fachbereich auswählbar");
    }

    /**
     * Die Methode setSubjectAreaItems erstellt die auswählbaren Items anhand des
     * gewählten Standortes.
     *
     * @param fhLocation FH-Standort, zu dem die Fachbereiche angezeigt werden sollen
     */
    public void setSubjectAreaItems(String fhLocation){

        if(fhLocation == null){
            setItems("Kein Fachbereich auswählbar");
        }
        else{
            switch (fhLocation) {
                case "Hagen":
                    setItems("Elektrotechnik und Informationstechnik", "Technische Betriebswirtschaft");
                    break;
                case "Iserlohn":
                    setItems("Informatik und Naturwissenschaften", "Maschinenbau");
                    break;
                case "Lüdenscheid":
                    setItems("Maschinenbau", "Elektrotechnik und Informationstechnik", "Technische Betriebswirtschaft");
                    break;
                case "Meschede":
                    setItems("Ingenieur- und Wirtschaftswissenschaften");
                    break;
                case "Soest":
                    setItems("Agrarwirtschaft", "Bildungs- und Gesellschaftswissenschaften",
                            "Elektrische Energietechnik", "Maschinenbau-Automatisierungstechnik");
                    break;
                default:
                    setItems("Kein Fachbereich auswählbar");
                    break;
            }
        }
    }
}
