package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Buchung;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.CheckboxRegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;


/**
 * TODO: Überarbeiten is schrott
 */
@Route(value = "geplanteFahrten", layout = MainLayout.class)
@PageTitle("Fahrten-Kalender")
@CssImport("/themes/mitfahrgelegenheit/views/calendar-view.css")
public class PlannedDriveView extends VerticalLayout {

    private DatePicker datePicker;
    private Grid<Buchung> calendarGrid;
    private TextField date;
    private TextField time;
    private TextField driver;
    private TextField passenger;
    private Anchor driverRoute;
    private CheckboxRegularDrive checkboxRegularDrive;
    private CheckboxGroup checkboxGasoline;

    public PlannedDriveView(){

        addClassName("driveCalendarView");
        createDriveCalendarView();
    }

    private void createDriveCalendarView(){

        // linke Seite der View
        H1 title = new H1("Fahrten-Kalender");
        title.addClassName("driveCalendarView");
        title.setId("calendarViewTitle");

        datePicker = new DatePicker();
        datePicker.addClassName("calendarViewLeft");

        calendarGrid = new Grid<>(Buchung.class);
        calendarGrid.setColumns("id");
        calendarGrid.addClassName("calendarViewLeft");
        calendarGrid.setId("calendarViewGrid");

        VerticalLayout leftSide = new VerticalLayout(datePicker, calendarGrid);
        leftSide.addClassName("calendarViewLeft");

        // rechte Seite der View
        H2 headerDriveDetails = new H2 ("Fahrt nach/von ...");
        headerDriveDetails.setId("calendarViewRightTitle");

        date = new TextField("Datum");
        date.setReadOnly(true);

        time = new TextField("Uhrzeit");
        time.setReadOnly(true);

        driver = new TextField("Fahrer");
        driver.setReadOnly(true);

        passenger = new TextField("Mitfahrer");
        passenger.setReadOnly(true);

//        driverRoute = new Anchor(new RouteString("Diesterwegstraße 6 58095 Hagen",
//                "Paschestraße 28 58089 Hagen").getRoute(), "Route");
//        driverRoute.removeAll();
//        Button buttonDriverRoute = new Button("Route", new Icon(VaadinIcon.CAR));
//        buttonDriverRoute.setId("calenderViewRouteButton");
//        driverRoute.add(buttonDriverRoute);
//        driverRoute.setTarget("_blank");
//        driverRoute.setId("calenderViewRoute");

        checkboxRegularDrive = new CheckboxRegularDrive();
        checkboxRegularDrive.setLabel("Dies ist eine regelmäßige Fahrt für folgende Wochentage:");
        checkboxRegularDrive.setId("calendarViewRegularDrive");
        checkboxRegularDrive.setReadOnly(true);

        checkboxGasoline = new CheckboxGroup();
        checkboxGasoline.setItems("ja", "nein");
        checkboxGasoline.setLabel("Spritbeteiligung?");
        checkboxGasoline.setId("calendarViewGasoline");
        checkboxGasoline.setReadOnly(true);

        HorizontalLayout checkboxes = new HorizontalLayout(checkboxRegularDrive, checkboxGasoline);

        FormLayout formLayout = new FormLayout(date, time, driver, passenger, checkboxes, driverRoute);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        formLayout.setColspan(date,1);
        formLayout.setColspan(time,1);
        formLayout.setColspan(driver, 1);
        formLayout.setColspan(passenger,1);
        formLayout.setColspan(checkboxes,2);
        formLayout.setColspan(driverRoute,2);
        formLayout.addClassName("calendarViewRight");

        Button buttonCancelDrive = new Button("Diese Fahrt absagen");
        buttonCancelDrive.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonCancelDrive.addClassName("calendarViewButtons");

        Button buttonCancelAll = new Button("Regelmäßige Fahrt absagen");
        buttonCancelAll.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonCancelAll.addClassName("calendarViewButtons");

        HorizontalLayout buttonLayout = new HorizontalLayout(buttonCancelDrive, buttonCancelAll);
        buttonLayout.addClassName("calendarViewRight");
        buttonLayout.setId("calendarViewButtons");

        VerticalLayout rightSide = new VerticalLayout();
        rightSide.add(headerDriveDetails, formLayout, buttonLayout);
        rightSide.addClassName("calendarViewRight");

        // Zusammenfügen der gesamten View
        HorizontalLayout calendarWithDetails = new HorizontalLayout(leftSide, rightSide);
        calendarWithDetails.addClassName("driveCalendarView");

        add(title,calendarWithDetails);

    }
}
