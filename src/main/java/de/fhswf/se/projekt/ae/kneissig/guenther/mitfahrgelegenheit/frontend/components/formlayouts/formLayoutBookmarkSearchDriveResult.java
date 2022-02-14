package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.CheckboxRegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.StarsRating;

/**
 * Die Klasse erstellt eine Tabelle für die BookmarksView & SearchdriveResultView
 *
 * @author Ramon Günther
 */
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-result-view.css")
public class formLayoutBookmarkSearchDriveResult extends VerticalLayout {

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     *
     * @param s String der den Text des Buttons bestimmt
     */
    public formLayoutBookmarkSearchDriveResult(String s) {
        if(s.isEmpty()){
            throw new IllegalArgumentException("Fehler: String ist leer.");
        }
       createformLayout(s);
    }

    /**
     * Die Methode createGrid erstellt ein Formlayout und fügt diese dem
     * VerticalLayout hinzu
     *
     * @param s String der den Text des Buttons bestimmt
     * @throw IllegalArgumentException if(s.isEmpty())
     *
     */
    void createformLayout(String s){
        if(s.isEmpty()){
            throw new IllegalArgumentException("Fehler: String ist leer.");
        }
        HorizontalLayout header = new HorizontalLayout();

        String name = "Ramon G.";
        H2 titleSearchDriveResult = new H2("Fahrt von " + name);

        StarsRating driverRating = new StarsRating(0);
        driverRating.setId("ratingSearchDriveResult");
        driverRating.setManual(true);

        header.add(titleSearchDriveResult, driverRating);


        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();

        checkboxGroup.setItems("Regelmäßige Fahrt", "Umweg möglich", "Spritbeteiligung");


        DatePicker dateDrive = new DatePicker("Datum");
        dateDrive.setReadOnly(true);
        DatePicker dateEndRegular = new DatePicker("Bis");
        dateEndRegular.setReadOnly(true);

        CheckboxRegularDrive driveDays = new CheckboxRegularDrive();
        //driveDays.setReadOnly(true);

        TextField startAddress = new TextField("Von");
        startAddress.setReadOnly(true);

        TextField endAddress = new TextField("Nach");
        endAddress.setReadOnly(true);



        TimePicker driveTime = new TimePicker("Abfahrt");
        driveTime.setReadOnly(true);


        Anchor anchorGoogleMaps = new Anchor("https://www.youtube.com/watch?v=d3-iRa4C_sE", "Route mit Umweg anzeigen");
        anchorGoogleMaps.removeAll();
        Button buttonAnchoGoogleMaps = new Button("Route anzeigen", new Icon(VaadinIcon.CAR));
        buttonAnchoGoogleMaps.setId("buttonGoogleMapsSearchDrive");
        anchorGoogleMaps.add(buttonAnchoGoogleMaps);
        anchorGoogleMaps.setTarget("_blank");
        anchorGoogleMaps.setId("anchorGoogleMapsSearchDrive");



        Button profileButton = new Button("Profil");
        profileButton.setClassName("operationButtonBookmarkSearchDriveResult");
        profileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button bookmarkButton = new Button();
        bookmarkButton.setClassName("operationButtonBookmarkSearchDriveResult");
        bookmarkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        if(s.equals("Merkliste")){
            bookmarkButton.setText("Fahrt entfernen");
        }
        else{
            bookmarkButton.setText("Fahrt merken");
        }

        Button requestButton = new Button("Fahrt anfragen");
        requestButton.setClassName("operationButtonBookmarkSearchDriveResult");
        requestButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("buttonLayoutSearchDriveResultView");
        buttonLayout.add(profileButton, bookmarkButton, requestButton);

        FormLayout formLayout = new FormLayout(header, checkboxGroup, dateDrive, dateEndRegular, driveTime, driveDays, startAddress, endAddress, anchorGoogleMaps);


        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formLayout.setColspan(header, 4);
        formLayout.setColspan(checkboxGroup, 4);
        formLayout.setColspan(driveDays, 4);
        formLayout.setColspan(startAddress, 3);
        formLayout.setColspan(endAddress,3);
        formLayout.setColspan(anchorGoogleMaps, 3);
        formLayout.setId("searchDriveResultFormLayout");


        setId("searchDriveRightSide");
        add(formLayout,buttonLayout);
    }
}
