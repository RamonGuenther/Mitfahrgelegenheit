package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;

/**
 * TODO:  - Für Merkliste könnte man den Dialog auch missbrauchen nur mit anderen buttons unten!
 */
@CssImport("/themes/mitfahrgelegenheit/components/search-drive-result-view-dialog.css")
public class SearchDriveResultViewDialog extends Dialog {

    private final UserService userService;
    private final DriveRouteService driveRouteService;


    private final DriveRoute driveRoute;

    private final MailService mailService;

    private Anchor userAnchor; //TODO


    public SearchDriveResultViewDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService){
        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.driveRoute = driveRoute;
        this.mailService = mailService;

        VerticalLayout verticalLayout = new VerticalLayout();

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Button closeButton = new Button();
        closeButton.setId("search-drive-result-view-close_button");
        closeButton.setIcon(icon);
        closeButton.addClickListener(e -> close());
        verticalLayout.add(closeButton);

        StarsRating driverRating = new StarsRating(0);
        driverRating.setId("search-drive-result-view-driver_rating");
        driverRating.setManual(true);

        userAnchor = new Anchor("/profil/" + driveRoute.getBenutzer().getUsername(), driveRoute.getBenutzer().getUsername()); //TODO

        add(userAnchor, driverRating);

        Icon PROFILE_ICON = new Icon(VaadinIcon.USER);
        switch (driveRoute.getDriveType()) {

            case OUTWARD_TRIP -> {
                FormLayoutTopOfferDrive formLayoutHinfahrt = new FormLayoutTopOfferDrive();

                formLayoutHinfahrt.getFlexButton().setVisible(true);
                formLayoutHinfahrt.getFlexButton().setId("search-drive-result-view-dialog-profile_button");
                formLayoutHinfahrt.getFlexButton().setText("Profil");
                formLayoutHinfahrt.getFlexButton().setIcon(PROFILE_ICON);

                formLayoutHinfahrt.getTitleLayout().add(driverRating);
                formLayoutHinfahrt.setColspan(formLayoutHinfahrt.getTitleLayout(), 2);

                formLayoutHinfahrt.getFlexButton().addClickListener(e -> {
                    UI.getCurrent().navigate(ProfileView.class,
                            new RouteParameters(new RouteParam("username", driveRoute.getBenutzer().getUsername())));
                    this.close();
                });

                formLayoutHinfahrt.setReadOnly(true);
                formLayoutHinfahrt.setTitle("Hinfahrt von " + driveRoute.getBenutzer().getUsername());
                formLayoutHinfahrt.setSitzplaetze(driveRoute.getSeatCount().toString());
                formLayoutHinfahrt.setFhLocation(driveRoute.getZiel().getAddress().getPlace());
                formLayoutHinfahrt.setDriveTime(driveRoute.getZiel().getTime().toLocalTime());
                formLayoutHinfahrt.setDriveDateStart(driveRoute.getZiel().getTime().toLocalDate());
                formLayoutHinfahrt.setAddress(driveRoute.getStart().getAddress().getStreet() + " "
                        + driveRoute.getStart().getAddress().getHouseNumber() + ", "
                        + driveRoute.getStart().getAddress().getPostal() + " "
                        + driveRoute.getStart().getAddress().getPlace() + ", "
                        + "Deutschland");
                verticalLayout.add(formLayoutHinfahrt);
            }
            case RETURN_TRIP -> {
                FormLayoutBottomOfferDrive formLayoutRueckfahrt = new FormLayoutBottomOfferDrive();

                formLayoutRueckfahrt.getFlexButton().setVisible(true);
                formLayoutRueckfahrt.getFlexButton().setId("search-drive-result-view-dialog-profile_button");
                formLayoutRueckfahrt.getFlexButton().setText("Profil");
                formLayoutRueckfahrt.getFlexButton().setIcon(PROFILE_ICON);

                formLayoutRueckfahrt.getTitleLayout().add(driverRating);
                formLayoutRueckfahrt.setColspan(formLayoutRueckfahrt.getTitleLayout(), 2);

                formLayoutRueckfahrt.getFlexButton().addClickListener(e -> {
                    UI.getCurrent().navigate(ProfileView.class,
                            new RouteParameters(new RouteParam("username", driveRoute.getBenutzer().getUsername())));
                    this.close();
                });

                formLayoutRueckfahrt.setReadOnly(true);
                formLayoutRueckfahrt.setTitle("Rückfahrt von " + driveRoute.getBenutzer().getUsername());
                formLayoutRueckfahrt.setSitzplaetze(driveRoute.getSeatCount().toString());
                formLayoutRueckfahrt.setFhLocation(driveRoute.getStart().getAddress().getPlace());
                formLayoutRueckfahrt.setDriveTime(driveRoute.getStart().getTime().toLocalTime());
                formLayoutRueckfahrt.setDriveDateStart(driveRoute.getStart().getTime().toLocalDate());
                formLayoutRueckfahrt.setAddress(driveRoute.getZiel().getAddress().getStreet() + " "
                        + driveRoute.getZiel().getAddress().getHouseNumber() + ", "
                        + driveRoute.getZiel().getAddress().getPostal() + " "
                        + driveRoute.getZiel().getAddress().getPlace() + ", "
                        + "Deutschland");
                verticalLayout.add(formLayoutRueckfahrt);
            }

        }
        verticalLayout.add(createButtons());
        add(verticalLayout);
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private HorizontalLayout createButtons() {
        Button requestButton = new Button("Fahrt anfragen");
        requestButton.setId("search-drive-result-view-dialog-request_button");
        requestButton.setClassName("search-drive-result-view-dialog-buttons");
        requestButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        requestButton.addClickListener(e->{
            close();
            DriveRequestDialog driveRequestDialog = new DriveRequestDialog(driveRoute, userService, driveRouteService, mailService);
            driveRequestDialog.open();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("search-drive-result-view-dialog-button_layout");
        buttonLayout.add(requestButton);

        return buttonLayout;
    }

}
