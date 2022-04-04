package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.StarsRating;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.formlayouts.FormLayoutDriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;

@CssImport("/themes/mitfahrgelegenheit/components/drive-details-dialog.css")
public class DriveDetailsDialog extends Dialog {

    public DriveDetailsDialog(DriveRoute driveRoute){
        HorizontalLayout driverInformationLayout = new HorizontalLayout();
        driverInformationLayout.setClassName("drive-details-dialog-driver_information_layout");

        H2 title = new H2("Fahrt von " + driveRoute.getDriver().getFullName());
        title.setId("drive-details-dialog-title");

        StarsRating driverRating = new StarsRating(driveRoute.getDriver().getUserRating().getAverageDriverRating());
        driverRating.setId("drive-details-dialog-driver_rating");
        driverRating.setManual(false);

        driverInformationLayout.add(title, driverRating);

        Button profileButton = new Button(VaadinIcon.USER.create());
        profileButton.setText("Profil");
        profileButton.setId("drive-details-dialog-profile_button");
        profileButton.addClickListener(e -> {
            close();
            UI.getCurrent().navigate(ProfileView.class,
                    new RouteParameters(new RouteParam("username", driveRoute.getDriver().getUsername())));

        });

        HorizontalLayout titleLayout = new HorizontalLayout(driverInformationLayout, profileButton);
        titleLayout.setClassName("drive-details-dialog-title_layout");
        add(titleLayout);

        switch (driveRoute.getDriveType()) {

            case OUTWARD_TRIP -> {
                FormLayoutDriveRoute formLayoutDriveRouteTop = new FormLayoutDriveRoute(DriveType.OUTWARD_TRIP);
                formLayoutDriveRouteTop.remove(formLayoutDriveRouteTop.getTitle());
                formLayoutDriveRouteTop.setReadOnly(true);
                formLayoutDriveRouteTop.setData(driveRoute);
                add(formLayoutDriveRouteTop);
            }
            case RETURN_TRIP -> {
                FormLayoutDriveRoute formLayoutDriveRouteBottom = new FormLayoutDriveRoute(DriveType.RETURN_TRIP);
                formLayoutDriveRouteBottom.remove(formLayoutDriveRouteBottom.getTitle());
                formLayoutDriveRouteBottom.setReadOnly(true);
                formLayoutDriveRouteBottom.setData(driveRoute);
                add(formLayoutDriveRouteBottom);
            }

        }

        Button closeButton = new Button("Schließen");
        closeButton.setId("drive-details-dialog-close_button");
        closeButton.addClickListener(e-> close());
        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
        buttonLayout.setClassName("drive-details-dialog-button_layout");
        add(buttonLayout);
    }
}
