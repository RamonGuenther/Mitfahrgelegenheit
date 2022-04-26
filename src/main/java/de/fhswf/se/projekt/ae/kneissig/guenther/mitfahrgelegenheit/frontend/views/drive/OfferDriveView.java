package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.RegularDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DayOfWeek;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidDateException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidRegularDrivePeriod;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

/**
 * Die Klasse OfferDriveView erstellt eine View für das Erstellen
 * eines Fahrtangebotes. Der Benutzer kann eine Hin- und/ oder eine
 * Rückfahrt zu einem FH-Standort der Fachhochschule Südwestfalen
 * erstellen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Route(value = "fahrtAnbieten", layout = MainLayout.class)
@PageTitle("Fahrt Anbieten")
@CssImport("/themes/mitfahrgelegenheit/views/offer-drive-view.css")
public class OfferDriveView extends VerticalLayout {

    private final DriveRouteService driveRouteService;
    private final UserService userService;

    private FormLayoutDriveRoute formlayoutDriveRouteTop;
    private FormLayoutDriveRoute formLayoutDriveRouteBottom;

    private RadioButtonGroup<String> layoutOption;
    private Button createButton;

    /**
     * Der Konstruktor ist für das Erstellen der View und den zugehörigen
     * Listener zuständig.
     */
    public OfferDriveView(DriveRouteService driveRouteService, UserService userService) {

        this.driveRouteService = driveRouteService;
        this.userService = userService;

        createOfferDriveView();

        layoutOption.setValue("Hinfahrt");

        createButton.addClickListener(e -> {

            switch (layoutOption.getValue()) {
                case "Hinfahrt" -> saveFormLayoutTop();
                case "Rückfahrt" -> saveFormLayoutBottom();
                case "Hin- & Rückfahrt" -> {
                    saveFormLayoutTop();
                    saveFormLayoutBottom();
                }
            }
        });
    }

    /**
     * In der Methode createOfferDriveView werden die Komponenten #
     * für das Layout, hinzugefügt.
     */
    private void createOfferDriveView() {
        User user = userService.getCurrentUser();

        Div div = new Div();
        div.setId("offer-drive-view-layout");

        H1 title = new H1("Fahrt anbieten");

        formlayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
        formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);

        layoutOption = new RadioButtonGroup<>();
        layoutOption.setItems("Hinfahrt", "Rückfahrt", "Hin- & Rückfahrt");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("offer-drive-view-button_layout");
        createButton = new Button("Fahrt erstellen");
        createButton.setId("offer-drive-view-create_button");
        createButton.setClassName("offer-drive-view-buttons");
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setId("offer-drive-view-cancel_button");
        cancelButton.setClassName("offer-drive-view-buttons");

        buttonLayout.add(createButton, cancelButton);

        layoutOption.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt" -> {
                    formlayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                    formlayoutDriveRouteTop.setFhLocation(user.getUniversityLocation());
                    formlayoutDriveRouteTop.setAddress(user.getAddress().toString());
                    div.removeAll();
                    div.add(title, layoutOption, formlayoutDriveRouteTop, buttonLayout);
                }
                case "Rückfahrt" -> {
                    formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                    formLayoutDriveRouteBottom.setFhLocation(user.getUniversityLocation());
                    formLayoutDriveRouteBottom.setAddress(user.getAddress().toString());
                    div.removeAll();
                    div.add(title, layoutOption, formLayoutDriveRouteBottom, buttonLayout);
                }
                case "Hin- & Rückfahrt" -> {
                    formlayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                    formlayoutDriveRouteTop.setFhLocation(user.getUniversityLocation());
                    formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                    formLayoutDriveRouteBottom.setFhLocation(user.getUniversityLocation());
                    div.removeAll();
                    div.add(title, layoutOption, formlayoutDriveRouteTop, formLayoutDriveRouteBottom, buttonLayout);
                }
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        add(div);
    }

    /**
     * Die Methode saveFormLayoutTop speichert eine vom Benutzer erstellte Hinfahrt.
     * Sie wird in der Methode saveDrive verwendet.
     */
    private void saveFormLayoutTop() {
        try {
            if (formlayoutDriveRouteTop.checkInputFields()) {
                NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                return;
            }

            saveDrive(formlayoutDriveRouteTop.getAddressValue(), formlayoutDriveRouteTop.getFhLocation(),
                    formlayoutDriveRouteTop.getDriveTime(), formlayoutDriveRouteTop.getCheckboxFuelParticipation(),
                    formlayoutDriveRouteTop.getCarSeatCount(), DriveType.OUTWARD_TRIP,
                    formlayoutDriveRouteTop.getDriveDateStart());

            formlayoutDriveRouteTop.clearFields();
        } catch (InvalidDateException | InvalidRegularDrivePeriod ex) {
            NotificationError.show(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Die Methode saveFormLayoutBottom speichert eine vom Benutzer erstellte Rückfahrt.
     * Sie wird in der Methode saveDrive verwendet.
     */
    private void saveFormLayoutBottom() {
        try {
            if (formLayoutDriveRouteBottom.checkInputFields()) {
                NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                return;
            }

            saveDrive(formLayoutDriveRouteBottom.getFhLocation(), formLayoutDriveRouteBottom.getAddressValue(),
                    formLayoutDriveRouteBottom.getDriveTime(), formLayoutDriveRouteBottom.getCheckboxFuelParticipation(),
                    formLayoutDriveRouteBottom.getCarSeatCount(), DriveType.RETURN_TRIP,
                    formLayoutDriveRouteBottom.getDriveDateStart());

            formLayoutDriveRouteBottom.clearFields();
        } catch (InvalidDateException | InvalidRegularDrivePeriod ex) {
            NotificationError.show(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Die Methode saveDrive speichert die Fahrt, die der Benutzer erstellen möchte. Dabei wird zwischen Hin-
     * und Rückfahrten unterschieden. Durch die unterschiedlichen FormLayouts und save-Methoden, ist es für
     * den Benutzer auch möglich, gleichzeitig eine Hin- und Rückfahrt zu erstellen.
     */
    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Boolean fuelParticipation,
                           Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try {
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            Start start = new Start(firstAddress);
            Destination destination = new Destination(secondAddress);

            RouteString routeString = new RouteString(start, destination, Collections.emptyList());

            DriveRoute newDriveRoute = new DriveRoute(
                    start,
                    destination,
                    driveTime.atDate(driveDate),
                    fuelParticipation,
                    carSeatCount,
                    user,
                    fahrtenTyp,
                    routeString.getRoute()
            );

            if (fahrtenTyp.equals(DriveType.OUTWARD_TRIP)) {
                if (formlayoutDriveRouteTop.getCheckboxRegularDriveValue()) {
                    newDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formlayoutDriveRouteTop.getDriveDays()),
                            formlayoutDriveRouteTop.getDriveDateStart(),
                            formlayoutDriveRouteTop.getDriveDateEnd())
                    );
                }
            } else {
                if (formLayoutDriveRouteBottom.getCheckboxRegularDriveValue()) {
                    newDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formLayoutDriveRouteBottom.getDriveDays()),
                            formLayoutDriveRouteBottom.getDriveDateStart(),
                            formLayoutDriveRouteBottom.getDriveDateEnd())
                    );
                }
            }
            driveRouteService.save(newDriveRoute);
            NotificationSuccess.show("Das Fahrtangebot wurde erstellt.");

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
        }
    }
}
