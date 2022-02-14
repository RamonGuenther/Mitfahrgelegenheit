package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;

/**
 * Die Klasse RegistrationView erstellt eine View zum anpassen der
 * fehlenden Benutzerdaten beim ersten Login des Users.
 *
 * @author Ivonne Kneißig und Ramon Günther
 */

@Route("benutzerdaten")
@PageTitle("Benutzerdaten anpassen")
@CssImport("/themes/mitfahrgelegenheit/views/registration-view.css")
public class RegistrationView extends VerticalLayout {

    /**
     * Der Konstruktor ist für das Erstellen der View zum
     * anpassen der Benutzerdaten zuständig.
     */

    public RegistrationView() {
        setId("registrationView");
        createRegistrationView();
    }

    /**
     * In der Methode createRegistrationView werden die einzelnen Komponenten
     * der View erzeugt und zusammengefügt.
     */

    private void createRegistrationView() {
        H1 title = new H1("Benutzerdaten anpassen");
        title.setId("titleRegistrationForm");

        TextField firstname = new TextField("Vorname");
        firstname.setReadOnly(true);

        TextField lastname = new TextField("Nachname");
        lastname.setReadOnly(true);

        TextField email = new TextField("Email");
        email.setReadOnly(true);

        TextFieldAddress address = new TextFieldAddress("Adresse");

        TextField postal = new TextField("Postleitzahl");
        postal.setReadOnly(true);
        TextField location = new TextField("Wohnort");
        location.setReadOnly(true);

        RadioButtonGender genderRadio = new RadioButtonGender();

        SelectFhLocation labelFhLocation = new SelectFhLocation();
        SelectSubjectArea labelSubjectArea = new SelectSubjectArea();

        labelFhLocation.addValueChangeListener(event ->
                labelSubjectArea.setSubjectAreaItems(labelFhLocation.getValue()));

        SelectFhLocation selectFhLocation = new SelectFhLocation();
        selectFhLocation.setReadOnly(true);
        SelectSubjectArea selectSubjectArea = new SelectSubjectArea();
        selectSubjectArea.setReadOnly(true);

        SelectLanguage selectLanguage = new SelectLanguage();
        MultiSelectLanguage multiSelectLanguage = new MultiSelectLanguage();

        Button submitButton = new Button("Speichern");
        submitButton.addClassName("buttonsRegistration");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClassName("buttonsRegistration");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        HorizontalLayout horizontalLayout = new HorizontalLayout(submitButton, cancelButton);

        FormLayout registrationForm = new FormLayout(title, firstname, address, lastname, postal, location,
                genderRadio, labelFhLocation, email, labelSubjectArea,
                selectLanguage, multiSelectLanguage, horizontalLayout);
        registrationForm.setId("registrationForm");

        registrationForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        registrationForm.setColspan(title, 4);
        registrationForm.setColspan(firstname, 2);
        registrationForm.setColspan(lastname, 2);
        registrationForm.setColspan(genderRadio, 2);
        registrationForm.setColspan(email, 2);
        registrationForm.setColspan(postal, 1);
        registrationForm.setColspan(location, 1);
        registrationForm.setColspan(labelFhLocation, 2);
        registrationForm.setColspan(labelSubjectArea, 2);
        registrationForm.setColspan(address, 2);
        registrationForm.setColspan(selectLanguage, 2);
        registrationForm.setColspan(multiSelectLanguage, 2);
        registrationForm.setColspan(horizontalLayout, 4);

        address.addValueChangeListener(event -> setAddressFields(registrationForm, address, postal, location));

        add(registrationForm);
    }

    /**
     * Die Methode setAddressFields ersetzt das autocomplete Adressfeld
     * durch ein normales Textfeld mit der Straße und setzt die Werte für
     * Postleitzahl und Ort in den entsprechenden Textfeldern.
     * Wird der Inhalt des Textfeldes für die Straße geändert, wird es wieder
     * in ein autocomplete Textfeld umgewandelt, um eine korrekte Adresseingabe
     * zu gewährleisten.
     *
     * @param layout            Layout, dessen Komponenten verändert werden
     * @param address           Autocomplete-Adressfeld, das ausgetauscht wird
     * @param postal            Textfeld für die Postleitzahl, dessen Wert gesetzt
     *                          werden soll
     * @param place             Textfeld für den Ort, dessen Wert gesetzt werden soll
     */
    private void setAddressFields(FormLayout layout, TextFieldAddress address,
                                  TextField postal, TextField place){
        if(layout == null){
            throw new IllegalArgumentException("RegistrationView: FormLayout is null");
        }
        if(address == null){
            throw new IllegalArgumentException("RegistrationView: TextFieldAddress is null");
        }
        if(postal == null){
            throw new IllegalArgumentException("RegistrationView: Textfield for postal is null");
        }
        if(place == null){
            throw new IllegalArgumentException("RegistrationView: Textfield for place is null");
        }

        TextField newField = new TextField("Straße/Hausnummer");
        newField.setValue(address.getStreet());
        postal.setValue(address.getPostal());
        place.setValue(address.getPlace());

        layout.remove(address);
        layout.addComponentAtIndex(2, newField);
        layout.setColspan(newField, 2);

        TextFieldAddress changeAddressTextField = new TextFieldAddress("Adresse");
        changeAddressTextField.addValueChangeListener(event -> setAddressFields(layout, changeAddressTextField,
                postal, place));

        newField.addFocusListener(event -> {
            layout.remove(newField);
            layout.addComponentAtIndex(2, changeAddressTextField);
            layout.setColspan(changeAddressTextField, 2);
            changeAddressTextField.focus();
            postal.setValue("");
            place.setValue("");
        });
    }
}