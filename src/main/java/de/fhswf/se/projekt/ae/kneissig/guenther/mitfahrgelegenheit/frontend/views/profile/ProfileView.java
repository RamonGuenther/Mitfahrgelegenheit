package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.MultiSelectLanguage;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectFhLocation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectLanguage;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectSubjectArea;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.AverageProfileRatings;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileDoubleRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private UserService userService;
    private User currentUser;

    /**
     * Der Konstruktor ist für das Erstellen der Profilansicht
     * zuständig.
     */
    public ProfileView(DriveRouteService driveRouteService, UserService userService){
        UI.getCurrent().setId(PageId.PROFILE.label);
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        currentUser = userService.getCurrentUser();
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

        TextField lastname = new TextField("Nachname");
        lastname.setReadOnly(true);

        TextField email = new TextField("Email");
        email.setReadOnly(true);

        TextField address = new TextField("Straße/Hausnummer");
        address.setReadOnly(true);

        TextField plz = new TextField("Postleitzahl");
        plz.setReadOnly(true);

        TextField location = new TextField("Wohnort");
        location.setReadOnly(true);

        TextField gender = new TextField("Geschlecht");
        gender.setReadOnly(true);

        SelectFhLocation selectFhLocation = new SelectFhLocation();
        selectFhLocation.setReadOnly(true);
        SelectSubjectArea selectSubjectArea = new SelectSubjectArea();
        selectSubjectArea.setReadOnly(true);

        SelectLanguage selectLanguage = new SelectLanguage();
        selectLanguage.setReadOnly(true);
        MultiSelectLanguage multiSelectLanguage = new MultiSelectLanguage();
        multiSelectLanguage.setReadOnly(true);

        // Buttons zum Bearbeiten / Löschen des Profils
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

        // Zusammenbauen der Profilangaben mit der Buttonleiste
        FormLayout registrationForm = new FormLayout(header, firstname, address, lastname, plz, location, gender,
                selectFhLocation, email, selectSubjectArea, selectLanguage, multiSelectLanguage, horizontalLayout);
        registrationForm.setId("profileForm");

        registrationForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        registrationForm.setColspan(header, 4);
        registrationForm.setColspan(firstname, 2);
        registrationForm.setColspan(lastname, 2);
        registrationForm.setColspan(gender, 2);
        registrationForm.setColspan(email, 2);
        registrationForm.setColspan(plz, 1);
        registrationForm.setColspan(location, 1);
        registrationForm.setColspan(selectFhLocation, 2);
        registrationForm.setColspan(selectSubjectArea, 2);
        registrationForm.setColspan(address, 2);
        registrationForm.setColspan(selectLanguage, 2);
        registrationForm.setColspan(multiSelectLanguage, 2);
        registrationForm.setColspan(horizontalLayout, 4);


        add(registrationForm);
    }

    /**
     * In der Methode createOwnOffersGrid werden die einzelnen Komponenten
     * für eine Auflistung der eigenen angebotenen Fahrten erstellt und
     * zusammengefügt.
     */
    private void createOwnOffersGrid(){

        H2 labelProfileGrid = new H2("Fahrtangebote von ...");

        List<DriveRoute> driveListTo = driveRouteService.findAllByBenutzerAndFahrtenTyp(currentUser, DriveType.OUTWARD_TRIP);
        List<DriveRoute> driveListBack = driveRouteService.findAllByBenutzerAndFahrtenTyp(currentUser, DriveType.RETURN_TRIP);

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
}
