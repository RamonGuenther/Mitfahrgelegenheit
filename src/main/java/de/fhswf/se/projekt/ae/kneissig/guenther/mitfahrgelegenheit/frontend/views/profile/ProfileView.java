package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidMailException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.PasswordDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutProfileData;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.DriveRouteGrid;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileDoubleRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.ProfileRatings;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse ProfileView erstellt eine View der Profilansicht
 * des Benutzers, der über die URL übergeben wird.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */

@Route(value = "profil/:id", layout = MainLayout.class)
@PageTitle("Profil")
@CssImport("/themes/mitfahrgelegenheit/views/profile.css")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private final DriveRouteService driveRouteService;
    private FormLayoutProfileData profileDataForm;
    private final UserService userService;
    private Long id;
    private User user;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;

    /**
     * Der Konstruktor initialisiert die Services für die ProfileView.
     */
    public ProfileView(DriveRouteService driveRouteService, UserService userService, MailService mailService, DriveRequestService driveRequestService) {
        UI.getCurrent().setId(PageId.PROFILE.label);
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        this.mailService = mailService;
        this.driveRequestService = driveRequestService;
    }

    /**
     * In der Methode createOwnProfileView werden die einzelnen Komponenten
     * der Benutzerdaten des eigenen Profils erstellt und zusammengefügt.
     */
    private void createOwnProfileView() {

        Button editProfileButton = new Button("Profil bearbeiten");
        editProfileButton.addClassName("profile-data-buttons");

        Button changePasswordButton = new Button("Passwort ändern");
        changePasswordButton.addClassName("profile-data-buttons");

        HorizontalLayout profileButtonLayout = new HorizontalLayout(editProfileButton, changePasswordButton);
        profileButtonLayout.setClassName("profile-data-buttonlayout");

        profileDataForm = new FormLayoutProfileData(profileButtonLayout);
        profileDataForm.createOwnProfileLayout();
        profileDataForm.remove(profileDataForm.getGoogleAddress());
        profileDataForm.addComponentAtIndex(4, profileDataForm.getStreet());
        profileDataForm.setColspan(profileDataForm.getStreet(), 2);
        profileDataForm.setClassName("profile-data-form");
        profileDataForm.showUserData(user);
        profileDataForm.setReadOnly(true);

        editProfileButton.addClickListener(event -> {
            profileDataForm.setReadOnly(false);

            editAddress();

            Button saveProfile = new Button("Speichern");
            saveProfile.addClassName("profile-data-buttons");
            Button cancel = new Button("Abbrechen");
            cancel.addClassName("profile-data-buttons");

            HorizontalLayout editProfilButtonLayout = new HorizontalLayout(saveProfile, cancel);
            editProfilButtonLayout.setClassName("profile-data-buttonlayout");
            profileDataForm.remove(profileButtonLayout);
            profileDataForm.addComponentAtIndex(10, editProfilButtonLayout);
            profileDataForm.setColspan(editProfilButtonLayout, 4);

            saveProfile.addClickListener(saveEvent -> saveProfileData(editProfilButtonLayout, profileButtonLayout));
            cancel.addClickListener(cancelEvent -> {
                profileDataForm.setReadOnly(true);
                profileDataForm.showUserData(user);
                profileDataForm.remove(editProfilButtonLayout);
                profileDataForm.addComponentAtIndex(10, profileButtonLayout);
                profileDataForm.setColspan(profileButtonLayout, 4);

                profileDataForm.remove(profileDataForm.getGoogleAddress());
                profileDataForm.addComponentAtIndex(4, profileDataForm.getStreet());
                profileDataForm.setColspan(profileDataForm.getStreet(), 2);
                profileDataForm.getSelectFaculty().setReadOnly(true);
                profileDataForm.setInputFieldsInvalid(false); //Damit beim Abbrechen nichts mehr rot is
            });
        });

        changePasswordButton.addClickListener(changePasswordEvent -> {
            PasswordDialog passwordDialog = new PasswordDialog(userService);
            passwordDialog.open();
        });
        add(profileDataForm);
    }

    /**
     * In der Methode createOtherUserProfileView werden die einzelnen Komponenten
     * der Benutzerdaten für das Profil eines anderen Benutzers erstellt und zusammengefügt.
     */
    private void createOtherUserProfileView() {
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
    private void createUsersDriveOffersGrid() {

        H2 labelProfileGrid = new H2("Fahrtangebote von " + user.getFirstName());

        List<DriveRoute> driveListTo = driveRouteService.getByUserAndDriveType(user, DriveType.OUTWARD_TRIP).orElse(Collections.emptyList())
                .stream()
                .filter(filter -> filter.getSeatCount() > filter.getBookings().size())
                .collect(Collectors.toList());

        List<DriveRoute> driveListBack = driveRouteService.getByUserAndDriveType(user, DriveType.RETURN_TRIP).orElse(Collections.emptyList())
                .stream()
                .filter(filter -> filter.getSeatCount() > filter.getBookings().size())
                .collect(Collectors.toList());

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");

        DriveRouteGrid gridOutward = new DriveRouteGrid("Ankunftszeit", driveListTo, driveRouteService, userService, mailService, driveRequestService, false, null);
        gridOutward.addClassName("profilegrid");
        DriveRouteGrid gridReturn = new DriveRouteGrid("Abfahrtzeit", driveListBack, driveRouteService, userService, mailService, driveRequestService, false, null);
        gridReturn.addClassName("profilegrid");
        Div div = new Div(labelProfileGrid, radioButtonGroup, gridOutward);
        div.setId("profile-drive_offers_layout");
        add(div);

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt" -> {
                    div.remove(gridReturn);
                    div.add(gridOutward);
                }
                case "Rückfahrt" -> {
                    div.remove(gridOutward);
                    div.add(gridReturn);
                }
            }
        });
        add(div);
    }

    /**
     * In der Methode createRatingsView werden die einzelnen Komponenten
     * der Benutzerbewertungen des Benutzers erstellt und zusammengefügt.
     */
    private void createRatingsView() {

        ProfileRatings profileRatings = new ProfileRatings(user);
        profileRatings.getAverageRatingsDriver().getPunctuality().setRating(user.getUserRating().getAverageDriverRatingPunctuality());
        profileRatings.getAverageRatingsDriver().getReliability().setRating(user.getUserRating().getAverageDriverRatingReliability());
        profileRatings.getAverageRatingsPassenger().getPunctuality().setRating(user.getUserRating().getAveragePassengerRatingPunctuality());
        profileRatings.getAverageRatingsPassenger().getReliability().setRating(user.getUserRating().getAveragePassengerRatingReliability());

        HorizontalLayout profileRatingLayout = new HorizontalLayout(profileRatings);
        profileRatingLayout.setId("profileRatingLayout");

        add(profileRatingLayout);
    }

    /**
     * Die Methode editAddress kümmert sich um das Wechseln der Adressfelder zwischen einem
     * normalen Textfeld zur Anzeige der Straße und einem Autocomplete-Adressfeld für die
     * Eingabe einer neuen Adresse.
     */
    private void editAddress() {
        if (profileDataForm == null) {
            throw new IllegalArgumentException("ProfileView: FormLayout is null");
        }

        profileDataForm.getStreet().addFocusListener(focusEvent -> {
            profileDataForm.remove(profileDataForm.getStreet());
            profileDataForm.remove(profileDataForm.getGoogleAddress());
            profileDataForm.setGoogleAddress(new TextFieldAddress("Adresse"));
            profileDataForm.addComponentAtIndex(4, profileDataForm.getGoogleAddress());
            profileDataForm.setColspan(profileDataForm.getGoogleAddress(), 2);
            profileDataForm.getStreet().setValue("");
            profileDataForm.getPostal().setValue("");
            profileDataForm.getPlace().setValue("");

            profileDataForm.getGoogleAddress().addValueChangeListener(event -> {
                profileDataForm.getStreet().setValue(profileDataForm.getGoogleAddress().getStreet() + " " + profileDataForm.getGoogleAddress().getNumber());
                profileDataForm.getPlace().setValue(profileDataForm.getGoogleAddress().getPlace());
                profileDataForm.getPostal().setValue(profileDataForm.getGoogleAddress().getPostal());
                profileDataForm.remove(profileDataForm.getGoogleAddress());
                profileDataForm.addComponentAtIndex(4, profileDataForm.getStreet());
                profileDataForm.setColspan(profileDataForm.getStreet(), 2);
            });
        });
    }

    /**
     * Die Methode saveProfileData speichert die aktualisierten Benutzerdaten, wenn der Benutzer
     * sein Profil editiert.
     *
     * @param editProfileButtonLayout Buttons während des Editierens
     * @param profileButtonLayout     Standard-Buttons für das Profil
     */
    private void saveProfileData(HorizontalLayout editProfileButtonLayout, HorizontalLayout profileButtonLayout) {
        try {
            if (profileDataForm.isValuePresent()) {
                user.setFirstName(profileDataForm.getFirstName().getValue());
                user.setLastName(profileDataForm.getLastName().getValue());
                user.setEmail(profileDataForm.getEmail().getValue());
                user.setUniversityLocation(profileDataForm.getSelectUniversityLocation().getValue());
                user.setFaculty((profileDataForm.getSelectFaculty().getValue()));
                user.setLanguages(new Languages(profileDataForm.getSelectLanguage().getValue(),
                        profileDataForm.getMultiSelectLanguage().getSelectedItems()));
                if (!profileDataForm.getGoogleAddress().getValue().isEmpty()) {
                    user.setAddress(new Address(
                            profileDataForm.getGoogleAddress().getPostal(),
                            profileDataForm.getGoogleAddress().getPlace(),
                            profileDataForm.getGoogleAddress().getStreet(),
                            profileDataForm.getGoogleAddress().getNumber()
                    ));
                }
                userService.save(user);
                profileDataForm.setReadOnly(true);
                profileDataForm.showUserData(user);

                profileDataForm.remove(editProfileButtonLayout);
                profileDataForm.addComponentAtIndex(10, profileButtonLayout);
                profileDataForm.setColspan(profileButtonLayout, 4);
            } else {
                NotificationError.show("Bitte alle Pflichtfelder ausfüllen");
            }
        } catch (InvalidMailException ex) {
            profileDataForm.getEmail().setErrorMessage(ex.getMessage());
            profileDataForm.getEmail().setInvalid(true);
            ex.printStackTrace();
        }

        catch(InvalidAddressException ex){
            profileDataForm.getGoogleAddress().setErrorMessage(ex.getMessage());
            profileDataForm.getGoogleAddress().setInvalid(true);
            ex.printStackTrace();
        }

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        if (beforeEnterEvent.getRouteParameters().get("id").isPresent()) {
            if (id != null && !id.equals(Long.parseLong(beforeEnterEvent.getRouteParameters().get("id").get()))) {
                UI.getCurrent().getPage().reload();
            } else {
                id = Long.parseLong(beforeEnterEvent.getRouteParameters().get("id").get());
            }
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        user = userService.findById(id).get();

        H1 title = new H1();
        title.setId("profile-data-title");

        ProfileDoubleRating doubleRating = new ProfileDoubleRating();
        doubleRating.setId("profile-data-double_rating");
        doubleRating.setDriverRating(user.getUserRating().getAverageDriverRating());
        doubleRating.setPassengerRating(user.getUserRating().getAveragePassengerRating());

        HorizontalLayout header = new HorizontalLayout();
        header.add(title, doubleRating);
        header.setId("profile-data-header");

        add(header);

        if (user.getId().equals(userService.getCurrentUser().getId())) {
            title.setText("Mein Profil");
            createOwnProfileView();
        } else {
            title.setText("Profil von " + user.getFirstName());
            createOtherUserProfileView();
            profileDataForm.setLastNameValue(user.getLastName().charAt(0) + ".");
            createUsersDriveOffersGrid();
        }
        createRatingsView();
    }
}
