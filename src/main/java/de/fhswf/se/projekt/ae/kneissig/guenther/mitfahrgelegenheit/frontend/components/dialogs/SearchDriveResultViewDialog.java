package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.StarsRating;

/**
 * TODO:  - Für Merkliste könnte man den Dialog auch missbrauchen nur mit anderen buttons unten!
 */
@CssImport("/themes/mitfahrgelegenheit/components/search-drive-result-view-dialog.css")
public class SearchDriveResultViewDialog extends Dialog {

    private final Icon PROFILE_ICON = new Icon(VaadinIcon.USER);

    private DriveRoute driveRoute;
    private FormLayoutTopOfferDrive formLayoutHinfahrt;
    private FormLayoutBottomOfferDrive formLayoutRueckfahrt;
    private VerticalLayout verticalLayout;

    public SearchDriveResultViewDialog(DriveRoute driveRoute){

//        this.fahrerRoute = fahrerRoute;

        verticalLayout = new VerticalLayout();

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Button closeButton = new Button();
        closeButton.setId("search-drive-result-view-close_button");
        closeButton.setIcon(icon);
        closeButton.addClickListener(e -> close());
        verticalLayout.add(closeButton);

        StarsRating driverRating = new StarsRating(0);
        driverRating.setId("search-drive-result-view-driver_rating");
        driverRating.setManual(true);
        add(driverRating);

        switch (driveRoute.getDriveType()) {
            case OUTWARD_TRIP -> {
                formLayoutHinfahrt = new FormLayoutTopOfferDrive();

                formLayoutHinfahrt.getFlexButton().setVisible(true);
                formLayoutHinfahrt.getFlexButton().setId("search-drive-result-view-dialog-profile_button");
                formLayoutHinfahrt.getFlexButton().setText("Profil");
                formLayoutHinfahrt.getFlexButton().setIcon(PROFILE_ICON);

                formLayoutHinfahrt.getTitleLayout().add(driverRating);
                formLayoutHinfahrt.setColspan(formLayoutHinfahrt.getTitleLayout(), 2);

                formLayoutHinfahrt.getFlexButton().addClickListener(e -> {
                    //Hier gehts zum Profil
                });

                formLayoutHinfahrt.setReadOnly(true);
                formLayoutHinfahrt.setTitle("Hinfahrt von " + driveRoute.getBenutzer().getUsername());
                formLayoutHinfahrt.setSitzplaetze(driveRoute.getSeatCount().toString());
                formLayoutHinfahrt.setFhLocation(driveRoute.getZiel().getAdresse().getPlace());
                formLayoutHinfahrt.setDriveTime(driveRoute.getZiel().getTime().toLocalTime());
                formLayoutHinfahrt.setDriveDateStart(driveRoute.getZiel().getTime().toLocalDate());
                formLayoutHinfahrt.setAddress(driveRoute.getStart().getAdresse().getStreet() + " "
                        + driveRoute.getStart().getAdresse().getHouseNumber() + ", "
                        + driveRoute.getStart().getAdresse().getPostal() + " "
                        + driveRoute.getStart().getAdresse().getPlace() + ", "
                        + "Deutschland");
                verticalLayout.add(formLayoutHinfahrt);
            }
            case RETURN_TRIP -> {
                formLayoutRueckfahrt = new FormLayoutBottomOfferDrive();

                formLayoutRueckfahrt.getFlexButton().setVisible(true);
                formLayoutRueckfahrt.getFlexButton().setId("search-drive-result-view-dialog-profile_button");
                formLayoutRueckfahrt.getFlexButton().setText("Profil");
                formLayoutRueckfahrt.getFlexButton().setIcon(PROFILE_ICON);

                formLayoutRueckfahrt.getTitleLayout().add(driverRating);
                formLayoutRueckfahrt.setColspan(formLayoutRueckfahrt.getTitleLayout(), 2);

                formLayoutRueckfahrt.getFlexButton().addClickListener(e -> {
                    //Hier gehts zum Profil
                });


                formLayoutRueckfahrt.setReadOnly(true);
                formLayoutRueckfahrt.setTitle("Rückfahrt von " + driveRoute.getBenutzer().getUsername());
                formLayoutRueckfahrt.setSitzplaetze(driveRoute.getSeatCount().toString());
                formLayoutRueckfahrt.setFhLocation(driveRoute.getStart().getAdresse().getPlace());
                formLayoutRueckfahrt.setDriveTime(driveRoute.getStart().getTime().toLocalTime());
                formLayoutRueckfahrt.setDriveDateStart(driveRoute.getStart().getTime().toLocalDate());
                formLayoutRueckfahrt.setAddress(driveRoute.getZiel().getAdresse().getStreet() + " "
                        + driveRoute.getZiel().getAdresse().getHouseNumber() + ", "
                        + driveRoute.getZiel().getAdresse().getPostal() + " "
                        + driveRoute.getZiel().getAdresse().getPlace() + ", "
                        + "Deutschland");
                verticalLayout.add(formLayoutRueckfahrt);
            }
        }
        verticalLayout.add(createButtons());
        add(verticalLayout);
        open();
    }

    /**
     * In der Methode createButtons werden die Buttons zum Speichern und Abbrechen
     * für den Dialog erstellt.
     */
    private HorizontalLayout createButtons() {

//        Button bookmarkButton = new Button("Fahrt merken");
//        bookmarkButton.setId("search-drive-result-view-dialog-bookmark_button");
//        bookmarkButton.setClassName("search-drive-result-view-dialog-buttons");
//        bookmarkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button requestButton = new Button("Fahrt anfragen");
        requestButton.setId("search-drive-result-view-dialog-request_button");
        requestButton.setClassName("search-drive-result-view-dialog-buttons");
        requestButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("search-drive-result-view-dialog-button_layout");
        buttonLayout.add(requestButton);

        return buttonLayout;
    }

}
