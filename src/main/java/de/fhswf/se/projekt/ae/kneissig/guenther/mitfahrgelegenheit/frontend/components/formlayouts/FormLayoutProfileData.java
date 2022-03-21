package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;

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

    public FormLayoutProfileData(String titleText, HorizontalLayout buttonLayout){
        setId("registrationForm");

        H1 title = new H1(titleText);
        title.setId("titleRegistrationForm");

        firstName = new TextField("Vorname");

        lastName = new TextField("Nachname");

        email = new TextField("FH-Email");
        email.setPattern("^[a-z]+.[a-z]+([1-9][0-9]*)?@fh-swf.de$");
        email.setErrorMessage("Bitte gültige FH-Mail eingeben.");

        googleAddress = new TextFieldAddress("Adresse");

        street = new TextField("Straße / Hausnummer");

        postal = new TextField("Postleitzahl");
        postal.setEnabled(false);

        place = new TextField("Wohnort");
        place.setEnabled(false);

        selectFaculty = new SelectFaculty();

        selectUniversityLocation = new SelectUniversityLocation();
        selectUniversityLocation.addValueChangeListener(event ->
                selectFaculty.setSubjectAreaItems(selectUniversityLocation.getValue()));

        selectLanguage = new SelectLanguage();

        multiSelectLanguage = new MultiSelectLanguage();

        this.buttonLayout = buttonLayout;

        add(title, firstName, lastName, email, selectUniversityLocation, googleAddress,
                selectFaculty, postal, place, selectLanguage, multiSelectLanguage, buttonLayout);

        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));


        setColspan(title, 4);
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
        setColspan(buttonLayout, 4);
    }

    public void markFormComponentsAsRequired(){
        firstName.setRequired(true);
        lastName.setRequired(true);
        email.setRequired(true);
        googleAddress.setRequiredIndicatorVisible(true);
        postal.setRequired(true);
        place.setRequired(true);
        selectUniversityLocation.setRequiredIndicatorVisible(true);
        selectLanguage.setRequiredIndicatorVisible(true);
    }

    public void showUserData(User user){
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        email.setValue(user.getEmail());
        selectUniversityLocation.setValue(user.getUniversityLocation());
        street.setValue(user.getAddress().getStreet() + " " + user.getAddress().getHouseNumber());
        place.setValue((user.getAddress().getPlace()));
        postal.setValue(user.getAddress().getPostal());
        selectLanguage.setValue(user.getLanguages().getMainLanguage());
        multiSelectLanguage.setValue(user.getLanguages().getAllLanguages());
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

    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
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

    public void setStreet(TextField street) {
        this.street = street;
    }

    public TextField getPostal() {
        return postal;
    }

    public void setPostal(TextField postal) {
        this.postal = postal;
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

    public void setSelectUniversityLocation(SelectUniversityLocation selectUniversityLocation) {
        this.selectUniversityLocation = selectUniversityLocation;
    }

    public SelectFaculty getSelectFaculty() {
        return selectFaculty;
    }

    public void setSelectFaculty(SelectFaculty selectFaculty) {
        this.selectFaculty = selectFaculty;
    }

    public SelectLanguage getSelectLanguage() {
        return selectLanguage;
    }

    public void setSelectLanguage(SelectLanguage selectLanguage) {
        this.selectLanguage = selectLanguage;
    }

    public MultiSelectLanguage getMultiSelectLanguage() {
        return multiSelectLanguage;
    }

    public void setMultiSelectLanguage(MultiSelectLanguage multiSelectLanguage) {
        this.multiSelectLanguage = multiSelectLanguage;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public void setButtonLayout(HorizontalLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
    }
}
