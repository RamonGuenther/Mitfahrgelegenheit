package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Languages;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutProfileData;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileRatings;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;


/**
 * Die Klasse ProfileView erstellt eine View der Profilansicht
 * des Benutzers
 *
 * @author Ivonne Kneißig
 */

@Route(value = "profil", layout = MainLayout.class)
@PageTitle("Profil")
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class ProfileView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private FormLayoutProfileData profileDataForm;
    private final UserService userService;
    private final User user;

    /**
     * Der Konstruktor ist für das Erstellen der Profilansicht
     * zuständig.
     */
    public ProfileView(DriveRouteService driveRouteService, UserService userService){
        UI.getCurrent().setId(PageId.PROFILE.label);
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        user = userService.getCurrentUser();

        createProfileView();
//        createOwnOffersGrid();
        createRatingsView();
    }

    /**
     * In der Methode createProfileView werden die einzelnen Komponenten
     * der Benutzerdaten des Profils erstellt und zusammengefügt.
     */
    private void createProfileView(){

        Button editProfileButton = new Button("Profil bearbeiten");
        editProfileButton.addClassName("profile-data-buttons");
        editProfileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button changePasswordButton = new Button("Passwort ändern");
        changePasswordButton.addClassName("profile-data-buttons");
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteAccountButton = new Button("Account löschen");
        deleteAccountButton.addClassName("profile-data-buttons");
        deleteAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout(editProfileButton, changePasswordButton, deleteAccountButton);

        profileDataForm = new FormLayoutProfileData("Profil", horizontalLayout);
        profileDataForm.remove(profileDataForm.getGoogleAddress());
        profileDataForm.addComponentAtIndex(5, profileDataForm.getStreet());
        profileDataForm.setColspan(profileDataForm.getStreet(), 2);
        profileDataForm.showUserData(user);
        profileDataForm.setReadOnly(true);
        profileDataForm.setClassName("profile-data-form");

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
            profileDataForm.remove(horizontalLayout);
            profileDataForm.addComponentAtIndex(11, editProfilButtonLayout);
            profileDataForm.setColspan(editProfilButtonLayout, 4);

            saveProfile.addClickListener(saveEvent -> {
              saveProfileData(editProfilButtonLayout, horizontalLayout);
            });
            cancel.addClickListener(cancelEvent -> {
                profileDataForm.setReadOnly(true);
                profileDataForm.showUserData(user);
                profileDataForm.remove(editProfilButtonLayout);
                profileDataForm.addComponentAtIndex(11, horizontalLayout);
                profileDataForm.setColspan(horizontalLayout, 4);
            });
        });
        add(profileDataForm);
    }

    /**
     * In der Methode createOwnOffersGrid werden die einzelnen Komponenten
     * für eine Auflistung der eigenen angebotenen Fahrten erstellt und
     * zusammengefügt.
     */
//    private void createOwnOffersGrid(){
//
//        H2 labelProfileGrid = new H2("Fahrtangebote von ...");
//
//        List<DriveRoute> driveListTo = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.OUTWARD_TRIP);
//        List<DriveRoute> driveListBack = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.RETURN_TRIP);
//
//        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
//        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
//        radioButtonGroup.setValue("Hinfahrt");
//
//        GridOwnDriveOffersView gridHinfahrt = new GridOwnDriveOffersView("Ankunftszeit", driveListTo, driveRouteService);
//        gridHinfahrt.addClassName("profilegrid");
//        GridOwnDriveOffersView gridRueckfahrt = new GridOwnDriveOffersView("Abfahrtzeit", driveListBack, driveRouteService);
//        gridRueckfahrt.addClassName("profilegrid");
//        Div div = new Div(labelProfileGrid, radioButtonGroup, gridHinfahrt);
//        div.setId("contentOwnDriveOffers");
//        add(div);
//
//        radioButtonGroup.addValueChangeListener(e -> {
//            switch (e.getValue()) {
//                case "Hinfahrt":
//                    div.remove(gridRueckfahrt);
//                    div.add(gridHinfahrt);
//                    break;
//
//                case "Rückfahrt":
//                    div.remove(gridHinfahrt);
//                    div.add(gridRueckfahrt);
//                    break;
//            }
//        });

//        grid.addClassName("profilegrid");
//        grid.setItems(driveList);
//
//
//        grid.getColumnByKey("ziel").setFooter("Anzahl:  "  /*cardList.size()*/);
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));

//        add(div);
//    }
//
    /**
     * In der Methode createRatingsView werden die einzelnen Komponenten
     * der Benutzerbewertungen erstellt und zusammengefügt.
     */
    private void createRatingsView(){

        HorizontalLayout profileRatingLayout = new HorizontalLayout();
        profileRatingLayout.setId("profileRatingLayout");

        ProfileRatings profileRatings = new ProfileRatings(user);
        profileRatingLayout.add(profileRatings);
        profileRatings.getRatingsRadio().addValueChangeListener(event ->
                profileRatings.setAverageRatingsDriver(profileRatings.getRatingsRadio().getValue()));
        add(profileRatingLayout);
    }


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

    private void saveProfileData(HorizontalLayout currentButtonLayout, HorizontalLayout newButtonLayout){
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

            profileDataForm.remove(currentButtonLayout);
            profileDataForm.addComponentAtIndex(11, newButtonLayout);
            profileDataForm.setColspan(newButtonLayout, 4);
        }
        else{
            NotificationError.show("Bitte alle Pflichtfelder ausfüllen");
        }
    }
}
