package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

/**
 * Die Klasse SelectSubjectArea erstellt ein Feld mit Radio-
 * Buttons zur Auswahl des Geschlechts
 *
 * @author Ivonne Kneißig
 */

public class RadioButtonGender extends RadioButtonGroup {

    /**
     * Der Konstruktor setzt das Label und die Items der Auswahl zusammen.
     */

    public RadioButtonGender(){
        setLabel("Geschlecht");
        setItems("weiblich", "männlich", "divers");
    }
}
