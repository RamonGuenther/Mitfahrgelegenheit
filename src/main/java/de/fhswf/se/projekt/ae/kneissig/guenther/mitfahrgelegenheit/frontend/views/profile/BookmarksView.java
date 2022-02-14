package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile;



import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.formLayoutBookmarkSearchDriveResult;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.GridBookmarkSearchDriveResult;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

/**
 * Die Klasse BookmarksView erstellt eine View für die Merkliste
 *
 * @author Ramon Günther
 */
@Route(value = "merkliste", layout = MainLayout.class)
@PageTitle("Merkliste Anzeigen")
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class BookmarksView extends VerticalLayout {

    private static final String STRING = "Merkliste";

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    BookmarksView() {
        setId("searchDriveResultView");
        createBookmarksview();
    }

    /**
     * In der Methode CreateSearchDriveResultView werden die Methoden
     * zum hinzufügen der Komponenten für das Layout, aufgerufen.
     */
    private void createBookmarksview() {
        HorizontalLayout content = new HorizontalLayout();
        content.setId("searchDriveResultContent");

        createGridLayout(content);
        createGridDetailsLayout(content);

        add(content);

    }

    /**
     * In der Methode createGridLayout werden wird eine Tabelle,
     * dem Layout auf der linken Seite hinzugefügt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createGridLayout(HorizontalLayout horizontalLayout) {
        GridBookmarkSearchDriveResult grid = new GridBookmarkSearchDriveResult(STRING, null);
        horizontalLayout.add(grid);
    }

    /**
     * In der Methode createGridDetailsLayout werden die Komponenten für eine
     * nähere Detailansicht der Tabelle, dem Layout auf der rechten Seite hinzugefügt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createGridDetailsLayout(HorizontalLayout horizontalLayout) {
        formLayoutBookmarkSearchDriveResult test = new formLayoutBookmarkSearchDriveResult(STRING);
        horizontalLayout.add(test);
    }

}