package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.notifications;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Die Klasse NotificationSuccess ist eine Notification für
 * eine Erfolgreiche Aktion.
 *
 * @author Ivonne Kneißig & Ramon Günther
 */
public class NotificationSuccess extends Notification {

    public NotificationSuccess(){
        setDuration(5000);
        setPosition(Position.BOTTOM_START);
        addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static NotificationSuccess show(String text){

        NotificationSuccess notification = new NotificationSuccess();
        notification.setText(text);
        notification.open();
        return notification;
    }
}
