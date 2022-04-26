package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.componentfactory.Autocomplete;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
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

    private List<String> predictions;

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
        setErrorMessage("Adresse bitte angeben");

        GoogleAddressAutocomplete googleAddressAutocomplete = new GoogleAddressAutocomplete();

        addChangeListener(event -> {
            if (getValue().length() > 10) {
                try {
                    predictions = new ArrayList<>();
                    predictions = googleAddressAutocomplete.findStreets(getValue());
                    setOptions(predictions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addValueChangeListener(
                event -> {
                    try {
                        if (predictions != null) {
                            for (String str : predictions) {
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
                    catch (InvalidAddressException ex){
                        ex.printStackTrace();
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