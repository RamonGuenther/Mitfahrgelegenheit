package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.BookingService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.DriveDetailsDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.DriveRequestManageDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.util.Collections;

/**
 * Die Klasse DriveRequestListView erstellt eine View für das Anzeigen
 * der Fahrtanfragen. Hierbei wird unterschieden in Fahrtanfragen, die
 * der Benutzer zu seinen eigenen Fahrtangeboten bekommen hat und den
 * Anfragen, die der Benutzer selbst zu den Fahrtangeboten anderer
 * Benutzer gestellt hat.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Route(value = "fahrtanfragen", layout = MainLayout.class)
@PageTitle("Fahrtanfragen")
@CssImport("/themes/mitfahrgelegenheit/views/drive-request-list-view.css")
public class DriveRequestListView extends VerticalLayout {

    DriveRequestListView(DriveRouteService driveRouteService, UserService userService,
                         DriveRequestService driveRequestService, BookingService bookingService) {
        UI.getCurrent().setId(PageId.DRIVE_REQUEST_LIST_VIEW.label);

        Div verticalLayout = new Div();
        verticalLayout.setId("drive-request-list-view-layout");

        H1 title = new H1("Fahrtanfragen");

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Meine Fahrtangebote", "Meine Fahrtsuchen");

        Grid<DriveRequest> driverGrid = new Grid<>();
        driverGrid.setClassName("drive-request-list-view-grids");
        driverGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        driverGrid.setItems(driveRequestService.findAllDriveRequestsDriver(userService.getCurrentUser()).orElse(Collections.emptyList()));
        driverGrid.addColumn(requestDate -> requestDate.getFormattedDate() + ", " + requestDate.getFormattedTime() ).setHeader("Anfragedatum");
        driverGrid.addColumn(stopover -> stopover.getStopover().getFullAddressToString()).setHeader("Zwischenstopp");
        driverGrid.addComponentColumn(passenger -> new Anchor("/drivetogether/profil/" + passenger.getPassenger().getId(),
                passenger.getPassenger().getFullName())).setHeader("Mitfahrer");
        driverGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e -> {
                DriveRequestManageDialog driveRequestManageDialog = new DriveRequestManageDialog(driveRequestService, driveRouteService, item, bookingService);
                driveRequestManageDialog.open();
            });
            return showDriveRequestButton;
        }).setHeader("");

        driverGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        Grid<DriveRequest> passengerGrid = new Grid<>();
        passengerGrid.setClassName("drive-request-list-view-grids");

        passengerGrid.setItems(driveRequestService.findAllDriveRequestsPassenger(userService.getCurrentUser()).orElse(Collections.emptyList()));
        passengerGrid.addColumn(requestDate -> requestDate.getFormattedDate() + ", " + requestDate.getFormattedTime() ).setHeader("Anfragedatum");
        passengerGrid.addColumn(status -> status.getRequestState().label).setHeader("Anfragestatus");
        passengerGrid.addComponentColumn(driver -> new Anchor("/drivetogether/profil/" + driver.getDriveRoute().getDriver().getId(),
                driver.getDriveRoute().getDriver().getFullName())).setHeader("Fahrer");
        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.SEARCH.create());
            showDriveRequestButton.addClickListener(e -> {
                DriveDetailsDialog driveDetailsDialog = new DriveDetailsDialog(item);
                driveDetailsDialog.open();
            });
            return showDriveRequestButton;
        }).setHeader("");

        passengerGrid.addComponentColumn(item -> {
            Button showDriveRequestButton = new Button(VaadinIcon.TRASH.create());
            showDriveRequestButton.setId("drive-request-list-view-delete_button");
            showDriveRequestButton.addClickListener(e -> {
                item.getDriveRoute().removeDriveRequest(item);
                driveRouteService.save(item.getDriveRoute());
                driveRequestService.delete(item);
                passengerGrid.setItems(driveRequestService.findAllDriveRequestsPassenger(userService.getCurrentUser())
                        .orElse(Collections.emptyList()));
            });
            return showDriveRequestButton;
        }).setHeader("");

        passengerGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        passengerGrid.getColumns().get(3).setWidth("50px");
        passengerGrid.getColumns().get(4).setWidth("50px");

        radioButtonGroup.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Meine Fahrtangebote":
                    verticalLayout.removeAll();
                    verticalLayout.add(title, radioButtonGroup, driverGrid);
                    break;
                case "Meine Fahrtsuchen":
                    verticalLayout.removeAll();
                    verticalLayout.add(title, radioButtonGroup, passengerGrid);
                    break;
            }
        });
        radioButtonGroup.setValue("Meine Fahrtangebote");

        add(verticalLayout);
    }

}
