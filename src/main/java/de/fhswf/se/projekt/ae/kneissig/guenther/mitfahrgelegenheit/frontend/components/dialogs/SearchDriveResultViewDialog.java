package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.FahrerRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutBottomOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutTopOfferDrive;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ratings.StarsRating;

/**
 * TODO:  - Für Merkliste könnte man den Dialog auch missbrauchen nur mit anderen buttons unten!
 */
@CssImport("/themes/mitfahrgelegenheit/components/search-drive-result-view-dialog.css")
public class SearchDriveResultViewDialog extends Dialog {

    private final Icon PROFILE_ICON = new Icon(VaadinIcon.USER);

    private FahrerRoute fahrerRoute;
    private FormLayoutTopOfferDrive formLayoutHinfahrt;
    private FormLayoutBottomOfferDrive formLayoutRueckfahrt;
    private VerticalLayout verticalLayout;

    public SearchDriveResultViewDialog(FahrerRoute fahrerRoute){

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

        switch (fahrerRoute.getFahrtenTyp()) {
            case HINFAHRT -> {
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
                formLayoutHinfahrt.setTitle("Hinfahrt von " + fahrerRoute.getBenutzer().getUsername());
                formLayoutHinfahrt.setSitzplaetze(fahrerRoute.getSitzplaetze().toString());
                formLayoutHinfahrt.setFhLocation(fahrerRoute.getZiel().getAdresse().getOrt());
                formLayoutHinfahrt.setDriveTime(fahrerRoute.getZiel().getZeit().toLocalTime());
                formLayoutHinfahrt.setDriveDateStart(fahrerRoute.getZiel().getZeit().toLocalDate());
                formLayoutHinfahrt.setAddress(fahrerRoute.getStart().getAdresse().getStrasse() + " "
                        + fahrerRoute.getStart().getAdresse().getHausnummer() + ", "
                        + fahrerRoute.getStart().getAdresse().getPlz() + " "
                        + fahrerRoute.getStart().getAdresse().getOrt() + ", "
                        + "Deutschland");
                verticalLayout.add(formLayoutHinfahrt);
            }
            case RUECKFAHRT -> {
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
                formLayoutRueckfahrt.setTitle("Rückfahrt von " + fahrerRoute.getBenutzer().getUsername());
                formLayoutRueckfahrt.setSitzplaetze(fahrerRoute.getSitzplaetze().toString());
                formLayoutRueckfahrt.setFhLocation(fahrerRoute.getStart().getAdresse().getOrt());
                formLayoutRueckfahrt.setDriveTime(fahrerRoute.getStart().getZeit().toLocalTime());
                formLayoutRueckfahrt.setDriveDateStart(fahrerRoute.getStart().getZeit().toLocalDate());
                formLayoutRueckfahrt.setAddress(fahrerRoute.getZiel().getAdresse().getStrasse() + " "
                        + fahrerRoute.getZiel().getAdresse().getHausnummer() + ", "
                        + fahrerRoute.getZiel().getAdresse().getPlz() + " "
                        + fahrerRoute.getZiel().getAdresse().getOrt() + ", "
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

        Button bookmarkButton = new Button("Fahrt merken");
        bookmarkButton.setId("search-drive-result-view-dialog-bookmark_button");
        bookmarkButton.setClassName("search-drive-result-view-dialog-buttons");
        bookmarkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button requestButton = new Button("Fahrt anfragen");
        requestButton.setId("search-drive-result-view-dialog-request_button");
        requestButton.setClassName("search-drive-result-view-dialog-buttons");
        requestButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("search-drive-result-view-dialog-button_layout");
        buttonLayout.add(bookmarkButton, requestButton);

        return buttonLayout;
    }

}
