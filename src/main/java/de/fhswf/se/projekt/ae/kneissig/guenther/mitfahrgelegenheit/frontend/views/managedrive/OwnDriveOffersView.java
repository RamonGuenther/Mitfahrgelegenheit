package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


/**
 * TODO: Grid: Nullwerte abfangen?
 *      -
 */

/**
 * Die Klasse OwnDriveOffersView erstellt eine View für die Auflistung
 * der eigenen Fahrtangebote
 *
 * @author Ramon Günther
 */
@com.vaadin.flow.router.Route(value = "eigeneFahrtangebote", layout = MainLayout.class)
@PageTitle("Eigene Fahrtangebote")
@CssImport("/themes/mitfahrgelegenheit/views/own-drive-offers-view.css")
public class OwnDriveOffersView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private final UserService userService;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    OwnDriveOffersView(DriveRouteService driveRouteService, UserService userService) {
        this.driveRouteService = driveRouteService;
        this.userService = userService;
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

        List<DriveRoute> driveListTo = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.OUTWARD_TRIP);
        List<DriveRoute> driveListBack = driveRouteService.findAllByBenutzerAndFahrtenTyp(user, DriveType.RETURN_TRIP);



        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
        radioButtonGroup.setValue("Hinfahrt");

        GridOwnDriveOffersView gridHinfahrt = new GridOwnDriveOffersView("Ankunftszeit", driveListTo, driveRouteService);
        gridHinfahrt.setId("gridOwnOffersView");
        GridOwnDriveOffersView gridRueckfahrt = new GridOwnDriveOffersView("Abfahrtzeit", driveListBack, driveRouteService);
        gridRueckfahrt.setId("gridOwnOffersView");

        Div div = new Div(title, radioButtonGroup, gridHinfahrt);
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

    }

}
