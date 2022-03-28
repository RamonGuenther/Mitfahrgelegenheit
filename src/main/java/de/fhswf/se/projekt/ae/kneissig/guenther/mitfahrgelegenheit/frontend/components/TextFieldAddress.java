package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.componentfactory.Autocomplete;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleAddressAutocomplete;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse TextfieldAddress erstellt ein spezielles Textfeld
 * mit einer Autocomplete-Funktion für Adressen, sodass die
 * korrekte Adresse aus den Optionen gewählt werden kann.
 *
 * @author Ivonne Kneißig und Ramon Günther
 */
public class TextFieldAddress extends Autocomplete {

    private List<String> daten;

    private String number;
    private String street;
    private String place;
    private String postal;

    /**
     * Im Konstruktor wird das Textfeld mit der Autocomplete-Funktion
     * für Adressen aufgebaut und ein entsprechender Listener hinzugefügt.
     *
     * @param label String, für die Beschriftung des Textfeldes
     */
    public TextFieldAddress(String label) {
        setLabel(label);
        GoogleAddressAutocomplete googleAddressAutocomplete = new GoogleAddressAutocomplete();

        addChangeListener(event -> {
            if (getValue().length() > 10) {
                try {
                    daten = new ArrayList<>();
                    daten = googleAddressAutocomplete.test(daten, getValue());
                    setOptions(daten);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addValueChangeListener(
                event -> {
                    System.out.println(event.getValue());
                    if (daten != null) {
                        for (String str : daten) {
                            if (str.equals(event.getValue())) {
                                AddressConverter converter = new AddressConverter(this.getValue());

                                street = converter.getStreet();
                                number = converter.getNumber();
                                postal = converter.getPostalCode();
                                place = converter.getPlace();
                            }
                        }
                    }
                }
        );
    }

    public String getStreet() {
        return street;
    }

    public String getPlace() {
        return place;
    }

    public String getPostal() {
        return postal;
    }

    public String getNumber() {
        return number;
    }

}