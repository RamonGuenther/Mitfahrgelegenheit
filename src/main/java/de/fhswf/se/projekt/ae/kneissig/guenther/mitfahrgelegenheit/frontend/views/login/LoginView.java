package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;

@Route("login")
@PageTitle("Mitfahrgelegenheit | Login")
@CssImport(value = "/themes/mitfahrgelegenheit/views/login-view.css", themeFor = "vaadin-login-overlay-wrapper")
public class LoginView extends Div implements BeforeEnterObserver {

    private final LoginOverlay loginOverlay;

    public LoginView() {
        getElement().getStyle().set("background", "#3e5365");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Anmelden");
        i18nForm.setUsername("Benutzername");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Passwort vergessen?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Ungültige Anmeldeinformationen");
        i18nErrorMessage.setMessage("Der Benutzername oder das Passwort ist falsch.");
        i18n.setErrorMessage(i18nErrorMessage);

        loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);
        loginOverlay.setAction("login");
        loginOverlay.setTitle("Drive Together");
        loginOverlay.setDescription("Bitte benutzen Sie ihren FH-Login");
        loginOverlay.setOpened(true);

        add(loginOverlay);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginOverlay.setError(true);
        }
    }
}