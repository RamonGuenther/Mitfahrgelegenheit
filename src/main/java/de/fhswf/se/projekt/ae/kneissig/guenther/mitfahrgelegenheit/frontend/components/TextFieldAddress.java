package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleAddressAutocomplete;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google.GoogleApiKey;

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
    private String streetWithoutNumber;

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
                                setAddressComponents();
                            }
                        }
                    }
                }
        );
    }

    /**
     * Die Methode setAddressComponents speichert die Address-
     * komponenten in einzelne Variablen, um diese weiter-
     * verwenden zu können.
     */
    private void setAddressComponents() {
        // getValue: Wert den das Feld nach dem Auswählen einer Adresse hat
        String value = this.getValue().replace(",", "");
        String[] addressComponents = value.split(" ");

        if (addressComponents.length == 5) {
            streetWithoutNumber = addressComponents[0];
            number = addressComponents[1];
            postal = addressComponents[2];
            place = addressComponents[3];
            street = streetWithoutNumber + " " + number;
        } else {
            street = addressComponents[0];
            postal = addressComponents[1];
            place = addressComponents[2];
        }
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

    public String getStreetWithoutNumber() {
        return streetWithoutNumber;
    }
}