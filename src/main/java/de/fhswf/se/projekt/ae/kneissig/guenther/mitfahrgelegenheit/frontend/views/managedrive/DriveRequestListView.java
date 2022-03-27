package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.io.ByteArrayInputStream;
import java.util.List;


/**
 * Die Klasse DriveRequestListView erstellt eine View für das anzeigen
 * der Fahrtanfragen.
 *
 * @author Ramon Günther
 */

/**
 * TODO: Drive request nehmen und query schreiben wo man alle Anfragen kriegt wo der Fahrer eine Route hat-
 *          - Radiobuttons und extra Grid
 *          - siehe Wireframe!
 */
@Route(value = "fahrtanfragen", layout = MainLayout.class)
@PageTitle("Fahrtanfragen")
@CssImport("/themes/mitfahrgelegenheit/views/drive-request-list-view.css")
public class DriveRequestListView extends VerticalLayout {


    DriveRequestListView(DriveRouteService driveRouteService, UserService userService){

        VerticalLayout verticalLayout = new VerticalLayout();
        H1 title = new H1("Fahrtanfragen");


        List<DriveRequest> driveRequests = driveRouteService.findAllDriveRequest(userService.getCurrentUser());


//        Grid<DriveRequest> grid = new Grid<>(DriveRequest.class);
        Grid<DriveRequest> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setItems(driveRequests);
        grid.addColumn(date ->date.getRequestTime().toLocalDate()).setHeader("Datum");
        grid.addColumn(time ->time.getRequestTime().toLocalTime()).setHeader("Uhrzeit");
        grid.addColumn(passenger ->passenger.getPassenger().getFullName()).setHeader("Mitfahrer");
        grid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e->{

            });
            return showDriveRequestButton;
        }).setHeader("");


//        grid.addColumn(new LocalDateTimeRenderer<>(item -> item.getZiel().getTime(),
//                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader("zeitpunkt");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        verticalLayout.add(title,grid);
        add(verticalLayout);
    }

}
