package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutProfileData;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileDoubleRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileRatings;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.util.List;


/**
 * Die Klasse ProfileView erstellt eine View der Profilansicht
 * des Benutzers, der über die URL übergeben wird.
 *
 * @author Ivonne Kneißig
 */

@Route(value = "profil/:username", layout = MainLayout.class)
@PageTitle("Profil")
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private final DriveRouteService driveRouteService;
    private FormLayoutProfileData profileDataForm;
    private final UserService userService;
    private String username;
    private User user;

    /**
     * Der Konstruktor initialisiert die Services für die ProfileView.
     */
    public ProfileView(DriveRouteService driveRouteService, UserService userService){
        UI.getCurrent().setId(PageId.PROFILE.label);
        this.driveRouteService = driveRouteService;
        this.userService = userService;
    }

    /**
     * In der Methode createOwnProfileView werden die einzelnen Komponenten
     * der Benutzerdaten des eigenen Profils erstellt und zusammengefügt.
     */
    private void createOwnProfileView(){

        Button editProfileButton = new Button("Profil bearbeiten");
        editProfileButton.addClassName("profile-data-buttons");
        editProfileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button changePasswordButton = new Button("Passwort ändern");
        changePasswordButton.addClassName("profile-data-buttons");
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout profileButtonLayout = new HorizontalLayout(editProfileButton, changePasswordButton);
        profileButtonLayout.setClassName("profile-data-buttonlayout");

        profileDataForm = new FormLayoutProfileData(profileButtonLayout);
        profileDataForm.createOwnProfileLayout();
        profileDataForm.remove(profileDataForm.getGoogleAddress());
        profileDataForm.addComponentAtIndex(5, profileDataForm.getStreet());
        profileDataForm.setColspan(profileDataForm.getStreet(), 2);
        profileDataForm.setClassName("profile-data-form");
        profileDataForm.showUserData(user);
        profileDataForm.setReadOnly(true);

        editProfileButton.addClickListener(event -> {
            profileDataForm.setReadOnly(false);

            editAddress();

            Button saveProfile = new Button("Speichern");
            saveProfile.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            saveProfile.addClassName("profile-data-buttons");
            Button cancel = new Button("Abbrechen");
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            cancel.addClassName("profile-data-buttons");

            HorizontalLayout editProfilButtonLayout = new HorizontalLayout(saveProfile, cancel);
            editProfilButtonLayout.setClassName("profile-data-buttonlayout");
            profileDataForm.remove(profileButtonLayout);
            profileDataForm.addComponentAtIndex(10, editProfilButtonLayout);
            profileDataForm.setColspan(editProfilButtonLayout, 4);

            saveProfile.addClickListener(saveEvent -> {
              saveProfileData(editProfilButtonLayout, profileButtonLayout);
            });
            cancel.addClickListener(cancelEvent -> {
                profileDataForm.setReadOnly(true);
                profileDataForm.showUserData(user);
                profileDataForm.remove(editProfilButtonLayout);
                profileDataForm.addComponentAtIndex(10, profileButtonLayout);
                profileDataForm.setColspan(profileButtonLayout, 4);
            });
        });
        add(profileDataForm);
    }

    /**
     * In der Methode createOtherUserProfileView werden die einzelnen Komponenten
     * der Benutzerdaten für das Profil eines anderen Benutzers erstellt und zusammengefügt.
     */
    private void createOtherUserProfileView(){
        profileDataForm = new FormLayoutProfileData();
        profileDataForm.createOtherUserProfileLayout();
        profileDataForm.setClassName("profile-data-form");
        profileDataForm.showUserData(user);
        profileDataForm.setReadOnly(true);
        add(profileDataForm);
    }

    /**
     * In der Methode createUsersDriveOffersGrid werden die einzelnen Komponenten
     * für eine Auflistung der Fahrtangebote eines anderen Benutzers erstellt und
     * zusammengefügt.
     */
    private void createUsersDriveOffersGrid(){

        H2 labelProfileGrid = new H2("Fahrtangebote von " + user.getFirstName());

        List<DriveRoute> driveListTo = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.OUTWARD_TRIP);
        List<DriveRoute> driveListBack = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.RETURN_TRIP);

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");

        GridOwnDriveOffersView gridHinfahrt = new GridOwnDriveOffersView("Ankunftszeit", driveListTo, driveRouteService, userService);
        gridHinfahrt.addClassName("profilegrid");
        GridOwnDriveOffersView gridRueckfahrt = new GridOwnDriveOffersView("Abfahrtzeit", driveListBack, driveRouteService, userService);
        gridRueckfahrt.addClassName("profilegrid");
        Div div = new Div(labelProfileGrid, radioButtonGroup, gridHinfahrt);
        div.setId("profile-drive_offers_layout");
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
        add(div);
    }

    /**
     * In der Methode createRatingsView werden die einzelnen Komponenten
     * der Benutzerbewertungen des Benutzers erstellt und zusammengefügt.
     */
    private void createRatingsView(){

        HorizontalLayout profileRatingLayout = new HorizontalLayout(new ProfileRatings(user));
        profileRatingLayout.setId("profileRatingLayout");
        add(profileRatingLayout);
    }

    /**
     * Die Methode editAddress kümmert sich um das Wechseln der Adressfelder zwischen einem
     * normalen Textfeld zur Anzeige der Straße und einem Autocomplete-Adressfeld für die
     * Eingabe einer neuen Adresse.
     */
    private void editAddress(){
        if(profileDataForm == null){
            throw new IllegalArgumentException("ProfileView: FormLayout is null");
        }

        profileDataForm.getStreet().addFocusListener(focusEvent -> {
            profileDataForm.remove(profileDataForm.getStreet());
            profileDataForm.remove(profileDataForm.getGoogleAddress());
            profileDataForm.setGoogleAddress(new TextFieldAddress("Adresse"));
            profileDataForm.addComponentAtIndex(5, profileDataForm.getGoogleAddress());
            profileDataForm.setColspan(profileDataForm.getGoogleAddress(), 2);

            profileDataForm.getGoogleAddress().focus();
            profileDataForm.getGoogleAddress().addValueChangeListener(event -> {
                profileDataForm.getStreet().setValue(profileDataForm.getGoogleAddress().getStreet());
                profileDataForm.getPlace().setValue(profileDataForm.getGoogleAddress().getPlace());
                profileDataForm.getPostal().setValue(profileDataForm.getGoogleAddress().getPostal());

                profileDataForm.remove(profileDataForm.getGoogleAddress());
                profileDataForm.addComponentAtIndex(5, profileDataForm.getStreet());
                profileDataForm.setColspan(profileDataForm.getStreet(), 2);
            });
        });
    }

    /**
     * Die Methode saveProfileData speichert die aktualisierten Benutzerdaten, wenn der Benutzer
     * sein Profil editiert.
     *
     * @param editProfileButtonLayout       Buttons während des Editierens
     * @param profileButtonLayout           Standard-Buttons für das Profil
     */
    private void saveProfileData(HorizontalLayout editProfileButtonLayout, HorizontalLayout profileButtonLayout){
        if(profileDataForm.isValuePresent()){
            user.setFirstName(profileDataForm.getFirstName().getValue());
            user.setLastName(profileDataForm.getLastName().getValue());
            user.setEmail(profileDataForm.getEmail().getValue());
            user.setUniversityLocation(profileDataForm.getSelectUniversityLocation().getValue());
            user.setFaculty((profileDataForm.getSelectFaculty().getValue()));
            user.setLanguages(new Languages(profileDataForm.getSelectLanguage().getValue(),
                    profileDataForm.getMultiSelectLanguage().getSelectedItems()));
            if(!profileDataForm.getGoogleAddress().getValue().isEmpty()){
                user.setAddress(new Address(
                        profileDataForm.getGoogleAddress().getPostal(),
                        profileDataForm.getGoogleAddress().getPlace(),
                        profileDataForm.getGoogleAddress().getStreetWithoutNumber(),
                        profileDataForm.getGoogleAddress().getNumber()
                ));
            }
            userService.save(user);
            profileDataForm.setReadOnly(true);
            profileDataForm.showUserData(user);

            profileDataForm.remove(editProfileButtonLayout);
            profileDataForm.addComponentAtIndex(11, profileButtonLayout);
            profileDataForm.setColspan(profileButtonLayout, 4);
        }
        else{
            NotificationError.show("Bitte alle Pflichtfelder ausfüllen");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getRouteParameters().get("username").isPresent()){
            username = beforeEnterEvent.getRouteParameters().get("username").get();
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        this.user = userService.findBenutzerByUsername(username);

        H1 title = new H1();
        title.setId("profile-data-title");

        ProfileDoubleRating doubleRating = new ProfileDoubleRating();
        doubleRating.setId("profile-data-double_rating");

        HorizontalLayout header = new HorizontalLayout();
        header.add(title, doubleRating);
        header.setId("profile-data-header");

        add(header);

        if(user.getId().equals(userService.getCurrentUser().getId())){
            title.setText("Mein Profil");
            createOwnProfileView();
        }
        else{
            title.setText("Profil von " + user.getFirstName());
            createOtherUserProfileView();
            createUsersDriveOffersGrid();
        }
        createRatingsView();
    }
}
