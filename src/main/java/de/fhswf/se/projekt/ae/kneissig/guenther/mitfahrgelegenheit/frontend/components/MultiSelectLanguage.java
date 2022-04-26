package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import org.vaadin.gatanaso.MultiselectComboBox;

/**
 * Die Klasse MultiSelectLanguage erstellt eine spezielle Combobox
 * zur Auswahl zusätzlicher Sprachen des Benutzers.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class MultiSelectLanguage extends MultiselectComboBox<String> {

    /**
     * Im Konstruktor von MultiSelectLanguage wird die Liste der
     * verfügbaren Sprachen initialisiert und der Combobox zur
     * Mehrfachauswahl hinzugefügt.
     */
    public MultiSelectLanguage(){
        LanguageList languageList = new LanguageList();
        setLabel("Weitere Sprachen");
        setItems(languageList);
        setClearButtonVisible(true);
    }
}
