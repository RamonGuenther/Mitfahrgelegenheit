package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;

@Route("login")
@PageTitle("Mitfahrgelegenheit | Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{

    private LoginForm login;

    public LoginView() {

        getElement().getStyle().set("background", "hsl(0, 36%, 39%)");

        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        login = new LoginForm();
        login.setAction("login");

        add(login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}