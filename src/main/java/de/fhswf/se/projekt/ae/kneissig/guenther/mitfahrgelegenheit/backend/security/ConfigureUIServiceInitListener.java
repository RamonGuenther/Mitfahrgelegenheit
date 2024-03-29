package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login.LoginView;
import org.springframework.stereotype.Component;

/**
 * Die Klasse ConfigureUIServiceInitListener ist dafür zuständig,
 * den Benutzer zur Login-View zurück zu Routen, wenn er nicht
 * autorisiert ist eine View zu betreten.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event){

        event.getSource().addUIInitListener((uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        }));
    }

    private void authenticateNavigation(BeforeEnterEvent event){
        if(!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {

            event.rerouteTo(LoginView.class);
        }
    }
}
