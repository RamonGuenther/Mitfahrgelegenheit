package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


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
@com.vaadin.flow.router.Route(value = "fahrtAnbieten", layout = MainLayout.class)
@PageTitle("Fahrt Anbieten")
@CssImport("/themes/mitfahrgelegenheit/views/offer-drive-view.css")
public class OfferDriveView extends VerticalLayout{

    private final DriveRouteService driveRouteService;

    private final UserService userService;

    private FormLayoutBottomOfferDrive formLayoutBottom;

    private FormLayoutTopOfferDrive formLayoutTop;

    private RadioButtonGroup<String> layoutOption;

    private Button createButton;

    /**
     * Der Konstruktor ist für das Erstellen der View und den zugehörigen
     * Listener zuständig.
     */
    public OfferDriveView(DriveRouteService driveRouteService, UserService userService) {
        this.driveRouteService = driveRouteService;
        this.userService = userService;

        setId("searchDriveResultView");
        createOfferDriveView();
        layoutOption.setValue("Hinfahrt");

        createButton.addClickListener(e -> {

            switch (layoutOption.getValue()) {
                case "Hinfahrt" -> {
                    saveFormLayoutTop();
                    formLayoutTop.clearFields();
                }
                case "Rückfahrt" -> {
                    saveFormLayoutBottom();
                    formLayoutBottom.clearFields();
                }
                case "Hin- & Rückfahrt" -> {
                    saveFormLayoutTop();
                    saveFormLayoutBottom();
                    formLayoutTop.clearFields();
                    formLayoutBottom.clearFields();
                }
            }
            //UI.getCurrent().getPage().reload();
        });
    }

    /**
     * In der Methode createOfferDriveView werden die Komponenten #
     * für das Layout, hinzugefügt.
     */
    private void createOfferDriveView() {
        Div div = new Div();
        div.setId("containerOfferDrive");

        H1 title = new H1("Fahrt anbieten");

        formLayoutBottom = new FormLayoutBottomOfferDrive();
        formLayoutTop = new FormLayoutTopOfferDrive();

        layoutOption = new RadioButtonGroup<>();
        layoutOption.setItems("Hinfahrt", "Rückfahrt", "Hin- & Rückfahrt");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("buttonLayoutOfferDrive");
        createButton = new Button("Fahrt erstellen");
        createButton.setId("createButtonOfferDrive");
        createButton.setClassName("operationButtonOfferDrive");
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setId("cancelButtonOfferDrive");
        cancelButton.setClassName("operationButtonOfferDrive");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonLayout.add(createButton, cancelButton);

        layoutOption.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt" -> {
                    formLayoutTop = new FormLayoutTopOfferDrive();
                    div.removeAll();
                    div.add(title, layoutOption, formLayoutTop, buttonLayout);
                }
                case "Rückfahrt" -> {
                    formLayoutBottom = new FormLayoutBottomOfferDrive();
                    div.removeAll();
                    div.add(title, layoutOption, formLayoutBottom, buttonLayout);
                }
                case "Hin- & Rückfahrt" -> {
                    formLayoutTop = new FormLayoutTopOfferDrive();
                    formLayoutBottom = new FormLayoutBottomOfferDrive();
                    div.removeAll();
                    div.add(title, layoutOption, formLayoutTop, formLayoutBottom, buttonLayout);
                }
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        add(div);
    }

    private void saveFormLayoutTop() {
        saveDrive(formLayoutTop.getAddress(), formLayoutTop.getFhLocation(), formLayoutTop.getDriveTime(), formLayoutTop.getCarSeatCount(), DriveType.OUTWARD_TRIP, formLayoutTop.getDriveDateStart());
    }

    private void saveFormLayoutBottom() {
        saveDrive(formLayoutBottom.getFhLocation(), formLayoutBottom.getAddress(), formLayoutBottom.getDriveTime(), formLayoutBottom.getCarSeatCount(), DriveType.RETURN_TRIP,formLayoutBottom.getDriveDateStart());
    }

    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Integer carSeatCount, DriveType fahrtenTyp, LocalDate driveDate) {
        try{
            AddressConverter converter = new AddressConverter(address);
            Address firstAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            converter = new AddressConverter(fhLocation);
            Address secondAddress = new Address(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());

            User user = userService.findBenutzerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            driveRouteService.save(new DriveRoute(
                    new Start(firstAddress, driveTime.atDate(driveDate)),
                    new Destination(secondAddress, driveTime.atDate(driveDate)),
                    carSeatCount, user, LocalDateTime.now(),fahrtenTyp
            ));


            NotificationSuccess.show("Fahrt wurde erstellt!");
        }
        catch(Exception e){
            NotificationError.show(e.getMessage());
        }
    }

}
