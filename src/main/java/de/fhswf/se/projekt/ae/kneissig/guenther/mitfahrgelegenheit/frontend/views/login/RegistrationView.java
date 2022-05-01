package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidMailException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutProfileData;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.SearchDriveView;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Die Klasse RegistrationView erstellt eine View zum anpassen der
 * fehlenden Benutzerdaten beim ersten Login des Users.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Route("benutzerdaten")
@PageTitle("Benutzerdaten anpassen")
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class RegistrationView extends VerticalLayout {

    private final UserService userService;
    private FormLayoutProfileData registrationForm;

    /**
     * Der Konstruktor ist für das Erstellen der View zum Anpassen der Benutzerdaten
     * beim ersten Login des Benutzers zuständig.
     */
    public RegistrationView(UserService userService) {
        this.userService = userService;
        createRegistrationView();
    }

    /**
     * In der Methode createRegistrationView werden die einzelnen Komponenten
     * der View erzeugt und zusammengefügt.
     */
    private void createRegistrationView() {
        Button submitButton = new Button("Speichern");
        submitButton.setEnabled(false);
        submitButton.addClassName("profile-data-buttons");

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClassName("profile-data-buttons");

        HorizontalLayout registrationButtonLayout = new HorizontalLayout(submitButton, cancelButton);

        H1 title = new H1("Benutzerdaten anpassen");
        title.setId("registration-data-title");

        registrationForm = new FormLayoutProfileData(registrationButtonLayout);
        registrationForm.createOwnProfileLayout();
        registrationForm.markFormComponentsAsRequired();
        registrationForm.setClassName("registration-data-form");

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Ich habe die Datenschutzerklärung gelesen und akzeptiere sie.");
        checkbox.addValueChangeListener(event -> submitButton.setEnabled(event.getValue()));

        Button privacyPolicyButton = new Button("(Datenschutzerklärung anzeigen)");
        privacyPolicyButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        privacyPolicyButton.addClickListener(event -> {

        });

        HorizontalLayout horizontalLayoutPolicy = new HorizontalLayout(checkbox, privacyPolicyButton);
        horizontalLayoutPolicy.setId("registration-horizontal-layout-policy");

        registrationForm.addComponentAtIndex(10, horizontalLayoutPolicy);
        registrationForm.setColspan(horizontalLayoutPolicy, 4);

        registrationForm.getGoogleAddress().addValueChangeListener(event ->
                editAddress());

        submitButton.addClickListener(e -> {
            try {
                User user = userService.getCurrentUser();
                Set<String> languages = registrationForm.getMultiSelectLanguage().getSelectedItems();

                if (registrationForm.isValuePresent()) {
                    user.setFirstName(registrationForm.getFirstName().getValue());
                    user.setLastName(registrationForm.getLastName().getValue());
                    user.setAddress(new Address(registrationForm.getGoogleAddress().getPostal(),
                            registrationForm.getGoogleAddress().getPlace(),
                            registrationForm.getGoogleAddress().getStreet(),
                            registrationForm.getGoogleAddress().getNumber()));
                    user.setLanguages(new Languages(registrationForm.getSelectLanguage().getValue(), languages));
                    user.setFaculty(registrationForm.getSelectFaculty().getValue());
                    user.setUniversityLocation(registrationForm.getSelectUniversityLocation().getValue());
                    user.setEmail(registrationForm.getEmail().getValue());
                    user.setLastLogin(LocalDateTime.now());
                    user.setFirstLogin(true);

                    userService.save(user);
                    UI.getCurrent().navigate(SearchDriveView.class);
                } else {
                    NotificationError.show("Bitte alle Pflichtfelder ausfüllen");
                }
            } catch (InvalidMailException | InvalidAddressException ex) {
                NotificationError.show(ex.getMessage());
                ex.printStackTrace();
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        add(title, registrationForm);
    }

    /**
     * Die Methode editAddress ersetzt das autocomplete Adressfeld
     * durch ein normales Textfeld mit der Straße und setzt die Werte für
     * Postleitzahl und Ort in den entsprechenden Textfeldern.
     * Wird der Inhalt des Textfeldes für die Straße geändert, wird es wieder
     * in ein autocomplete Textfeld umgewandelt, um eine korrekte Adresseingabe
     * zu gewährleisten.
     */
    private void editAddress() {
        if (registrationForm == null) {
            throw new IllegalArgumentException("ProfileView: FormLayout is null");
        }

        registrationForm.getStreet().setValue(registrationForm.getGoogleAddress().getStreet() + " " + registrationForm.getGoogleAddress().getNumber());
        registrationForm.getPlace().setValue(registrationForm.getGoogleAddress().getPlace());
        registrationForm.getPostal().setValue(registrationForm.getGoogleAddress().getPostal());
        registrationForm.remove(registrationForm.getGoogleAddress());
        registrationForm.addComponentAtIndex(4, registrationForm.getStreet());
        registrationForm.setColspan(registrationForm.getStreet(), 2);

        registrationForm.getStreet().addFocusListener(focusEvent -> {
            registrationForm.remove(registrationForm.getStreet());

            registrationForm.remove(registrationForm.getGoogleAddress());
            registrationForm.setGoogleAddress(new TextFieldAddress("Adresse"));
            registrationForm.getGoogleAddress().addValueChangeListener(e -> editAddress());

            registrationForm.addComponentAtIndex(4, registrationForm.getGoogleAddress());
            registrationForm.setColspan(registrationForm.getGoogleAddress(), 2);

            registrationForm.getPostal().setValue("");
            registrationForm.getPlace().setValue("");
        });
    }
}