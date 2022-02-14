package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.checkbox.CheckboxGroup;

public class CheckboxRegularDrive extends CheckboxGroup<String> {

    /**
     * Der Konstruktor ist für das Erstellen Checkbox-Gruppe
     * mit den Wochentagen Montag bis Samstag zuständig.
     * Sonntag wurde nicht mit einbezogen, da am Sonntag keine
     * Veranstaltungen an der FH stattfinden.
     */
    public CheckboxRegularDrive (){
        setLabel("Regelmäßige Fahrt");
        setItems("Mo", "Di", "Mi", "Do", "Fr", "Sa");
        setWidth("500px");
    }
}
