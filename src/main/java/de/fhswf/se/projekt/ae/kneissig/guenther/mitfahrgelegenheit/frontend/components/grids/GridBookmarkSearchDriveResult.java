package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;

import java.util.Collection;

/**
 * Die Klasse erstellt eine Tabelle für die BookmarksView & SearchdriveResultView
 *
 * @author Ramon Günther
 */
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class GridBookmarkSearchDriveResult extends VerticalLayout { //beschissener Name


    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     *
     * @param s String der den Titel der Seite bestimmt
     */
    public GridBookmarkSearchDriveResult(String s, Collection<DriveRoute> driveList) {
        if(s.isEmpty()){
            throw new IllegalArgumentException("Fehler: String ist leer.");
        }
        createGrid(s, driveList);
    }

    /**
     * Die Methode createGrid erstellt eine Tabelle und fügt diese dem
     * VerticalLayout hinzu
     *
     * @param s String der den Titel der Seite bestimmt
     * @throws IllegalArgumentException if(s.isEmpty())
     *
     */
    private void createGrid(String s, Collection<DriveRoute> driveList){
        if(s.isEmpty()){
            throw new IllegalArgumentException("Fehler: String ist leer.");
        }
        H1 title = new H1(s);
        title.setId("titleSearchDriveResultBookMark");

        Grid<DriveRoute> grid = new Grid<>(DriveRoute.class);
        grid.setItems(driveList);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(start -> start.getStart().getAddress().getStreet() + " "
                + start.getStart().getAddress().getHouseNumber() + ", "
                + start.getStart().getAddress().getPostal() + " "
                + start.getStart().getAddress().getPlace()).setHeader("Startadresse");

        grid.addColumn(ziel -> ziel.getZiel().getAddress().getStreet() + " "
                + ziel.getZiel().getAddress().getHouseNumber() + ", "
                + ziel.getZiel().getAddress().getPostal() + " "
                + ziel.getZiel().getAddress().getPlace()).setHeader("Zieladresse");

//        grid.setColumns("ziel", "sitzplaetze");
//        grid.getColumnByKey("ziel").setFooter("Anzahl:  "  /*cardList.size()*/);
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(title,grid);
    }
}
