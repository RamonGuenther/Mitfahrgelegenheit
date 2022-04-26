package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;

@CssImport("/themes/mitfahrgelegenheit/components/drive-details-dialog.css")
public class DriveDetailsDialog extends Dialog {

    public DriveDetailsDialog(DriveRequest driveRequest) {
        HorizontalLayout driverInformationLayout = new HorizontalLayout();
        driverInformationLayout.setClassName("drive-details-dialog-driver_information_layout");

        H2 title = new H2("Fahrt von " + driveRequest.getDriveRoute().getDriver().getFullName());
        title.setId("drive-details-dialog-title");

        StarsRating driverRating = new StarsRating(driveRequest.getDriveRoute().getDriver().getUserRating().getAverageDriverRating());
        driverRating.setId("drive-details-dialog-driver_rating");
        driverRating.setManual(false);

        driverInformationLayout.add(title, driverRating);

        Button profileButton = new Button(VaadinIcon.USER.create());
        profileButton.setText("Profil");
        profileButton.setId("drive-details-dialog-profile_button");
        profileButton.addClickListener(e -> {
            close();
            UI.getCurrent().navigate(ProfileView.class,
                    new RouteParameters(new RouteParam("username", driveRequest.getDriveRoute().getDriver().getUsername())));

        });

        HorizontalLayout titleLayout = new HorizontalLayout(driverInformationLayout, profileButton);
        titleLayout.setClassName("drive-details-dialog-title_layout");
        add(titleLayout);

        FormLayoutDriveRoute formLayoutDriveRouteTop = new FormLayoutDriveRoute(driveRequest.getDriveRoute().getDriveType());
        formLayoutDriveRouteTop.remove(formLayoutDriveRouteTop.getTitle());
        formLayoutDriveRouteTop.setData(driveRequest.getDriveRoute());
        formLayoutDriveRouteTop.setReadOnly(true);
        add(formLayoutDriveRouteTop);

        if (driveRequest.getRegularDriveSingleDriveDate() != null) {
            Label singleDriveLabel = new Label("Achtung: Du hast deine Anfrage nur für den "
                    + driveRequest.getFormattedSingleDriveDate() + " gestellt.");
            singleDriveLabel.setId("drive-request-manage-dialog-label_single_drive");
            formLayoutDriveRouteTop.addComponentAtIndex(10, singleDriveLabel);
            formLayoutDriveRouteTop.setColspan(singleDriveLabel, 4);
        }


        Button closeButton = new Button("Schließen");
        closeButton.setId("drive-details-dialog-close_button");
        closeButton.addClickListener(e -> close());
        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
        buttonLayout.setClassName("drive-details-dialog-button_layout");
        add(buttonLayout);
    }
}
