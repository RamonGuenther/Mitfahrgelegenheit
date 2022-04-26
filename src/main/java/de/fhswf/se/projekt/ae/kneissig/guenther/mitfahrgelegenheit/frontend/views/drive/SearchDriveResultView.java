package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.MailService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.grids.DriveRouteGrid;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * TODO: Wieder so machen, dass man wenn keine Orte gefunden wurden die anderen angezeigt werden
 */

/**
 * Die Klasse SearchDriveResultView erstellt eine View für die Suchergebnisse
 * aus SearchDriveView
 *
 * @author Ramon Günther
 */
@Route(value = "fahrtensucheErgebnis/fahrtentyp/:fahrtentyp/fhStandort/:fhStandort/adresse/:adresse/datum/:datum/uhrzeit/:uhrzeit/:regelmaessig/wochentag/:wochentag/search", layout = MainLayout.class)
@PageTitle("Ergebnis Fahrtensuche")
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class SearchDriveResultView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private static final String TITEL_GRID = "Suchergebnisse";

    private final DriveRouteService driveRouteService;
    private final UserService userService;
    private final MailService mailService;
    private final DriveRequestService driveRequestService;

    private List<DriveRoute> driveList;
    private DriveType fahrtenTyp;
    private String typ;
    private String fhStandort;
    private String adresse;
    private String date;
    private String time;
    private boolean isUserSearchsRegularDrive;
    private DayOfWeek dayOfWeek;
    private LocalDateTime dateTime;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public SearchDriveResultView(DriveRouteService driveRouteService, UserService userService, MailService mailService, DriveRequestService driveRequestService) {
        this.driveRouteService = driveRouteService;
        this.mailService = mailService;
        this.userService = userService;
        this.driveRequestService = driveRequestService;
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
     */
    private void createGridLayout() {
        Div div = new Div();
        div.setId("search-drive-result-view-content-div");

        H1 title = new H1(TITEL_GRID);
        DriveRouteGrid grid = new DriveRouteGrid("Ankunftszeit", driveList, driveRouteService, userService, mailService, driveRequestService, isUserSearchsRegularDrive,
                Objects.equals(date,"keinDatum")? LocalDate.now() : LocalDate.of(
                        Integer.parseInt(date.substring(0, 4)),
                        date.substring(5,6).contains("0") ? Integer.parseInt(date.substring(6,7)) : Integer.parseInt(date.substring(5,7)),
                        date.substring(8,9).contains("0") ? Integer.parseInt(date.substring(9)) : Integer.parseInt(date.substring(8))));
        grid.setId("gridOwnOffersView");
        div.add(title, grid);
        add(div);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getRouteParameters().get("fahrtentyp").isPresent()) {
            typ = beforeEnterEvent.getRouteParameters().get("fahrtentyp").get();
        }

        if (beforeEnterEvent.getRouteParameters().get("fhStandort").isPresent()) {
            fhStandort = beforeEnterEvent.getRouteParameters().get("fhStandort").get();
        }

        if (beforeEnterEvent.getRouteParameters().get("adresse").isPresent()) {
            adresse = beforeEnterEvent.getRouteParameters().get("adresse").get();
        }

        if (beforeEnterEvent.getRouteParameters().get("datum").isPresent()) {
            date = beforeEnterEvent.getRouteParameters().get("datum").get();
        }

        if (beforeEnterEvent.getRouteParameters().get("uhrzeit").isPresent()) {
            time = beforeEnterEvent.getRouteParameters().get("uhrzeit").get();
        }

        if (beforeEnterEvent.getRouteParameters().get("regelmaessig").isPresent()) {
            isUserSearchsRegularDrive = Boolean.parseBoolean(beforeEnterEvent.getRouteParameters().get("regelmaessig").get());
        }


        if (beforeEnterEvent.getRouteParameters().get("wochentag").isPresent()) {
            if(!beforeEnterEvent.getRouteParameters().get("wochentag").get().equals("keinTag")){
                dayOfWeek = DayOfWeek.getDayOfWeek(beforeEnterEvent.getRouteParameters().get("wochentag").get());
            }
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(!Objects.equals(date, "keinDatum")) {
            dateTime = LocalDateTime.of(
                    LocalDate.of(
                            Integer.parseInt(date.substring(0, 4)),
                            date.substring(5, 6).contains("0") ? Integer.parseInt(date.substring(6, 7)) : Integer.parseInt(date.substring(5, 7)),
                            date.substring(8, 9).contains("0") ? Integer.parseInt(date.substring(9)) : Integer.parseInt(date.substring(8))),
                    LocalTime.of(
                            Integer.parseInt(time.substring(0, 2)),
                            Integer.parseInt(time.substring(3)))
            );
        }

        switch (typ) {
            case "Hinfahrt" -> {
                fahrtenTyp = DriveType.OUTWARD_TRIP;
                driveList = driveRouteService.getDriveRoutesForSearchDrive(fahrtenTyp, adresse, fhStandort, user, dateTime, isUserSearchsRegularDrive, dayOfWeek);
            }
            case "Rückfahrt" -> {
                fahrtenTyp = DriveType.RETURN_TRIP;
                driveList = driveRouteService.getDriveRoutesForSearchDrive(fahrtenTyp, fhStandort, adresse, user, dateTime, isUserSearchsRegularDrive, dayOfWeek);
            }
        }
        CreateSearchDriveResultView();
    }
}
