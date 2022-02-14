package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BenutzerService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.FahrerRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

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
@Route(value = "eigeneFahrtangebote", layout = MainLayout.class)
@PageTitle("Eigene Fahrtangebote")
@CssImport("/themes/mitfahrgelegenheit/views/own-drive-offers-view.css")
public class OwnDriveOffersView extends VerticalLayout {

    private static final String PAGE_ID = "OwnDriveOffersView";

    private final FahrerRouteService fahrerRouteService;
    private final BenutzerService benutzerService;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    OwnDriveOffersView(FahrerRouteService fahrerRouteService, BenutzerService benutzerService) {
        this.fahrerRouteService = fahrerRouteService;
        this.benutzerService = benutzerService;
        createGrids();
    }

    /**
     * In der Methode createGrids werden die Tabellen für die
     * Hin-& Rückfahrt erstellt.
     *
     */
    private void createGrids() {
        H1 title = new H1("Eigene Fahrtangebote");

        UI.getCurrent().setId(PAGE_ID);

        //TODO BENUTZER IN POST KONSTRUKT EINFÜGEN!!!!!
//        Benutzer benutzer = benutzerService.
//       List<FahrerRoute> driveList = fahrerRouteService.findAllFahrerRoutenByBenutzer();
//
//        driveList.getSuccess().ifPresent(d -> {
//
//            RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
//            radioButtonGroup.setItems("Hinfahrt", "Rückfahrt");
//            radioButtonGroup.setValue("Hinfahrt");
//
//            GridOwnDriveOffersView gridHinfahrt = new GridOwnDriveOffersView("Ankunftszeit", d.hinfahrten(), interactor);
//            GridOwnDriveOffersView gridRueckfahrt = new GridOwnDriveOffersView("Abfahrtzeit",d.rueckfahrten(), interactor);
//            Div div = new Div(title, radioButtonGroup, gridHinfahrt);
//            div.setId("contentOwnDriveOffers");
//            add(div);
//
//            radioButtonGroup.addValueChangeListener(e -> {
//               switch(e.getValue()){
//                   case "Hinfahrt":
//                       div.remove(gridRueckfahrt);
//                       div.add(gridHinfahrt);
//                       break;
//
//                   case "Rückfahrt":
//                       div.remove(gridHinfahrt);
//                       div.add(gridRueckfahrt);
//                       break;
//               }
//            });
//        });

    }

}
