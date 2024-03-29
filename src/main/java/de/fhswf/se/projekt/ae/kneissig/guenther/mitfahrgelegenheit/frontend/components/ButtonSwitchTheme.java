package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.User;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;

/**
 * Ein spezieller Button für das Wechseln zwischen dunklem
 * und hellem Design.
 *
 * @author Ramon Günther & Ivonne Kneißg
 */
@CssImport("/themes/mitfahrgelegenheit/components/button-switch-theme.css")
public class ButtonSwitchTheme extends Button {

    private final UserService userService;

    private final Icon sun;
    private final Icon moon;
    private final User user;

    private final Anchor userGuide;

    public ButtonSwitchTheme(UserService userService, Anchor userGuide) {
        this.userService = userService;
        this.userGuide = userGuide;

        user = userService.getCurrentUser();

        sun = new Icon(VaadinIcon.SUN_O);
        sun.setClassName("button-switch-theme-icons");
        moon = new Icon(VaadinIcon.MOON);
        moon.setClassName("button-switch-theme-icons");

        setText("Darkmode");
        setIcon(moon);
        setClassName("button-switch-theme-button_position");
        setId("button-switch-theme-light_mode");
        addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addClickListener(e -> themeEvent());

        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (user.isDarkMode()) {
            setIcon(sun);
            setText("Lightmode");
            setId("button-switch-theme-dark_mode");
            userGuide.setId("anchor_user_guide_dark");
            themeList.add(Lumo.DARK);
        }
    }

    /**
     * Die Methode themeEvent löst das Event für das Wechseln des
     * Designs aus.
     */
    void themeEvent() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK)) {
            setIcon(moon);
            setText("Darkmode");
            setId("button-switch-theme-light_mode");
            themeList.remove(Lumo.DARK);
            userGuide.setId("anchor_user_guide_light");
            user.setDarkMode(false);
        } else {
            setIcon(sun);
            setText("Lightmode");
            setId("button-switch-theme-dark_mode");
            themeList.add(Lumo.DARK);
            userGuide.setId("anchor_user_guide_dark");
            user.setDarkMode(true);
        }

        userService.save(user);
    }
}
