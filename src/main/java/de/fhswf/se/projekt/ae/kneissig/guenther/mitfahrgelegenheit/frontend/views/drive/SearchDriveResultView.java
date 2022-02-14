package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Benutzer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BenutzerService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.FahrerRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridOwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

/**
 * TODO: Wieder so machen, dass man wenn keine Orte gefunden wurden die anderen angezeigt werden
 */

/**
 * Die Klasse SearchDriveResultView erstellt eine View für die Suchergebnisse
 * aus SearchDriveView
 *
 * @author Ramon Günther
 */
@Route(value = "fahrtensucheErgebnis/fahrtentyp/:fahrtentyp/fhStandort/:fhStandort/adresse/:adresse/search", layout = MainLayout.class)
@PageTitle("Ergebnis Fahrtensuche")
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class SearchDriveResultView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver{

    private static final String PAGE_ID = "SearchDriveResultView";
    private static final String TITEL_GRID = "Suchergebnisse";

    private final FahrerRouteService fahrerRouteService;
    private final BenutzerService benutzerService;

    private List<FahrerRoute> driveList;
    private DriveType fahrtenTyp;
    private String typ;
    private String fhStandort;
    private String adresse;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public SearchDriveResultView(FahrerRouteService fahrerRouteService, BenutzerService benutzerService) {
        this.fahrerRouteService = fahrerRouteService;
        this.benutzerService = benutzerService;
        UI.getCurrent().setId(PAGE_ID);
        setId("searchDriveResultView");
    }

    /**
     * In der Methode CreateSearchDriveResultView werden die Methoden
     * zum hinzufügen der Komponenten für das Layout, aufgerufen.
     */
    private void CreateSearchDriveResultView() {
        createGridLayout();
    }

    /**
     * In der Methode createGridLayout werden die Komponenten der Tabelle,
     * dem Layout hinzugefügt.
     *
     */
    private void createGridLayout() { //Hier entsteht grid , dann klicken wir eine Sache an die runter zu createGridDetailsLayout gebracht werden muss
        Div div = new Div();
        div.setId("search-drive-result-view-content-div");

        H1 title = new H1(TITEL_GRID);
//        GridBookmarkSearchDriveResult grid = new GridBookmarkSearchDriveResult(TITEL_GRID, driveList);
        GridOwnDriveOffersView grid = new GridOwnDriveOffersView("Ankunftszeit", driveList,fahrerRouteService);
        div.add(title,grid);
        add(div);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println("Hallo");
        if(beforeEnterEvent.getRouteParameters().get("fahrtentyp").isPresent()){
            typ = beforeEnterEvent.getRouteParameters().get("fahrtentyp").get();
        }

        if(beforeEnterEvent.getRouteParameters().get("fhStandort").isPresent()){
            fhStandort = beforeEnterEvent.getRouteParameters().get("fhStandort").get();
        }

        if(beforeEnterEvent.getRouteParameters().get("adresse").isPresent()){
            adresse = beforeEnterEvent.getRouteParameters().get("adresse").get();
        }

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        Benutzer benutzer = benutzerService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        switch (typ) {
            case "Hinfahrt" ->{
                fahrtenTyp = DriveType.HINFAHRT;
            }
            case "Rückfahrt" -> {
                fahrtenTyp = DriveType.RUECKFAHRT;
            }
        }
        driveList = fahrerRouteService.findRouten(benutzer,fahrtenTyp,adresse,fhStandort);


        //If fahrten leer notification und zur Searchdrive zurück

        CreateSearchDriveResultView();
    }
}
