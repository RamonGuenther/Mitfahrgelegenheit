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
import com.vaadin.flow.router.Route;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Adresse;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Ziel;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.FahrerRouteService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationError;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications.NotificationSuccess;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout.MainLayout;

import java.time.LocalDate;
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
@Route(value = "fahrtAnbieten", layout = MainLayout.class)
@PageTitle("Fahrt Anbieten")
@CssImport("/themes/mitfahrgelegenheit/views/offer-drive-view.css")
public class OfferDriveView extends VerticalLayout{

    private final FahrerRouteService fahrerRouteService;

    private FormLayoutBottomOfferDrive formLayoutBottom;

    private FormLayoutTopOfferDrive formLayoutTop;

    private RadioButtonGroup<String> layoutOption;

    private Button createButton;

    /**
     * Der Konstruktor ist für das Erstellen der View und den zugehörigen
     * Listener zuständig.
     */
    public OfferDriveView(FahrerRouteService fahrerRouteService) {
        this.fahrerRouteService = fahrerRouteService;

        setId("searchDriveResultView");
        createOfferDriveView();
        layoutOption.setValue("Hinfahrt");

        createButton.addClickListener(e -> {

            switch (layoutOption.getValue()) {
                case "Hinfahrt" -> {
//                    saveFormLayoutTop();
                    formLayoutTop.clearFields();
                }
                case "Rückfahrt" -> {
//                    saveFormLayoutBottom();
                    formLayoutBottom.clearFields();
                }
                case "Hin- & Rückfahrt" -> {
//                    saveFormLayoutTop();
//                    saveFormLayoutBottom();
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

//    private void saveFormLayoutTop() {
//        saveDrive(formLayoutTop.getAddress(), formLayoutTop.getFhLocation(), formLayoutTop.getDriveTime(), formLayoutTop.getCarSeatCount(), FahrtenTyp.HINFAHRT, formLayoutTop.getDriveDateStart());
//    }
//
//    private void saveFormLayoutBottom() {
//        saveDrive(formLayoutBottom.getFhLocation(), formLayoutBottom.getAddress(), formLayoutBottom.getDriveTime(), formLayoutBottom.getCarSeatCount(), FahrtenTyp.RUECKFAHRT,formLayoutBottom.getDriveDateStart());
//    }

//    private void saveDrive(String address, String fhLocation, LocalTime driveTime, Integer carSeatCount, FahrtenTyp fahrtenTyp, LocalDate driveDate) {
//        try{
//            AddressConverter converter = new AddressConverter(address);
//            Adresse firstAddress = new Adresse(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());
//
//            converter = new AddressConverter(fhLocation);
//            Adresse secondAddress = new Adresse(converter.getPostalCode(), converter.getPlace(), converter.getStreet(), converter.getNumber());
//
//            Result<Void, CreateFahrerRouteError> result = interactor.createFahrerRoute(new CreateFahrerRouteInput(
//                    new Start(firstAddress, driveTime.atDate(driveDate)),
//                    new Ziel(secondAddress, driveTime.atDate(driveDate)),
//                    carSeatCount, fahrtenTyp));
//
//            result.either(ignored -> System.out.println("ok"), this::handleError);
//
//            result.getFailure().ifPresent(this::handleError);
//
//            if (result.isFailure()) {
//                System.out.println("nicht ok");
//            }
//
//            NotificationSuccess.show("Fahrt wurde erstellt!");
//        }
//        catch(Exception e){
//            NotificationError.show(e.getMessage());
//        }
//    }
//
//    void handleError(CreateFahrerRouteError error) {
//
//    }

}
