package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.AverageProfileRatings;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileDoubleRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.util.List;
import java.util.Set;

/**
 * Die Klasse ProfileView erstellt eine View der Profilansicht
 * des Benutzers
 *
 * @author Ivonne Kneißig
 */

@Route(value = "profil", layout = MainLayout.class)
@PageTitle("Profil")
@CssImport("/themes/mitfahrgelegenheit/views/profile-view.css")
public class ProfileView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private final UserService userService;
    private final User user;
    private TextFieldAddress changeAddress;

    /**
     * Der Konstruktor ist für das Erstellen der Profilansicht
     * zuständig.
     */
    public ProfileView(DriveRouteService driveRouteService, UserService userService){
        UI.getCurrent().setId(PageId.PROFILE.label);
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        user = userService.getCurrentUser();
        setId("profileView");
        createProfileView();
        createOwnOffersGrid();
        createRatingsView();
    }

    /**
     * In der Methode createProfileView werden die einzelnen Komponenten
     * der Benutzerdaten des Profils erstellt und zusammengefügt.
     */
    private void createProfileView(){

        H1 title = new H1("Profil");
        title.setId("titleProfile");
        ProfileDoubleRating doubleRating = new ProfileDoubleRating();
        doubleRating.setId("doubleRating");
        HorizontalLayout header = new HorizontalLayout();
        header.setId("profileHeader");
        header.add(title, doubleRating);

        TextField firstname = new TextField("Vorname");
        firstname.setReadOnly(true);
        firstname.setValue(user.getFirstName());

        TextField lastname = new TextField("Nachname");
        lastname.setReadOnly(true);
        lastname.setValue(user.getLastName());

        TextField email = new TextField("Email");
        email.setReadOnly(true);
        email.setPattern("^[a-z]+.[a-z]+([1-9][0-9]*)?@fh-swf.de$");
        email.setErrorMessage("Bitte gültige FH-Mail eingeben.");
        email.setValue(user.getEmail());

        TextField address = new TextField("Straße/Hausnummer");
        address.setReadOnly(true);
        address.setValue(user.getAddress().getStreet() + user.getAddress().getHouseNumber());

        TextField postal = new TextField("Postleitzahl");
        postal.setReadOnly(true);
        postal.setValue(user.getAddress().getPostal());

        TextField place = new TextField("Wohnort");
        place.setReadOnly(true);
        place.setValue(user.getAddress().getPlace());

        SelectUniversityLocation selectUniversityLocation = new SelectUniversityLocation();
//        TextField selectUniversityLocation = new TextField("FH-Standort");
        selectUniversityLocation.setReadOnly(true);
        selectUniversityLocation.setValue(user.getUniversityLocation());

        SelectFaculty selectFaculty = new SelectFaculty();
//        TextField selectFaculty = new TextField("Fachbereich");
        selectFaculty.setValue(user.getFaculty());
        selectFaculty.setReadOnly(true);

        SelectLanguage selectLanguage = new SelectLanguage();
        selectLanguage.setReadOnly(true);
        selectLanguage.setValue(user.getLanguages().getMainLanguage());

        MultiSelectLanguage multiSelectLanguage = new MultiSelectLanguage();
        multiSelectLanguage.setReadOnly(true);
        Set<String> testlanguages = user.getLanguages().getAllLanguages();
        multiSelectLanguage.setValue(testlanguages);

        // Buttons zum Bearbeiten / Löschen des Profil
        Button editProfileButton = new Button("Profil bearbeiten");
        editProfileButton.addClassName("buttonsProfile");
        editProfileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        Button changePasswordButton = new Button("Passwort ändern");
        changePasswordButton.addClassName("buttonsProfile");
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteAccountButton = new Button("Account löschen");
        deleteAccountButton.addClassName("buttonsProfile");
        deleteAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout(editProfileButton, changePasswordButton, deleteAccountButton);

        FormLayout registrationForm = new FormLayout(title, firstname, lastname, selectUniversityLocation, selectFaculty, address,
                postal, place, email, selectLanguage, multiSelectLanguage, horizontalLayout);
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

        editProfileButton.addClickListener(event -> {
            firstname.setReadOnly(false);
            lastname.setReadOnly(false);
            selectUniversityLocation.setReadOnly(false);
            selectFaculty.setReadOnly(false);
            editAddress(registrationForm, address, postal, place);
            email.setReadOnly(false);
            selectLanguage.setReadOnly(false);
            multiSelectLanguage.setReadOnly(false);

            Button saveProfile = new Button("Speichern");
            saveProfile.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancel = new Button("Abbrechen");
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            HorizontalLayout editProfilButtonLayout = new HorizontalLayout(saveProfile, cancel);
            registrationForm.remove(horizontalLayout);
            registrationForm.addComponentAtIndex(11, editProfilButtonLayout);
            registrationForm.setColspan(editProfilButtonLayout, 4);

            cancel.addClickListener(cancelEvent -> {
                firstname.setReadOnly(true);
                firstname.setValue(user.getFirstName());
                lastname.setReadOnly(true);
                lastname.setValue(user.getLastName());
                selectUniversityLocation.setReadOnly(true);
                selectFaculty.setReadOnly(true);
                address.setReadOnly(true);
                email.setReadOnly(true);
                selectLanguage.setReadOnly(true);
                multiSelectLanguage.setReadOnly(true);
            });
        });

        add(registrationForm);
    }



    /**
     * In der Methode createOwnOffersGrid werden die einzelnen Komponenten
     * für eine Auflistung der eigenen angebotenen Fahrten erstellt und
     * zusammengefügt.
     */
    private void createOwnOffersGrid(){

        H2 labelProfileGrid = new H2("Fahrtangebote von ...");

        List<DriveRoute> driveListTo = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.OUTWARD_TRIP);
        List<DriveRoute> driveListBack = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.RETURN_TRIP);

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");

        GridOwnDriveOffersView gridHinfahrt = new GridOwnDriveOffersView("Ankunftszeit", driveListTo, driveRouteService);
        gridHinfahrt.addClassName("profilegrid");
        GridOwnDriveOffersView gridRueckfahrt = new GridOwnDriveOffersView("Abfahrtzeit", driveListBack, driveRouteService);
        gridRueckfahrt.addClassName("profilegrid");
        Div div = new Div(labelProfileGrid, radioButtonGroup, gridHinfahrt);
        div.setId("contentOwnDriveOffers");
        add(div);

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt":
                    div.remove(gridRueckfahrt);
                    div.add(gridHinfahrt);
                    break;

                case "Rückfahrt":
                    div.remove(gridHinfahrt);
                    div.add(gridRueckfahrt);
                    break;
            }
        });

//        grid.addClassName("profilegrid");
//        grid.setItems(driveList);
//
//
//        grid.getColumnByKey("ziel").setFooter("Anzahl:  "  /*cardList.size()*/);
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(div);
    }

    /**
     * In der Methode createRatingsView werden die einzelnen Komponenten
     * der Benutzerbewertungen erstellt und zusammengefügt.
     */
    private void createRatingsView(){

        HorizontalLayout profileRatingLayout = new HorizontalLayout();
        profileRatingLayout.setId("profileRatingLayout");

        AverageProfileRatings averageProfileRatings = new AverageProfileRatings();
        averageProfileRatings.setId("averageProfileRatings");
        profileRatingLayout.add(averageProfileRatings);
        averageProfileRatings.getRatingsRadio().addValueChangeListener(event ->
                averageProfileRatings.setAverageRatings(averageProfileRatings.getRatingsRadio().getValue()));

        add(profileRatingLayout);
    }


    private void editAddress(FormLayout layout, TextField address,
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

        layout.remove(address);
        changeAddress = new TextFieldAddress("Adresse");
        layout.addComponentAtIndex(5, changeAddress);
        layout.setColspan(changeAddress, 2);

        changeAddress.addValueChangeListener(event -> {
            address.setValue(changeAddress.getStreet());
            place.setValue(changeAddress.getPlace());
            postal.setValue(changeAddress.getPostal());

            layout.remove(changeAddress);
            layout.addComponentAtIndex(5, address);
            layout.setColspan(address, 2);
            address.addFocusListener(e1 -> {
                editAddress(layout, address, place, postal);
            });
        });
    }
}
