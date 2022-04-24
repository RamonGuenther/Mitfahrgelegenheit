package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidMailException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.addressPatternCheck;
import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.emailPatternCheck;

/**
 * Die Klasse FormLayoutProfileData erstellt das FormLayout zur Eingabe bzw.
 * Anzeige der Profildaten eines Benutzers auf der RegistrationView und
 * der ProfileView
 *
 * @author Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class FormLayoutProfileData extends FormLayout {

    private String title;
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextFieldAddress googleAddress;
    private TextField street;
    private TextField postal;
    private TextField place;
    private SelectUniversityLocation selectUniversityLocation;
    private SelectFaculty selectFaculty;
    private SelectLanguage selectLanguage;
    private MultiSelectLanguage multiSelectLanguage;
    private HorizontalLayout buttonLayout;

    public FormLayoutProfileData(HorizontalLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
        this.buttonLayout.setClassName("profile-data-buttonlayout");
        createProfileDataForm();
    }

    public FormLayoutProfileData() {
        this.buttonLayout = new HorizontalLayout();
        this.buttonLayout.setClassName("profile-data-buttonlayout");
        createProfileDataForm();
    }

    /**
     * Die Methode createProfileDataForm erzeugt die notwendigen Komponenten für
     * das FormLayout zum Anzeigen oder Bearbeiten von Profildaten.
     */
    public void createProfileDataForm() {

        firstName = new TextField("Vorname");
        firstName.setErrorMessage("Vorname bitte angeben");

        lastName = new TextField("Nachname");
        lastName.setErrorMessage("Nachname bitte angeben");

        email = new TextField("FH-Email");
        email.setPattern("^[a-z]+.[a-z]+([1-9][0-9]*)?@fh-swf.de$");
        email.setErrorMessage("Bitte gültige FH-Mail eingeben.");

        googleAddress = new TextFieldAddress("Adresse");
        googleAddress.setErrorMessage("Adresse bitte angeben");

        street = new TextField("Straße / Hausnummer");

        postal = new TextField("Postleitzahl");
        postal.setReadOnly(true);

        place = new TextField("Wohnort");
        place.setReadOnly(true);

        selectUniversityLocation = new SelectUniversityLocation();
        selectUniversityLocation.setErrorMessage("FH Standort bitte angeben");
        selectUniversityLocation.addValueChangeListener(event -> {
            selectUniversityLocation.setInvalid(false);
            selectFaculty.setReadOnly(false);
            selectFaculty.setSubjectAreaItems(selectUniversityLocation.getValue());
        });


        selectFaculty = new SelectFaculty();
        selectFaculty.setReadOnly(true);
        selectFaculty.setErrorMessage("Fachbereich bitte angeben");

        selectLanguage = new SelectLanguage();
        selectLanguage.addValueChangeListener(e-> selectLanguage.setInvalid(false));
        selectLanguage.setErrorMessage("Bitte die Hauptsprache angeben");
        selectLanguage.setRequiredIndicatorVisible(true);

        multiSelectLanguage = new MultiSelectLanguage();

        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        setColspan(firstName, 2);
        setColspan(lastName, 2);
        setColspan(email, 2);
        setColspan(selectUniversityLocation, 2);
        setColspan(googleAddress, 2);
        setColspan(selectFaculty, 2);
        setColspan(postal, 1);
        setColspan(place, 1);
        setColspan(selectLanguage, 1);
        setColspan(multiSelectLanguage, 1);
        setColspan(this.buttonLayout, 4);
    }

    /**
     * Die Methode markFormComponentsAsRequired zeigt an, welche Felder vom Benutzer
     * ausgefüllt werden müssen.
     */
    public void markFormComponentsAsRequired() {
        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        googleAddress.setRequiredIndicatorVisible(true);
        selectUniversityLocation.setRequiredIndicatorVisible(true);
        selectLanguage.setRequiredIndicatorVisible(true);
        selectFaculty.setRequiredIndicatorVisible(true);
    }

    /**
     * Mit der Methode showUserData werden die Felder des Formulars mit den
     * bereist vorhandenen Nutzerdaten ausgefüllt.
     *
     * @param user Benutzer, dessen Daten angezeigt werden sollen.
     */
    public void showUserData(User user) {
        lastName.setValue(user.getLastName());
        firstName.setValue(user.getFirstName());
        email.setValue(user.getEmail());
        selectUniversityLocation.setValue(user.getUniversityLocation());
        selectFaculty.setSubjectAreaItems(user.getUniversityLocation());
        selectFaculty.setValue(user.getFaculty());
        street.setValue(user.getAddress().getStreet() + " " + user.getAddress().getHouseNumber());
        place.setValue(user.getAddress().getPlace());
        postal.setValue(user.getAddress().getPostal());
        selectLanguage.setValue(user.getLanguages().getMainLanguage());
        multiSelectLanguage.setValue(user.getLanguages().getAllLanguages());
    }

    /**
     * Mit der Methode setReadOnly kann eingestellt werden, ob die Felder des
     * Formulars nur zu lesen sind, oder ob sie auch bearbeitet werden können.
     *
     * @param value true oder false
     */
    public void setReadOnly(boolean value) {
        firstName.setReadOnly(value);
        lastName.setReadOnly(value);
        selectUniversityLocation.setReadOnly(value);
        selectFaculty.setReadOnly(value);
        email.setReadOnly(value);
        street.setReadOnly(value);
        selectLanguage.setReadOnly(value);
        multiSelectLanguage.setReadOnly(value);
    }

    /**
     * Prüft, ob alle Felder, die ausgefüllt sein müssen, entsprechend Daten enthalten.
     *
     * @return Alle notwendigen Felder ausgefüllt?
     */
    public boolean isValuePresent() throws InvalidMailException, InvalidAddressException {
        setInputFieldsInvalid();
        return getStreet().getValue() != null && !getStreet().getValue().isEmpty() &&
                getFirstName().getValue() != null && !getFirstName().getValue().isEmpty() &&
                getLastName().getValue() != null && !getLastName().getValue().isEmpty() &&
                getEmail().getValue() != null && !getEmail().getValue().isEmpty() &&
                getSelectUniversityLocation().getValue() != null && !getSelectUniversityLocation().getValue().isEmpty() &&
                !getSelectLanguage().getValue().isEmpty();
    }

    private void setInputFieldsInvalid() throws InvalidMailException, InvalidAddressException {
        if (firstName.isEmpty()) {
            firstName.setInvalid(true);
        }
        if (lastName.isEmpty()) {
            lastName.setInvalid(true);
        }
        if (email.isEmpty()) {
            email.setInvalid(true);
        }
        else{
            emailPatternCheck(email.getValue());
        }
        if (selectUniversityLocation.isEmpty()) {
            selectUniversityLocation.setInvalid(true);
        }
        else {
            if (selectFaculty.isEmpty()) {
                selectFaculty.setInvalid(true);
            }
        }
        if (googleAddress.getValue().isEmpty()) {
            googleAddress.setInvalid(true);
        }
        else{
            addressPatternCheck(googleAddress.getValue());
        }
        if (selectLanguage.isEmpty()) {
            selectLanguage.setInvalid(true);
        }
    }

    /**
     * Setzt das Formular für die Registierung oder das eigene Profil zusammen.
     */
    public void createOwnProfileLayout() {
        setColspan(selectLanguage, 1);
        setColspan(multiSelectLanguage, 1);

        add(firstName, lastName, email, selectUniversityLocation, googleAddress,
                selectFaculty, postal, place, selectLanguage, multiSelectLanguage, buttonLayout);
    }

    /**
     * Sett das Formular für das Profil eines anderen Benutzers zusammen.
     */
    public void createOtherUserProfileLayout() {
        setColspan(selectLanguage, 2);
        setColspan(multiSelectLanguage, 2);

        add(firstName, lastName, selectUniversityLocation,
                selectFaculty, selectLanguage, multiSelectLanguage);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextField getFirstName() {
        return firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public TextField getEmail() {
        return email;
    }

    public TextFieldAddress getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(TextFieldAddress googleAddress) {
        this.googleAddress = googleAddress;
    }

    public TextField getStreet() {
        return street;
    }

    public TextField getPostal() {
        return postal;
    }

    public TextField getPlace() {
        return place;
    }

    public void setPlace(TextField place) {
        this.place = place;
    }

    public SelectUniversityLocation getSelectUniversityLocation() {
        return selectUniversityLocation;
    }

    public SelectFaculty getSelectFaculty() {
        return selectFaculty;
    }

    public SelectLanguage getSelectLanguage() {
        return selectLanguage;
    }

    public MultiSelectLanguage getMultiSelectLanguage() {
        return multiSelectLanguage;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public void setButtonLayout(HorizontalLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
    }

    public void setLastNameValue(String lastName) {
        this.lastName.setValue(lastName);
    }

}
