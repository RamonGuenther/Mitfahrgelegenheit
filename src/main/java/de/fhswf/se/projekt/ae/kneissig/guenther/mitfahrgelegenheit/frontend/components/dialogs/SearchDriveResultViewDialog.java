package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;

import java.time.LocalDate;

/**
 * Die Klasse DriveRequestDialog erstellt einen Dialog, auf dem noch einmal die Details
 * zu einem Fahrtangebot dargstellt werden. Der suchende Benutzer hat hier die Möglichkeit,
 * eine Anfrage zu dieser Fahrt zu erstellen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@CssImport("/themes/mitfahrgelegenheit/components/search-drive-result-view-dialog.css")
public class SearchDriveResultViewDialog extends Dialog {

    private final UserService userService;
    private final DriveRouteService driveRouteService;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;
    private final DriveRoute driveRoute;
    private final LocalDate singleDriveDate;
    private final boolean isUserSearchsRegularDrive;

    public SearchDriveResultViewDialog(DriveRoute driveRoute, UserService userService,
                                       DriveRouteService driveRouteService, MailService mailService,
                                       DriveRequestService driveRequestService, boolean isUserSearchsRegularDrive,
                                       LocalDate singleDriveDate) {

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

        Button profileButton = new Button(new Anchor("/profil/" + driveRoute.getDriver().getId(),
                driveRoute.getDriver().getFullName()));

        profileButton.setIcon(VaadinIcon.USER.create());
        profileButton.setText("Profil");
        profileButton.setId("search-drive-result-view-profile_button");
        profileButton.addClickListener(e -> close());

        HorizontalLayout titleLayout = new HorizontalLayout(driverInformationLayout, profileButton);
        titleLayout.setClassName("search-drive-result-view-title_layout");
        add(titleLayout);

        FormLayoutDriveRoute formLayoutDriveRoute = new FormLayoutDriveRoute(driveRoute.getDriveType());
        formLayoutDriveRoute.remove(formLayoutDriveRoute.getTitle());
        formLayoutDriveRoute.setData(driveRoute);
        formLayoutDriveRoute.setReadOnly(true);
        add(formLayoutDriveRoute);

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
