package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.RouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.OwnDriveOffersEditDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.SearchDriveResultViewDialog;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * TODO: - Name passt nicht mehr da dieses Grid auch für SearchDriveResult benutzt wird.
 */
public class GridOwnDriveOffersView extends Grid<Route> {

    private static final String OWN_DRIVE_OFFERS_VIEW = "OwnDriveOffersView";
    private static final String SEARCH_DRIVE_RESULT_VIEW = "SearchDriveResultView";


    private final RouteService routeService;

    public GridOwnDriveOffersView(String zeitpunkt, List<Route> driveList, RouteService routeService) {
        this.routeService = routeService;

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setSelectionMode(SelectionMode.NONE);
        setId("gridOwnOffersView");
        //TODO Abfrage Absicherung
        setItems(driveList);

        addColumn(start -> start.getStart().getAdresse().getStreet() + " "
                + start.getStart().getAdresse().getHouseNumber() + ", "
                + start.getStart().getAdresse().getPostal() + " "
                + start.getStart().getAdresse().getPlace()).setHeader("Startadresse");

        addColumn(ziel -> ziel.getZiel().getAdresse().getStreet() + " "
                + ziel.getZiel().getAdresse().getHouseNumber() + ", "
                + ziel.getZiel().getAdresse().getPostal() + " "
                + ziel.getZiel().getAdresse().getPlace()).setHeader("Zieladresse");

        addColumn(new LocalDateTimeRenderer<>(item -> item.getZiel().getTime(),
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader(zeitpunkt);


        getColumns().get(0).setFooter("Anzahl:  " + driveList.size());

        addColumn(Route::getSeatCount).setHeader("Sitzplaetze");
        addColumn(Route::getDriveType).setHeader("Fahrentyp");

        addComponentColumn(this::createButtons);

        getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private Button createButtons(Route route) {
        Icon icon = new Icon(VaadinIcon.SEARCH);
        Button button = new Button();
        button.setIcon(icon);

        button.addClickListener(e->{

            if(UI.getCurrent().getId().get().equals(OWN_DRIVE_OFFERS_VIEW)) {
                OwnDriveOffersEditDialog ownDriveOffersEditDialog = new OwnDriveOffersEditDialog(route, routeService);
            }
            else if(UI.getCurrent().getId().get().equals(SEARCH_DRIVE_RESULT_VIEW)){
                SearchDriveResultViewDialog searchDriveResultViewDialog = new SearchDriveResultViewDialog(route);
            }
            else{
                throw new IllegalArgumentException("Fehler in " + getClass().getSimpleName() + "lol");
            }

        });

        return button;
    }

}
