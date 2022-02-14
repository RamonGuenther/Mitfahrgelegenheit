package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.completeddrive.RatingButton;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.completeddrive.ReportButton;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.completeddrive.Role;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Die Klasse CompletedDriveView erstellt eine View für das anzeigen
 * der abgeschlossenen Fahrten, aus Fahrer- und Mitfahrersicht, um
 * eine Person zu bewerten oder zu melden.
 *
 * @author Ramon Günther
 */
@Route(value = "abgeschlosseneFahrten", layout = MainLayout.class)
@PageTitle("Abgeschlossene Fahrten")
@CssImport("/themes/mitfahrgelegenheit/views/completed-drive-view.css")
public class CompletedDriveView extends VerticalLayout {

    private Label personName;
    private StarsRating passengerRating;
    private RadioButtonGroup<String> radioButtonGrid;
    private Role role;
    private RatingButton ratingButton;
    private ReportButton reportButton;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig
     */
    CompletedDriveView() {
        createCompletedDriveView();
    }

    /**
     * In der Methode createCompletedDriveView werden die Methoden
     * zum hinzufügen der Komponenten für das Layout, aufgerufen.
     */
    private void createCompletedDriveView() {
        HorizontalLayout content = new HorizontalLayout();
        content.setId("completeDriveContent");
        createGrids(content);
        createFormlayout(content);
        radioButtonGrid.setValue("angebotene Fahrten");

        ratingButton.addClickListener(e -> ratingButton.ratingButtonEvent(role));
        reportButton.addClickListener(e -> reportButton.reportButtonEvent());

        add(content);

    }


    /**
     * In der Methode createGrids werden die Tabellen,
     * dem Layout auf der linken Seite hinzugefügt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createGrids(HorizontalLayout horizontalLayout) {
        VerticalLayout verticalLayout = new VerticalLayout();
        H1 title = new H1("Abgeschlossene Fahrten");
        title.setId("titleCompletedDrive");

        List<FahrerRoute> driveList = new ArrayList<>();


        Grid<FahrerRoute> gridUp = new Grid<>(FahrerRoute.class);
        gridUp.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridUp.setClassName("gridCompletedDrive");



        Grid<FahrerRoute> gridDown = new Grid<>(FahrerRoute.class);
        gridDown.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridDown.setClassName("gridCompletedDrive");


        radioButtonGrid = new RadioButtonGroup<>();
        radioButtonGrid.setItems("angebotene Fahrten", "mitgefahrende Fahrten");

        radioButtonGrid.addValueChangeListener(event -> {
            String choice = event.getValue();
            switch (choice) {
                case "angebotene Fahrten":
                    role = Role.FAHRER;
                    verticalLayout.remove(gridDown);
                    verticalLayout.add(gridUp);
                    personName.setText("Fahrt mit Ivonnilein");
                    break;

                case "mitgefahrende Fahrten":
                    role = Role.MITFAHRER;
                    personName.setText("Fahrt von Ramonilein");
                    verticalLayout.remove(gridUp);
                    verticalLayout.add(gridDown);
                    break;

                default:
                    break;
            }
        });

        verticalLayout.add(title, radioButtonGrid);


        horizontalLayout.add(verticalLayout);

    }

    /**
     * In der Methode createFormLayout werden die Komponenten für eine
     * nähere Detailansicht der Tabelle, dem Layout auf der rechten Seite hinzugefügt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createFormlayout(HorizontalLayout horizontalLayout) {

        LocalTime time = LocalTime.of(12, 45);
        LocalDate dateExample = LocalDate.of(2021, 7, 8);

        personName = new Label();

        passengerRating = new StarsRating(0);
        passengerRating.setId("ratingCompletedDrive");
        passengerRating.setManual(true);

        HorizontalLayout header = new HorizontalLayout(personName, passengerRating);

        DatePicker date = new DatePicker("Datum");
        date.setReadOnly(true);

        date.setValue(dateExample);

        TimePicker driveTimeLeft = new TimePicker("An/Abfahrt");
        driveTimeLeft.setReadOnly(true);
        driveTimeLeft.setValue(time);

        TextField driveStartPoint = new TextField("Von");
        driveStartPoint.setReadOnly(true);


        TextField driveEndPoint = new TextField("Nach");
        driveEndPoint.setReadOnly(true);


        Anchor anchorGoogleMaps = new Anchor("https://www.youtube.com/watch?v=d3-iRa4C_sE");
        anchorGoogleMaps.removeAll();
        Button buttonDetourRoute = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
        buttonDetourRoute.setId("buttonGoogleMapsCompletedDrive");
        anchorGoogleMaps.add(buttonDetourRoute);
        anchorGoogleMaps.setTarget("_blank");
        anchorGoogleMaps.setId("anchorGoogleMapsCompletedDrive");

        ratingButton = new RatingButton();

        reportButton = new ReportButton();


        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("buttonLayoutCompletedDrive");
        buttonLayout.add(ratingButton, reportButton);


        FormLayout formLayout = new FormLayout(header, date, driveTimeLeft, driveStartPoint, driveEndPoint, anchorGoogleMaps);
        formLayout.setId("contentRightCompletedDrive");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formLayout.setColspan(header, 4);
        formLayout.setColspan(date, 2);
        formLayout.setColspan(driveTimeLeft, 2);
        formLayout.setColspan(driveStartPoint, 4);
        formLayout.setColspan(driveEndPoint, 4);
        formLayout.setColspan(anchorGoogleMaps, 4);


        VerticalLayout verticalLayout = new VerticalLayout(formLayout, buttonLayout);
        horizontalLayout.add(verticalLayout);

    }
}
