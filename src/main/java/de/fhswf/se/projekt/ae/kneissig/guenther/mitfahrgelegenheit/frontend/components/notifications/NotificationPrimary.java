package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Die Klasse NotificationPrimary ist eine Notification zum
 * Anzeigen eines Hinweises zu einer Aktion der Applikation.
 *
 * @author Ivonne Kneißig & Ramon Günther
 */
public class NotificationPrimary extends Notification {

    public NotificationPrimary(){
        setDuration(5000);
        setPosition(Position.BOTTOM_START);
        addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }

    public static NotificationPrimary show(String text){

        NotificationPrimary notification = new NotificationPrimary();
        notification.setText(text);
        notification.open();
        return notification;
    }
}
