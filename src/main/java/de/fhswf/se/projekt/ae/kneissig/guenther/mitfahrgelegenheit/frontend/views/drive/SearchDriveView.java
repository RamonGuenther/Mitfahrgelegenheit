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

import static de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.ValidationUtility.*;

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

    private final Checkbox checkboxRegularDrive;
    private final RadioButtonGroup<String> radioDriveDirection;
    private final TextFieldAddress address;
    private final DatePicker date;
    private final SelectUniversityLocation fhLocation;
    private final TimePicker time;
    private final Select<String> dayOfWeek;
    private AddressConverter addressConverter;

    /**
     * Der Konstruktor ist für das Erstellen der View zum Suchen
     * einer Mitfahrgelegenheit zuständig.
     */

    public SearchDriveView(UserService userService) {

        H1 title = new H1("Mitfahrgelegenheit suchen");

        checkboxRegularDrive = new Checkbox("Regelmäßige Fahrten");

        radioDriveDirection = new RadioButtonGroup<>();
        radioDriveDirection.setItems("Hinfahrt", "Rückfahrt");
        radioDriveDirection.setValue("Hinfahrt");

        address = new TextFieldAddress("Von");
        address.setValue(userService.getCurrentUser().getAddress().toString());
        address.setErrorMessage("Adresse bitte eintragen");
        address.setRequiredIndicatorVisible(true);

        fhLocation = new SelectUniversityLocation();
        fhLocation.setValue(userService.getCurrentUser().getUniversityLocation());
        fhLocation.addValueChangeListener(event -> fhLocation.setUniversityLocationAddress(fhLocation.getValue()));

        date = new DatePicker("Datum");
        date.setErrorMessage("Datum bitte angeben");
        date.setMin(LocalDate.now());
        date.setRequiredIndicatorVisible(true);

        time = new TimePicker("Ankunftzeit");
        time.setStep(Duration.ofMinutes(15));
        time.setErrorMessage("Ankunft/Abfahrtszeit bitte angeben");
        time.setRequiredIndicatorVisible(true);

        dayOfWeek = new Select<>();
        dayOfWeek.setItems(DayOfWeek.getDayOfWeekList());
        dayOfWeek.setValue(DayOfWeek.MONDAY.label);
        dayOfWeek.setLabel("Wochentag");
        dayOfWeek.setPlaceholder("Wochentag auswählen");

        Button searchButton = new Button("Fahrt suchen");
        searchButton.setId("search-drive-view-search_button");

        searchButton.addClickListener(searchEvent -> {
                    try {

                        if (checkInputFields()) {
                            NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                            return;
                        }

                        UI.getCurrent().navigate(SearchDriveResultView.class,
                                new RouteParameters(
                                        new RouteParam("fahrtentyp", radioDriveDirection.getValue()),
                                        new RouteParam("fhStandort", fhLocation.getValue()),
                                        new RouteParam("adresse", addressConverter.getPlace()),
                                        new RouteParam("datum", date.isEmpty() || checkboxRegularDrive.getValue() ? "" : date.getValue().toString()),
                                        new RouteParam("uhrzeit", time.getValue().toString()),
                                        new RouteParam("regelmaessig", checkboxRegularDrive.getValue().toString()),
                                        new RouteParam("wochentag", checkboxRegularDrive.getValue() ? dayOfWeek.getValue() : "")
                                ));

                    } catch (InvalidAddressException | InvalidDateException ex) {
                        NotificationError.show(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
        );

        Label labelOwnDrive = new Label("Doch selber fahren?");

        Button buttonOfferDrive = new Button("Fahrt anbieten");
        buttonOfferDrive.addClickListener(e -> UI.getCurrent().navigate(OfferDriveView.class));

        FormLayout formSearchDrive = new FormLayout(title, radioDriveDirection, checkboxRegularDrive,
                address, date, fhLocation, time);
        formSearchDrive.setId("search-drive-view-form_layout");

        formSearchDrive.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formSearchDrive.setColspan(title, 2);


        FormLayout formOfferDriveQuestion = new FormLayout(labelOwnDrive, buttonOfferDrive);
        formOfferDriveQuestion.setId("search-drive-view-form_offer_drive_question");

        add(formSearchDrive, searchButton, formOfferDriveQuestion);

        radioDriveDirection.addValueChangeListener(event -> setAddressFields(formSearchDrive, address,
                fhLocation, time, radioDriveDirection.getValue()));

        checkboxRegularDrive.addValueChangeListener(event -> setDateOrDayField(event.getValue(), formSearchDrive, date, dayOfWeek));
    }

    private boolean checkInputFields() throws InvalidDateException, InvalidAddressException {
        setInputFieldsInvalid();
        if (checkboxRegularDrive.getValue())
            return address.getValue().isEmpty() || time.isEmpty();
        else
            return address.getValue().isEmpty() || date.isEmpty() || time.isEmpty();
    }

    private void setInputFieldsInvalid() throws InvalidDateException, InvalidAddressException {
        if (address.getValue().isEmpty()) {
            address.setInvalid(true);
        } else {
            addressConverter = new AddressConverter(address.getValue());
        }
        if (date.isEmpty()) {
            date.setInvalid(true);
        } else {
            localDateCheck(date.getValue());
        }
        if (time.isEmpty()) {
            time.setInvalid(true);
        }
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

        if (isRegulaDrive) {
            layout.remove(date);
            layout.addComponentAtIndex(4, dayOfWeek);
        } else {
            layout.remove(dayOfWeek);
            layout.addComponentAtIndex(4, date);
        }
    }
}

