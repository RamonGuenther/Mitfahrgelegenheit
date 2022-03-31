package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

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

    private String username;


    DriveRequestListView(DriveRouteService driveRouteService, UserService userService, DriveRequestService driveRequestService){
        username = userService.getCurrentUser().getUsername();

        Div verticalLayout = new Div();
        verticalLayout.setId("drive-request-list-view-layout");

        H1 title = new H1("Fahrtanfragen");


        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Meine Fahrtangebote", "Meine Fahrtsuchen");



        Grid<DriveRequest> driverGrid = new Grid<>();
        driverGrid.setClassName("drive-request-list-view-grids");
        driverGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        driverGrid.setItems(driveRequestService.findAllDriveRequestsDriver(userService.getCurrentUser()));
        driverGrid.addColumn(date ->date.getRequestTime().toLocalDate()).setHeader("Datum");
        driverGrid.addColumn(time ->time.getRequestTime().toLocalTime()).setHeader("Uhrzeit");
        driverGrid.addColumn(passenger ->passenger.getPassenger().getFullName()).setHeader("Mitfahrer");
        driverGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e->{

            });
            return showDriveRequestButton;
        }).setHeader("");


        Grid<DriveRequest> passengerGrid = new Grid<>();
        passengerGrid.setClassName("drive-request-list-view-grids");


        passengerGrid.setItems(driveRequestService.findAllDriveRequestsPassenger(userService.getCurrentUser()));

        passengerGrid.addColumn(name ->name.getDriveRoute().getDriver().getFullName()).setHeader("Name");
        passengerGrid.addColumn(date ->date.getRequestTime().toLocalDate()).setHeader("Datum");
        passengerGrid.addColumn(time ->time.getRequestTime().toLocalTime()).setHeader("Uhrzeit");
        passengerGrid.addColumn(status ->status.getRequestState().label).setHeader("Anfragestatus");


        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e->{

            });
            return showDriveRequestButton;
//            return new Label(driveRouteService.findDriveRouteByDriveRequest(item.getApfel(), item.getPassenger().getUsername()).getBenutzer().getFullName());
        }).setHeader("");

        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.TRASH.create());
            showDriveRequestButton.addClickListener(e->{

            });
            return showDriveRequestButton;
        }).setHeader("");

//        grid.addColumn(new LocalDateTimeRenderer<>(item -> item.getZiel().getTime(),
//                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))).setHeader("zeitpunkt");

        passengerGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        passengerGrid.getColumns().get(3).setWidth("50px");
        passengerGrid.getColumns().get(4).setWidth("50px");

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Meine Fahrtangebote" -> {
                    verticalLayout.removeAll();
                    verticalLayout.add(title,radioButtonGroup,driverGrid);
                }
                case "Meine Fahrtsuchen" -> {
                    verticalLayout.removeAll();
                    verticalLayout.add(title,radioButtonGroup,passengerGrid);
                }
            }
        });

        radioButtonGroup.setValue("Meine Fahrtangebote");


        add(verticalLayout);
    }

}
