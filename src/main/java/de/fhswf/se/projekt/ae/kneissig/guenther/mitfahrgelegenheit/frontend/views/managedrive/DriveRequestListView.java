package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.UI;
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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.DriveDetailsDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.DriveRequestManageDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.SearchDriveResultViewDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.util.List;


/**
 * Die Klasse DriveRequestListView erstellt eine View für das anzeigen
 * der Fahrtanfragen.
 *
 * @author Ramon Günther
 */
@Route(value = "fahrtanfragen", layout = MainLayout.class)
@PageTitle("Fahrtanfragen")
@CssImport("/themes/mitfahrgelegenheit/views/drive-request-list-view.css")
public class DriveRequestListView extends VerticalLayout {

    private String username;


    DriveRequestListView(DriveRouteService driveRouteService, UserService userService, DriveRequestService driveRequestService, BookingService bookingService){
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
        driverGrid.addColumn(DriveRequest::getFormattedDate).setHeader("Datum");
        driverGrid.addColumn(DriveRequest::getFormattedTime).setHeader("Uhrzeit");
        driverGrid.addColumn(passenger ->passenger.getPassenger().getFullName()).setHeader("Mitfahrer");
        driverGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e->{
                DriveRequestManageDialog driveRequestManageDialog = new DriveRequestManageDialog(driveRequestService, driveRouteService, item, bookingService);
                driveRequestManageDialog.open();
            });
            return showDriveRequestButton;
        }).setHeader("");


        Grid<DriveRequest> passengerGrid = new Grid<>();
        passengerGrid.setClassName("drive-request-list-view-grids");


        passengerGrid.setItems(driveRequestService.findAllDriveRequestsPassenger(userService.getCurrentUser()));

        passengerGrid.addColumn(name ->name.getDriveRoute().getDriver().getFullName()).setHeader("Name");
        passengerGrid.addColumn(DriveRequest::getFormattedDate).setHeader("Datum");
        passengerGrid.addColumn(DriveRequest::getFormattedTime).setHeader("Uhrzeit");
        passengerGrid.addColumn(status ->status.getRequestState().label).setHeader("Anfragestatus");


        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e->{
                DriveDetailsDialog driveDetailsDialog = new DriveDetailsDialog(item.getDriveRoute());
                driveDetailsDialog.open();
            });
            return showDriveRequestButton;
        }).setHeader("");

        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.TRASH.create());
            showDriveRequestButton.addClickListener(e->{
//                System.out.println(item.getDriveRoute().getDriveRequests().size());
//                item.getDriveRoute().removeDriveRequest(item);
//                driveRouteService.save(item.getDriveRoute());
//                System.out.println(item.getDriveRoute().getDriveRequests().size());
//                driveRequestService.delete(item);
//                UI.getCurrent().getPage().reload();
            });
            return showDriveRequestButton;
        }).setHeader("");

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
