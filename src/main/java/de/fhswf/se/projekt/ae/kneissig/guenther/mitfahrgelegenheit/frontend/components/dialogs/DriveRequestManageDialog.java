package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.Booking;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRoute;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.RequestState;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.DuplicateBookingException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRequestService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.DriveRouteService;

import java.time.LocalDateTime;

//TODO: - Fahrtanfragen gehen wieder mehrfach von der gleichen Person für eine fahrt wegen Equals und hashcode
//      -
public class DriveRequestManageDialog extends Dialog {

    private final DriveRequestService driveRequestService;
    private final DriveRouteService driveRouteService;
    private final DriveRequest driveRequest;
    private DriveRoute driveRoute;

    public DriveRequestManageDialog(DriveRequestService driveRequestService, DriveRouteService driveRouteService, DriveRequest driveRequest) {
        this.driveRequestService = driveRequestService;
        this.driveRouteService = driveRouteService;
        this.driveRequest = driveRequest;

        Div div = new Div();

        Button acceptButton = new Button("Akzeptieren");
        Button declineButton = new Button("Ablehnen");

        //TODO: Booking / Auslagern  | Wie setze ich den DriveRequest in der Route dann? scheint zu klappen lol
        acceptButton.addClickListener(e -> {
            saveDriveRequest(RequestState.ACCEPTED);
            System.out.println(driveRouteService.findById(driveRequest.getDriveRoute().getId()).get().getDriveRequests().get(0).getRequestState().label);
        });

        declineButton.addClickListener(e -> {
            saveDriveRequest(RequestState.REJECTED);
        });

        div.add(acceptButton, declineButton);

        add(div);
    }

    private void saveDriveRequest(RequestState requestState) {
        driveRequest.setRequestState(requestState);
        driveRequestService.save(driveRequest);
        if(requestState == RequestState.ACCEPTED){
            Booking newBooking = new Booking(driveRequest.getPassenger(), LocalDateTime.now(), driveRequest.getStopover());
            try {
                driveRequest.getDriveRoute().addBooking(newBooking);
            } catch (DuplicateBookingException ex) {
                ex.printStackTrace();
            }
        }
        driveRouteService.save(driveRequest.getDriveRoute());
    }
}
