package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;

import java.time.LocalDate;

@CssImport("/themes/mitfahrgelegenheit/components/search-drive-result-view-dialog.css")
public class SearchDriveResultViewDialog extends Dialog {

    private final UserService userService;
    private final DriveRouteService driveRouteService;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;

    private final DriveRoute driveRoute;
    private final LocalDate singleDriveDate;
    private final boolean isUserSearchsRegularDrive;

    public SearchDriveResultViewDialog(DriveRoute driveRoute, UserService userService, DriveRouteService driveRouteService, MailService mailService, DriveRequestService driveRequestService, boolean isUserSearchsRegularDrive, LocalDate singleDriveDate) {
        this.userService = userService;
        this.driveRouteService = driveRouteService;
        this.driveRoute = driveRoute;
        this.mailService = mailService;
        this.driveRequestService = driveRequestService;
        this.singleDriveDate = singleDriveDate;
        this.isUserSearchsRegularDrive = isUserSearchsRegularDrive;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        HorizontalLayout driverInformationLayout = new HorizontalLayout();
        driverInformationLayout.setClassName("search-drive-result-view-driver_information_layout");

        H2 title = new H2("Fahrt von " + driveRoute.getDriver().getFullName());
        title.setId("search-drive-result-view-title");

        StarsRating driverRating = new StarsRating(driveRoute.getDriver().getUserRating().getAverageDriverRating());
        driverRating.setId("search-drive-result-view-driver_rating");
        driverRating.setManual(false);

        driverInformationLayout.add(title, driverRating);

        Button profileButton = new Button(VaadinIcon.USER.create());
        profileButton.setText("Profil");
        profileButton.setId("search-drive-result-view-profile_button");
        profileButton.addClickListener(e -> {
            close();
            UI.getCurrent().navigate(ProfileView.class,
                    new RouteParameters(new RouteParam("username", driveRoute.getDriver().getUsername())));

        });

        HorizontalLayout titleLayout = new HorizontalLayout(driverInformationLayout, profileButton);
        titleLayout.setClassName("search-drive-result-view-title_layout");
        add(titleLayout);

        switch (driveRoute.getDriveType()) {

            case OUTWARD_TRIP -> {
                FormLayoutDriveRoute formLayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                formLayoutDriveRouteTop.remove(formLayoutDriveRouteTop.getTitle());
                formLayoutDriveRouteTop.setData(driveRoute);
                formLayoutDriveRouteTop.setReadOnly(true);
                add(formLayoutDriveRouteTop);
            }
            case RETURN_TRIP -> {
                FormLayoutDriveRoute formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                formLayoutDriveRouteBottom.remove(formLayoutDriveRouteBottom.getTitle());
                formLayoutDriveRouteBottom.setData(driveRoute);
                formLayoutDriveRouteBottom.setReadOnly(true);
                add(formLayoutDriveRouteBottom);
            }

        }
        add(createButtons());
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private HorizontalLayout createButtons() {
        Button requestButton = new Button("Fahrt anfragen");
        requestButton.setClassName("search-drive-result-view-dialog-buttons");

        Button closeButton = new Button("Schließen");
        closeButton.setClassName("search-drive-result-view-dialog-buttons");
        closeButton.addClickListener(e -> close());

        requestButton.addClickListener(e -> {
            close();
            DriveRequestDialog driveRequestDialog = new DriveRequestDialog(
                    driveRoute,
                    userService,
                    driveRouteService,
                    mailService,
                    driveRequestService,
                    isUserSearchsRegularDrive,
                    singleDriveDate
            );
            driveRequestDialog.open();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(requestButton, closeButton);
        buttonLayout.setClassName("search-drive-result-view-dialog-button_layout");

        return buttonLayout;
    }

}
