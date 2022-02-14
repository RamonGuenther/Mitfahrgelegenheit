package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.FahrerRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.OwnDriveOffersEditDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.SearchDriveResultViewDialog;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;

/**
 * TODO: - Name passt nicht mehr da dieses Grid auch für SearchDriveResult benutzt wird.
 */
public class GridOwnDriveOffersView extends Grid<FahrerRoute> {

    private static final String OWN_DRIVE_OFFERS_VIEW = "OwnDriveOffersView";
    private static final String SEARCH_DRIVE_RESULT_VIEW = "SearchDriveResultView";


    private final FahrerRouteService fahrerRouteService;

    public GridOwnDriveOffersView(String zeitpunkt, List<FahrerRoute> driveList, FahrerRouteService fahrerRouteService) {
        this.fahrerRouteService = fahrerRouteService;

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setSelectionMode(SelectionMode.NONE);
        setId("gridOwnOffersView");
        //TODO Abfrage Absicherung
        setItems(driveList);

        addColumn(start -> start.getStart().getAdresse().getStrasse() + " "
                + start.getStart().getAdresse().getHausnummer() + ", "
                + start.getStart().getAdresse().getPlz() + " "
                + start.getStart().getAdresse().getOrt()).setHeader("Startadresse");

        addColumn(ziel -> ziel.getZiel().getAdresse().getStrasse() + " "
                + ziel.getZiel().getAdresse().getHausnummer() + ", "
                + ziel.getZiel().getAdresse().getPlz() + " "
                + ziel.getZiel().getAdresse().getOrt()).setHeader("Zieladresse");

        addColumn(new LocalDateTimeRenderer<>(item -> item.getZiel().getZeit(),
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader(zeitpunkt);


        getColumns().get(0).setFooter("Anzahl:  " + driveList.size());

        addColumn(FahrerRoute::getSitzplaetze).setHeader("Sitzplaetze");
        addColumn(FahrerRoute::getFahrtenTyp).setHeader("Fahrentyp");

        addComponentColumn(this::createButtons);

        getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private Button createButtons(FahrerRoute fahrerRoute) {
        Icon icon = new Icon(VaadinIcon.SEARCH);
        Button button = new Button();
        button.setIcon(icon);

        button.addClickListener(e->{

            if(UI.getCurrent().getId().get().equals(OWN_DRIVE_OFFERS_VIEW)) {
                OwnDriveOffersEditDialog ownDriveOffersEditDialog = new OwnDriveOffersEditDialog(fahrerRoute, fahrerRouteService);
            }
            else if(UI.getCurrent().getId().get().equals(SEARCH_DRIVE_RESULT_VIEW)){
                SearchDriveResultViewDialog searchDriveResultViewDialog = new SearchDriveResultViewDialog(fahrerRoute);
            }
            else{
                throw new IllegalArgumentException("Fehler in " + getClass().getSimpleName() + "lol");
            }

        });

        return button;
    }

}
