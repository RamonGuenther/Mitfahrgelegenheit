package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.DriveRouteGrid;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;


/**
 * Die Klasse OwnDriveOffersView erstellt eine View für die Auflistung
 * der eigenen Fahrtangebote
 *
 * @author Ramon Günther
 */
@Route(value = "eigeneFahrtangebote", layout = MainLayout.class)
@PageTitle("Eigene Fahrtangebote")
@CssImport("/themes/mitfahrgelegenheit/views/own-drive-offers-view.css")
public class OwnDriveOffersView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private final UserService userService;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    OwnDriveOffersView(DriveRouteService driveRouteService, UserService userService, MailService mailService, DriveRequestService driveRequestService) {
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;
        this.userService = userService;
        this.driveRequestService = driveRequestService;
        createGrids();
    }

    /**
     * In der Methode createGrids werden die Tabellen für die
     * Hin-& Rückfahrt erstellt.
     */
    private void createGrids() {
        H1 title = new H1("Eigene Fahrtangebote");

        UI.getCurrent().setId(PageId.OWN_DRIVE_OFFERS_VIEW.label);

        User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<DriveRoute> driveListTo = driveRouteService.getByUserAndDriveType(user, DriveType.OUTWARD_TRIP).orElse(Collections.emptyList());
        List<DriveRoute> driveListBack = driveRouteService.getByUserAndDriveType(user, DriveType.RETURN_TRIP).orElse(Collections.emptyList());

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");

        DriveRouteGrid gridHinfahrt = new DriveRouteGrid("Ankunftszeit", driveListTo, driveRouteService, userService, mailService,driveRequestService);
        gridHinfahrt.setId("gridOwnOffersView");
        DriveRouteGrid gridRueckfahrt = new DriveRouteGrid("Abfahrtzeit", driveListBack, driveRouteService, userService, mailService, driveRequestService);
        gridRueckfahrt.setId("gridOwnOffersView");

        Div div = new Div(title, radioButtonGroup, gridHinfahrt);
        div.setId("contentOwnDriveOffers");
        add(div);

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt" -> {
                    div.remove(gridRueckfahrt);
                    div.add(gridHinfahrt);
                }
                case "Rückfahrt" -> {
                    div.remove(gridHinfahrt);
                    div.add(gridRueckfahrt);
                }
            }
        });

    }

}
