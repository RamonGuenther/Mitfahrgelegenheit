package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;


import java.time.LocalTime;
import java.util.ArrayList;
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

    private Label passengerName;
    private StarsRating passengerRating;
    private DatePicker date;
    private TimePicker driveTime;
    private TextArea textAreaMessage;
    private Anchor anchorGoogleMaps;
    private Button acceptRequest;
    private Button declineRequest;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    DriveRequestListView(){
        createDriveRequestListView();
    }


    /**
     * In der Methode createDriveRequestListView werden die Methoden
     * zum hinzufügen der Komponenten für das Layout, aufgerufen.
     */
    void createDriveRequestListView(){
        HorizontalLayout content = new HorizontalLayout();
        content.setId("contentDriveRequestList");
        createGrid(content);
        createFormLayout(content);
        add(content);
    }

    /**
     * In der Methode createGird wird die Tabelle für die linke Seite
     * der View erstellt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createGrid(HorizontalLayout horizontalLayout){

        VerticalLayout verticalLayout = new VerticalLayout();
        H1 title = new H1("Fahrtanfragen");

        List<FahrerRoute> driveList = new ArrayList<>();

        Grid<FahrerRoute> grid = new Grid<>(FahrerRoute.class);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES); //mit Ivonne Themes anschauen!

        grid.setId("contentLeftdriveRequestList");

        grid.setItems(driveList);
        grid.removeColumnByKey("id");

        grid.setColumns("ziel", "sitzplaetze");
        grid.getColumnByKey("ziel").setFooter("Anzahl:  "  /*cardList.size()*/);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        verticalLayout.add(title,grid);
        horizontalLayout.add(verticalLayout);
    }

    /**
     * In der Methode createFormLayout wird das FormLayout für die rechte Seite
     * der View erstellt.
     *
     * @param horizontalLayout horizontales Layout das die Komponenten übergibt
     */
    private void createFormLayout(HorizontalLayout horizontalLayout){

        LocalTime time =  LocalTime.of(12,45);

        passengerName = new Label("Anfrage von Ramon G.");

        passengerRating = new StarsRating(0);
        passengerRating.setId("passengerRatingDriveRequestList");
        passengerRating.setManual(true);

        HorizontalLayout header = new HorizontalLayout(passengerName,passengerRating);

        date = new DatePicker("Datum");

        driveTime = new TimePicker("An/Abfahrt");
        driveTime.setValue(time);

        textAreaMessage = new TextArea("Nachricht");
        textAreaMessage.setId("textAreaDriveRequestList");

        anchorGoogleMaps = new Anchor("https://www.youtube.com/watch?v=d3-iRa4C_sE");
        anchorGoogleMaps.removeAll();
        Button buttonDetourRoute = new Button("Route mit Umweg anzeigen", new Icon(VaadinIcon.CAR));
        buttonDetourRoute.setId("buttonGoogleMapsDriveRequestList");
        anchorGoogleMaps.add(buttonDetourRoute);
        anchorGoogleMaps.setTarget("_blank");
        anchorGoogleMaps.setId("anchorGoogleMapsDriveRequestList");


        acceptRequest = new Button("Anfrage bestätigen");
        acceptRequest.setClassName("buttonsDriveRequestList");
        acceptRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        declineRequest = new Button("Anfrage ablehnen");
        declineRequest.setClassName("buttonsDriveRequestList");
        declineRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("buttonLayoutDriveRequestList");
        buttonLayout.add(acceptRequest,declineRequest);


        FormLayout formLayout = new FormLayout(header,date, driveTime, textAreaMessage,anchorGoogleMaps);
        formLayout.setId("contentRightdriveRequestList");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));


        formLayout.setColspan(header,2);
        formLayout.setColspan(date,1);
        formLayout.setColspan(driveTime,1);
        formLayout.setColspan(textAreaMessage,2);
        formLayout.setColspan(anchorGoogleMaps,2);

        VerticalLayout verticalLayout = new VerticalLayout(formLayout, buttonLayout);
        horizontalLayout.add(verticalLayout);
    }
}
