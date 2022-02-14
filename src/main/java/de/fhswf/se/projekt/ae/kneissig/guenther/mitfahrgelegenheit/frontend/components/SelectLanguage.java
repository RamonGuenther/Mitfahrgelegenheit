package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.combobox.ComboBox;

/**
 * Die Klasse SelectLanguage erstellt eine spezielle Combobox
 * zur Auswahl der Hauptsprache des Benutzers
 *
 * @author Ivonne Kneißig
 */
public class SelectLanguage extends ComboBox<String> {

    /**
     * Im Konstruktor von SelectLanguage wird die Liste der
     * verfügbaren Sprachen initialisiert und der Combobox zur
     * Auswahl hinzugefügt.
     */
    public SelectLanguage(){
        LanguageList languageList = new LanguageList();
        setLabel("Sprache");
        setItems(languageList);
        setClearButtonVisible(true);
    }
}
