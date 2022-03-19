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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.SearchDriveView;

import java.time.LocalDateTime;
import java.util.Set;

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

    private final UserService userService;
    private TextField street;

    /**
     * Der Konstruktor ist für das Erstellen der View zum Anpassen der Benutzerdaten
     * beim ersten Login des Benutzers zuständig.
     */

    public RegistrationView(UserService userService) {
        setId("registrationView");
        this.userService = userService;
        street = new TextField("Straße/Hausnummer");
        street.setRequired(true);
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
        firstname.setRequired(true);

        TextField lastname = new TextField("Nachname");
        lastname.setRequired(true);

        TextField email = new TextField("FH-Email");
        email.setRequired(true);

        TextFieldAddress address = new TextFieldAddress("Adresse");
        address.setRequiredIndicatorVisible(true);

        TextField postal = new TextField("Postleitzahl");
        postal.setEnabled(false);
        postal.setRequired(true);

        TextField place = new TextField("Wohnort");
        place.setEnabled(false);
        place.setRequired(true);

        SelectUniversityLocation selectUniversityLocation = new SelectUniversityLocation();
        selectUniversityLocation.setRequiredIndicatorVisible(true);

        SelectFaculty selectFaculty = new SelectFaculty();

        selectUniversityLocation.addValueChangeListener(event ->
                selectFaculty.setSubjectAreaItems(selectUniversityLocation.getValue()));

        SelectLanguage selectLanguage = new SelectLanguage();
        selectLanguage.setRequiredIndicatorVisible(true);

        MultiSelectLanguage multiSelectLanguage = new MultiSelectLanguage();

        Button submitButton = new Button("Speichern");
        submitButton.addClassName("buttonsRegistration");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClassName("buttonsRegistration");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout(submitButton, cancelButton);

        FormLayout registrationForm = new FormLayout(title, firstname, lastname, email, selectUniversityLocation, address,
                selectFaculty, postal, place, selectLanguage, multiSelectLanguage, horizontalLayout);
        registrationForm.setId("registrationForm");

        registrationForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        registrationForm.setColspan(title, 4);
        registrationForm.setColspan(firstname, 2);
        registrationForm.setColspan(lastname, 2);
        registrationForm.setColspan(email, 2);
        registrationForm.setColspan(selectUniversityLocation, 2);
        registrationForm.setColspan(address, 2);
        registrationForm.setColspan(selectFaculty, 2);
        registrationForm.setColspan(postal, 1);
        registrationForm.setColspan(place, 1);
        registrationForm.setColspan(selectLanguage, 1);
        registrationForm.setColspan(multiSelectLanguage, 1);
        registrationForm.setColspan(horizontalLayout, 4);

        address.addValueChangeListener(event -> setAddressFields(registrationForm, address, postal, place));

        submitButton.addClickListener(e -> {
            User user = userService.getCurrentUser();
            Set<String> languages = multiSelectLanguage.getSelectedItems();

            if(address.getValue() == null || address.getValue().isEmpty() ||
                    firstname.getValue() == null || firstname.getValue().isEmpty() ||
                    lastname.getValue() == null || lastname.getValue().isEmpty() ||
                    email.getValue() == null || email.getValue().isEmpty() ||
                    selectUniversityLocation.getValue() == null || selectUniversityLocation.getValue().isEmpty() ||
                    selectLanguage.getValue() == null || selectLanguage.getValue().isEmpty()){
                NotificationError.show("Bitte alle Pflichtfelder ausfüllen");
            }
            else{
                user.setAddress(new Address(address.getPostal(), address.getPlace(), address.getStreet(), address.getNumber()));
                user.setLanguages(new Languages(selectLanguage.getValue(), languages));
                user.setFaculty(selectFaculty.getValue());
                user.setUniversityLocation(selectUniversityLocation.getValue());
                user.setEmail(email.getValue());
                user.setLastLogin(LocalDateTime.now());
                user.setFirstLogin(true);

                userService.save(user);
                UI.getCurrent().navigate(SearchDriveView.class);
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

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

        street.setValue(address.getStreet());
        postal.setValue(address.getPostal());
        place.setValue(address.getPlace());

        layout.remove(address);
        layout.addComponentAtIndex(5, street);
        layout.setColspan(street, 2);

        TextFieldAddress changeAddressTextField = new TextFieldAddress("Adresse");
        changeAddressTextField.addValueChangeListener(event -> setAddressFields(layout, changeAddressTextField,
                postal, place));

        street.addFocusListener(event -> {
            layout.remove(street);
            layout.addComponentAtIndex(2, changeAddressTextField);
            layout.setColspan(changeAddressTextField, 2);
            changeAddressTextField.focus();
            postal.setValue("");
            place.setValue("");
        });
    }
}