package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.SelectUniversityLocation;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.TextFieldAddress;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.localDateCheck;
import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.nullCheck;

/**
 * Die Klasse SearchDriveView erstellt eine View zum Suchen einer
 * Mitfahrgelegenheit.
 *
 * @author Ivonne Kneißig und Ramon Günther
 */

@Route(value = "fahrtSuchen", layout = MainLayout.class)
@PageTitle("Fahrt Suchen")
@CssImport("/themes/mitfahrgelegenheit/views/search-drive-view.css")
public class SearchDriveView extends VerticalLayout {

    private final UserService userService;
    private Checkbox checkboxRegularDrive;
    private RadioButtonGroup<String> radioDriveDirection;
    private TextFieldAddress address;
    private DatePicker date;
    private SelectUniversityLocation fhLocation;
    private TimePicker time;

    private Select<String> dayOfWeek;

    /**
     * Der Konstruktor ist für das Erstellen der View zum Suchen
     * einer Mitfahrgelegenheit zuständig.
     */

    public SearchDriveView(UserService userService) {
        this.userService = userService;
        setId("searchView");
        createSearchView();
    }

    /**
     * In der Methode createSearchView werden die einzelnen Komponenten
     * der View erzeugt und zusammengefügt.
     */

    private void createSearchView() {

        H1 title = new H1("Mitfahrgelegenheit suchen");
        title.setId("titleFahrtSuchen");

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrten");
        checkboxRegularDrive.setId("checkBoxRegularDrive");

        radioDriveDirection = new RadioButtonGroup<>();
        radioDriveDirection.setItems("Hinfahrt", "Rückfahrt");
        radioDriveDirection.setValue("Hinfahrt");
        radioDriveDirection.setId("radioDriveDirection");

        address = new TextFieldAddress("Von");
        address.setId("textfieldStart");

        date = new DatePicker("Datum");
        date.setErrorMessage("Datum darf nicht in der Vergangenheit liegen");
        date.setMin(LocalDate.now());
        date.setId("datepicker");

        fhLocation = new SelectUniversityLocation();
        fhLocation.setValue(userService.getCurrentUser().getUniversityLocation());
        fhLocation.setId("textfieldDestination");
        fhLocation.addValueChangeListener(event ->
                fhLocation.setUniversityLocationAddress(fhLocation.getValue()));

        dayOfWeek = new Select<>();
        dayOfWeek.setItems(DayOfWeek.getDayOfWeekList());
        dayOfWeek.setValue(DayOfWeek.MONDAY.label);
        dayOfWeek.setLabel("Wochentag");
        dayOfWeek.setPlaceholder("Wochentag auswählen");

        time = new TimePicker("Ankunftzeit");
        time.setId("timepicker");
        time.setStep(Duration.ofMinutes(15));

        Button buttonSearch = new Button("Fahrt suchen");
        buttonSearch.setId("buttonSearch");
        //TODO: Wochentag mit übergeben
        buttonSearch.addClickListener(searchEvent -> {
                    try {
                        //Todo: Ähm der kann doch weg oder? xD
                        AddressConverter converter = new AddressConverter(address.getValue());

                        if (fhLocation.getValue() != null && !address.getValue().equals("") && time.getValue()!= null) {

                            if(checkboxRegularDrive.getValue()){
                                date.setValue(LocalDate.now());
                            }
                            else{
                                dayOfWeek.setValue("");
                                if (date.getValue() == null){
                                    NotificationError.show("Bitte ein Datum auswählen");
                                }
                                localDateCheck(date.getValue());
                            }
                            UI.getCurrent().navigate(SearchDriveResultView.class,
                                    new RouteParameters(
                                            new RouteParam("fahrtentyp", radioDriveDirection.getValue()),
                                            new RouteParam("fhStandort", fhLocation.getValue()),
                                            new RouteParam("adresse", address.getPlace()),
                                            new RouteParam("datum", date.getValue().toString()),
                                            new RouteParam("uhrzeit", time.getValue().toString()),
                                            new RouteParam("regelmaessig", checkboxRegularDrive.getValue().toString()),
                                            new RouteParam("wochentag", dayOfWeek.getValue())
                                    ));
                        } else {
                            NotificationError.show("Bitte Start- und Zieladresse, sowie Datum/Zeit angeben.");
                        }
                    } catch (InvalidAddressException | InvalidDateException ex) {
                        ex.printStackTrace();
                        if (Objects.equals(ex.getClass().getSimpleName(), "InvalidAddressException")) {
                            NotificationError.show("Keine gültige Adresse.");
                        } else if (Objects.equals(ex.getClass().getSimpleName(), "InvalidDateException")) {
                            NotificationError.show("Das Datum darf nicht in der Vergangenheit liegen.");
                        } else
                            NotificationError.show("Unbekannter Fehler.");
                    }
                }
        );


        Label labelOwnDrive = new Label("Doch selber fahren?");
        labelOwnDrive.setId("labelOwnDrive");

        Button buttonOfferDrive = new Button("Fahrt anbieten");
        buttonOfferDrive.setId("buttonOfferDrive");
        buttonOfferDrive.addClickListener(e -> UI.getCurrent().navigate(OfferDriveView.class));

        FormLayout formSearchDrive = new FormLayout(title, radioDriveDirection, checkboxRegularDrive,
                address, date, fhLocation, time);
        formSearchDrive.setId("formSearchDrive");

        formSearchDrive.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formSearchDrive.setColspan(title, 2);


        FormLayout formOfferDriveQuestion = new FormLayout(labelOwnDrive, buttonOfferDrive);
        formOfferDriveQuestion.setId("formOfferDriveQuestion");

        add(formSearchDrive, buttonSearch, formOfferDriveQuestion);

        radioDriveDirection.addValueChangeListener(event -> setAddressFields(formSearchDrive, address,
                fhLocation, time, radioDriveDirection.getValue()));

        checkboxRegularDrive.addValueChangeListener(event -> setDateOrDayField(event.getValue(), formSearchDrive, date, dayOfWeek));
    }

    /**
     * Die Methode setAddressFields setzt die Adressfelder für die
     * Fahrtrichtung Von/Nach, je nach ausgewähltem RadioButton für
     * Hin- oder Rückfahrt.
     *
     * @param layout         FormLayout, welches bearbeitet werden soll
     * @param address        Adressfeld, dessen Position im Formular geändert
     *                       werden soll
     * @param fhLocation     Auswahlfeld für den FH-Standort, dessen Position
     *                       im Formular geändert werden soll
     * @param routeDirection Wert des ausgwählten RadioButtons für die Fahrtrichtung
     */
    private void setAddressFields(FormLayout layout, TextFieldAddress address,
                                  SelectUniversityLocation fhLocation, TimePicker time, String routeDirection) {

        nullCheck(layout, routeDirection, address, time, fhLocation);

        switch (routeDirection) {
            case "Hinfahrt" -> {
                layout.remove(address, fhLocation);
                layout.addComponentAtIndex(3, address);
                address.setLabel("Von");
                layout.addComponentAtIndex(5, fhLocation);
//                    fhLocation.setValue(null);
                time.setLabel("Ankunftszeit");
            }
            case "Rückfahrt" -> {
                layout.remove(address, fhLocation);
                layout.addComponentAtIndex(5, address);
                address.setLabel("Nach");
                layout.addComponentAtIndex(3, fhLocation);
//                    fhLocation.setValue(null);
                time.setLabel("Abfahrtszeit");
            }
            default -> {
            }

        }
    }

    private void setDateOrDayField(boolean isRegulaDrive, FormLayout layout, DatePicker date, Select<String> dayOfWeek) {

        nullCheck(layout, date, dayOfWeek);

        if(isRegulaDrive){
            layout.remove(date);
            layout.addComponentAtIndex(4, dayOfWeek);
        }
        else{
            layout.remove(dayOfWeek);
            layout.addComponentAtIndex(4, date);
        }
    }
}

