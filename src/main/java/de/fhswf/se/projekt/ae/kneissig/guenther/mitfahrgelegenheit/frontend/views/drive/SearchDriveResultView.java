package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
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
 * TODO: Wieder so machen, dass man wenn keine Orte gefunden wurden die anderen angezeigt werden
 */

/**
 * Die Klasse SearchDriveResultView erstellt eine View für die Suchergebnisse
 * aus SearchDriveView
 *
 * @author Ramon Günther
 */
@com.vaadin.flow.router.Route(value = "fahrtensucheErgebnis/fahrtentyp/:fahrtentyp/fhStandort/:fhStandort/adresse/:adresse/search", layout = MainLayout.class)
@PageTitle("Ergebnis Fahrtensuche")
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class SearchDriveResultView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver{

    private static final String TITEL_GRID = "Suchergebnisse";

    private final DriveRouteService driveRouteService;
    private final UserService userService;

    private List<DriveRoute> driveList;
    private DriveType fahrtenTyp;
    private String typ;
    private String fhStandort;
    private String adresse;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public SearchDriveResultView(DriveRouteService driveRouteService, UserService userService) {
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        UI.getCurrent().setId(PageId.SEARCH_DRIVE_RESULT_VIEW.label);
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
        GridOwnDriveOffersView grid = new GridOwnDriveOffersView("Ankunftszeit", driveList, driveRouteService, userService);
        grid.setId("gridOwnOffersView");
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
        User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        switch (typ) {
            case "Hinfahrt" ->{
                fahrtenTyp = DriveType.OUTWARD_TRIP;
            }
            case "Rückfahrt" -> {
                fahrtenTyp = DriveType.RETURN_TRIP;
            }
        }
        driveList = driveRouteService.findRouten(user,fahrtenTyp,adresse,fhStandort);


        //If fahrten leer notification und zur Searchdrive zurück

        CreateSearchDriveResultView();
    }
}
