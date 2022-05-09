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
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.PageId;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
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
    private FormLayoutDriveRoute formLayoutOutwardTrip;
    private FormLayoutDriveRoute formLayoutReturnTrip;
    private RadioButtonGroup<String> layoutOption;
    private Button saveButton;
    private HorizontalLayout buttonLayout;
    private Div layout;

    /**
     * Der Konstruktor ist für das Erstellen der View zuständig.
     */
    public OfferDriveView(DriveRouteService driveRouteService, UserService userService) {
        UI.getCurrent().setId(PageId.OFFER_DRIVE_VIEW.label);

        this.driveRouteService = driveRouteService;
        this.userService = userService;

        createOfferDriveView();

        layoutOption.setValue("Hinfahrt");

        saveButton.addClickListener(e -> {

            switch (layoutOption.getValue()) {
                case "Hinfahrt":
                    if(checkOutwardTrip()){
                        saveOutwardTrip();
                        clearOutwardTrip();
                    }
                    break;
                case "Rückfahrt":
                    if(checkReturnTrip()) {
                        saveReturnTrip();
                        clearReturnTrip();
                    }
                    break;
                case "Hin- & Rückfahrt":
                    if(checkOutwardTrip() && checkReturnTrip()){
                        saveOutwardTrip();
                        saveReturnTrip();
                        clearOutwardTrip();
                        clearReturnTrip();
                    }
                    break;
            }
        });
    }

    /**
     * In der Methode createOfferDriveView werden die Komponenten
     * für das Layout, hinzugefügt.
     */
    private void createOfferDriveView(){
        User currentUser = userService.getCurrentUser();

        layout = new Div();
        layout.setId("offer-drive-view-layout");

        H1 title = new H1("Fahrt anbieten");

        formLayoutOutwardTrip = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
        formLayoutReturnTrip = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);

        layoutOption = new RadioButtonGroup<>();
        layoutOption.setItems("Hinfahrt", "Rückfahrt", "Hin- & Rückfahrt");

        buttonLayout = new HorizontalLayout();
        buttonLayout.setId("offer-drive-view-button_layout");
        saveButton = new Button("Fahrt erstellen");
        saveButton.setId("offer-drive-view-create_button");
        saveButton.setClassName("offer-drive-view-buttons");
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setId("offer-drive-view-cancel_button");
        cancelButton.setClassName("offer-drive-view-buttons");

        buttonLayout.add(saveButton, cancelButton);

        layoutOption.addValueChangeListener(e -> {
            switch (e.getValue()) {
                case "Hinfahrt":
                    formLayoutOutwardTrip = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                    formLayoutOutwardTrip.getDriveDateStart().setMin(LocalDate.now());
                    formLayoutOutwardTrip.setFhLocation(currentUser.getUniversityLocation());
                    formLayoutOutwardTrip.setAddress(currentUser.getAddress().toString());
                    layout.removeAll();
                    layout.add(title, layoutOption, formLayoutOutwardTrip, buttonLayout);
                    break;
                case "Rückfahrt":
                    formLayoutReturnTrip = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                    formLayoutReturnTrip.getDriveDateStart().setMin(LocalDate.now());
                    formLayoutReturnTrip.setFhLocation(currentUser.getUniversityLocation());
                    formLayoutReturnTrip.setAddress(currentUser.getAddress().toString());
                    layout.removeAll();
                    layout.add(title, layoutOption, formLayoutReturnTrip, buttonLayout);
                    break;
                case "Hin- & Rückfahrt":
                    formLayoutOutwardTrip = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                    formLayoutOutwardTrip.setFhLocation(currentUser.getUniversityLocation());
                    formLayoutOutwardTrip.getDriveDateStart().setMin(LocalDate.now());
                    formLayoutOutwardTrip.setAddress(currentUser.getAddress().toString());
                    formLayoutReturnTrip = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                    formLayoutReturnTrip.setFhLocation(currentUser.getUniversityLocation());
                    formLayoutReturnTrip.getDriveDateStart().setMin(LocalDate.now());
                    formLayoutReturnTrip.setAddress(currentUser.getAddress().toString());
                    layout.removeAll();
                    layout.add(title, layoutOption, formLayoutOutwardTrip, formLayoutReturnTrip, buttonLayout);
                    break;
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        add(layout);
    }

    /**
     * Die Methode checkOutwardTrip wird für Hinfahrten verwendet und ruft
     * die Methode checkFormLayout auf mit dem Formular einer Hinfahrt.
     */
    private boolean checkOutwardTrip() {
        return checkFormLayout(formLayoutOutwardTrip);
    }

    /**
     * Die Methode checkReturnTrip wird für Rückfahrten verwendet und ruft
     * die Methode checkFormLayout auf mit dem Formular einer Rückfahrt.
     */
    private boolean checkReturnTrip() {
        return checkFormLayout(formLayoutReturnTrip);
    }

    /**
     * Die Methode checkFormLayout überprüft die Eingabefelder auf ihre Gültigkeit und gibt
     * true zurück, wenn alle Eingaben vollständig und gültig sind.
     *
     * @param formLayoutDriveRoute Hinfahrt- oder Rückfahrt-Formular
     * @return true wenn alle Daten korrekt sind
     */
    private boolean checkFormLayout(FormLayoutDriveRoute formLayoutDriveRoute) {
        try {
            if (formLayoutDriveRoute.checkInputFields()) {
                NotificationError.show("Bitte alle Eingabefelder ausfüllen.");
                return false;
            }
            return true;

        } catch (InvalidAddressException ex) {
            formLayoutDriveRoute.getAddress().setErrorMessage(ex.getMessage());
            formLayoutDriveRoute.getAddress().setInvalid(true);
            ex.printStackTrace();
        } catch (InvalidRegularDrivePeriod ex) {
            formLayoutDriveRoute.getDriveDateStart().setInvalid(true);
            formLayoutDriveRoute.getDriveDateStart().setErrorMessage(ex.getMessage());
            formLayoutDriveRoute.getDriveDateEnd().setInvalid(true);
            formLayoutDriveRoute.getDriveDateEnd().setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        } catch (InvalidDateException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * Die Methode saveOutwardTrip speichert eine Hinfahrt.
     */
    private void saveOutwardTrip(){
        saveDrive(formLayoutOutwardTrip.getAddressValue(), formLayoutOutwardTrip.getFhLocation(),
                formLayoutOutwardTrip.getDriveTime(), formLayoutOutwardTrip.getCheckboxFuelParticipation(),
                formLayoutOutwardTrip.getCarSeatCount(), DriveType.OUTWARD_TRIP,
                formLayoutOutwardTrip.getDriveDateStartValue());
    }

    /**
     * Die Methode saveReturnTrip speichert eine Rückfahrt.
     */
    private void saveReturnTrip(){
        saveDrive(formLayoutReturnTrip.getFhLocation(), formLayoutReturnTrip.getAddressValue(),
                formLayoutReturnTrip.getDriveTime(), formLayoutReturnTrip.getCheckboxFuelParticipation(),
                formLayoutReturnTrip.getCarSeatCount(), DriveType.RETURN_TRIP,
                formLayoutReturnTrip.getDriveDateStartValue());
    }

    /**
     * Löscht alle Eingaben auf dem Hinfahrt-Formular und deaktiviert
     * den ungültigen Status der einzelnen Felder.
     */
    private void clearOutwardTrip(){
        formLayoutOutwardTrip.clearFields();
        formLayoutOutwardTrip.setInvalid(false);
    }

    /**
     * Löscht alle Eingaben auf dem Rückfahrt-Formular und deaktiviert
     * den ungültigen Status der einzelnen Felder.
     */
    private void clearReturnTrip(){
        formLayoutReturnTrip.clearFields();
        formLayoutReturnTrip.setInvalid(false);
    }

    /**
     * Die Methode saveDrive speichert das Fahrtangebot des Nutzers.
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
                if (formLayoutOutwardTrip.getCheckboxRegularDriveValue()) {
                    newDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formLayoutOutwardTrip.getDriveDays()),
                            formLayoutOutwardTrip.getDriveDateStartValue(),
                            formLayoutOutwardTrip.getDriveDateEndValue())
                    );
                }
            } else {
                if (formLayoutReturnTrip.getCheckboxRegularDriveValue()) {
                    newDriveRoute.setRegularDrive(new RegularDrive(
                            DayOfWeek.getDayOfWeek(formLayoutReturnTrip.getDriveDays()),
                            formLayoutReturnTrip.getDriveDateStartValue(),
                            formLayoutReturnTrip.getDriveDateEndValue())
                    );
                }
            }
            driveRouteService.save(newDriveRoute);
            NotificationSuccess.show("Das Fahrtangebot wurde erstellt.");

        } catch (Exception e) {
            NotificationError.show(e.getMessage());
            e.printStackTrace();
        }
    }
}
