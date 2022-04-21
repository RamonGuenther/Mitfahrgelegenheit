package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.OwnDriveOffersEditDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.SearchDriveResultViewDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class DriveRouteGrid extends Grid<DriveRoute> {

    private final DriveRouteService driveRouteService;

    private final UserService userService;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;

    public DriveRouteGrid(String zeitpunkt, List<DriveRoute> driveList, DriveRouteService driveRouteService, UserService userService, MailService mailService, DriveRequestService driveRequestService) {
        this.driveRouteService = driveRouteService;
        this.userService = userService;
        this.mailService = mailService;
        this.driveRequestService = driveRequestService;

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setSelectionMode(SelectionMode.NONE);

        setItems(driveList.stream().filter(driveRoute -> driveRoute.getDrivingTime().isAfter(LocalDateTime.now())).collect(Collectors.toList()));

        addColumn(start -> start.getStart().getAddress().getStreet() + " "
                + start.getStart().getAddress().getHouseNumber() + ", "
                + start.getStart().getAddress().getPostal() + " "
                + start.getStart().getAddress().getPlace()).setHeader("Startadresse");

        addColumn(ziel -> ziel.getZiel().getAddress().getStreet() + " "
                + ziel.getZiel().getAddress().getHouseNumber() + ", "
                + ziel.getZiel().getAddress().getPostal() + " "
                + ziel.getZiel().getAddress().getPlace()).setHeader("Zieladresse");

        addColumn(driveRoute -> driveRoute.getRegularDrive().getRegularDriveDay() == null ?
                driveRoute.getFormattedDate() + ", " + driveRoute.getFormattedTime() :
                driveRoute.getRegularDrive().getRegularDriveDay().label + "s, " + driveRoute.getDrivingTime().toLocalTime().toString() + " Uhr").setHeader(zeitpunkt);

        addColumn(DriveRoute::getSeatCount).setHeader("Sitzplätze");

        addComponentColumn(driver -> new Anchor("/profil/" + driver.getDriver().getUsername(),
                driver.getDriver().getFullName())).setHeader("Fahrer");

        getColumns().get(0).setFooter("Anzahl:  " + driveList.size());

        addComponentColumn(this::createButtons);

        getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Button createButtons(DriveRoute driveRoute) {
        Icon icon = new Icon(VaadinIcon.SEARCH);
        Button button = new Button();
        button.setIcon(icon);

        button.addClickListener(e -> {

            if (UI.getCurrent().getId().get().equals(PageId.OWN_DRIVE_OFFERS_VIEW.label)) {
                OwnDriveOffersEditDialog ownDriveOffersEditDialog = new OwnDriveOffersEditDialog(driveRoute, driveRouteService, mailService);
            } else if (UI.getCurrent().getId().get().equals(PageId.SEARCH_DRIVE_RESULT_VIEW.label)) {
                SearchDriveResultViewDialog searchDriveResultViewDialog = new SearchDriveResultViewDialog(driveRoute, userService, driveRouteService, mailService, driveRequestService);
            } else if (UI.getCurrent().getId().get().equals(PageId.PROFILE.label)) {
                SearchDriveResultViewDialog searchDriveResultViewDialog = new SearchDriveResultViewDialog(driveRoute, userService, driveRouteService, mailService, driveRequestService);
            } else {
                throw new IllegalArgumentException("Fehler in " + getClass().getSimpleName() + "lol");
            }

        });

        return button;
    }

}
