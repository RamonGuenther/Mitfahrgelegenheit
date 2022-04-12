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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;


/*
* FIXME:
*
* TODO:
*       - Sunderweg, Bielefeld, Deutschland Ohne PLZ!! wenn man keine Hausnummer angbit. können wir die API so einstellen das es unserem Format entspricht oder Validierung in dem Textfeld
*       - Krayer Str., Essen, Deutschland
*
* */


/**
 * Die Klasse OfferDriveView erstellt eine View für das Erstellen
 * eines Fahrtangebotes
 *
 * @author Ramon Günther
 */
@Route(value = "fahrtAnbieten", layout = MainLayout.class)
@PageTitle("Fahrt Anbieten")
@CssImport("/themes/mitfahrgelegenheit/views/offer-drive-view.css")
public class OfferDriveView extends VerticalLayout{

    private final DriveRouteService driveRouteService;

    private final UserService userService;

    private FormLayoutDriveRoute formlayoutTop;
    private FormLayoutDriveRoute formLayoutBottom;

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
                case "Hinfahrt" -> {
                    saveFormLayoutTop();
                    formlayoutTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                }
                case "Rückfahrt" -> {
                    saveFormLayoutBottom();
                    formLayoutBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);

                }
                case "Hin- & Rückfahrt" -> {
                    saveFormLayoutTop();
                    saveFormLayoutBottom();
                    formlayoutTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                    formLayoutBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                }
            }
        });
    }

    /**
     * In der Methode createOfferDriveView werden die Komponenten #
     * für das Layout, hinzugefügt.
     */
    private void createOfferDriveView() {
        Div div = new Div();
        div.setId("offer-drive-view-layout");

        H1 title = new H1("Fahrt anbieten");

        formlayoutTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
        formLayoutBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);

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
                    div.removeAll();
                    div.add(title, layoutOption, formlayoutTop, buttonLayout);
                }
                case "Rückfahrt" -> {
                    div.removeAll();
                    div.add(title, layoutOption, formLayoutBottom, buttonLayout);
                }
                case "Hin- & Rückfahrt" -> {
                    div.removeAll();
                    div.add(title, layoutOption, formlayoutTop, formLayoutBottom, buttonLayout);
                }
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        add(div);
    }

    private void saveFormLayoutTop() {
        saveDrive(formlayoutTop.getAddress(), formlayoutTop.getFhLocation(), formlayoutTop.getDriveTime(), formlayoutTop.getCheckboxFuelParticipation(),formlayoutTop.getCarSeatCount(), DriveType.OUTWARD_TRIP, formlayoutTop.getDriveDateStart());
    }

    private void saveFormLayoutBottom() {
        saveDrive(formLayoutBottom.getFhLocation(), formLayoutBottom.getAddress(), formLayoutBottom.getDriveTime(), formLayoutBottom.getCheckboxFuelParticipation(),formLayoutBottom.getCarSeatCount(), DriveType.RETURN_TRIP,formLayoutBottom.getDriveDateStart());
    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Boolean fuelParticipation, Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try{
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            Start start = new Start(firstAddress);
            Destination destination = new Destination(secondAddress);

            RouteString routeString = new RouteString(start, destination, Collections.emptyList());
            driveRouteService.save(new DriveRoute(
                    start,
                    destination,
                    driveTime.atDate(driveDate),
                    fuelParticipation,
                    carSeatCount,
                    user,
                    LocalDateTime.now(),
                    fahrtenTyp,
                    routeString.getRoute()
            ));

            NotificationSuccess.show("Fahrt wurde erstellt!");
        }
        catch(Exception e){
            NotificationError.show(e.getMessage());
        }
    }

}
