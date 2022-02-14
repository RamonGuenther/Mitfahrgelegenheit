package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ButtonSwitchTheme;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.OfferDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.SearchDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.login.LoginView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.CompletedDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.DriveRequestListView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.OwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.PlannedDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.BookmarksView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Die MainView dient dem Erstellen einer MenuBar zum Navigieren in
 * der Appilkation.
 *
 * @author Ivonne Kneißig und Ramon Günther
 */

@PWA(name = "Mitfahrgelegenheiten", shortName = "Mitfahrgelegenheiten", enableInstallPrompt = false)
@Theme(themeFolder = "mitfahrgelegenheit")
@CssImport("/themes/mitfahrgelegenheit/views/main-view.css")
@CssImport(value = "/themes/mitfahrgelegenheit/components/menu-bar-button.css", themeFor = "vaadin-menu-bar-button")
public class MainLayout extends AppLayout {

    /**
     * Die Konstruktor erstellt eine Menüleiste für die Applikation.
     */

    public MainLayout(){
        setId("app");
        createMenuBar();
    }

    /**
     * Die Methode createMenuBar() baut die Menüleiste der Applikation
     * zusammen. Außerdem weißt sie den RouterLinks die entsprechenden
     * Views zur Navigation zu.
     */

    private void createMenuBar(){

        StreamResource streamResource = new StreamResource("LogoMitfahrgelegenheit.png",
                () -> MainLayout.class.getClassLoader().
                        getResourceAsStream("images/LogoMitfahrgelegenheit.png"));

        Image logoFH = new Image (streamResource, "FH SWF");
        logoFH.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        logoFH.setId("logoFH");

        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        menuBar.setOpenOnHover(true);

        // Haupt-MenuItems
        MenuItem fahrtSuchen = menuBar.addItem("Fahrt suchen");
        fahrtSuchen.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        MenuItem fahrtAnbieten = menuBar.addItem(" Fahrt anbieten");
        fahrtAnbieten.addClickListener(e -> UI.getCurrent().navigate(OfferDriveView.class));

        MenuItem fahrtVerwalten = menuBar.addItem("Fahrten verwalten");

        MenuItem benutzername = menuBar.addItem(SecurityContextHolder.getContext().getAuthentication().getName());

        // Untermenü Fahrten verwalten
        SubMenu projectSubMenu = fahrtVerwalten.getSubMenu();
        MenuItem fahrtAngebote = projectSubMenu.addItem("Eigene Fahrtangebote");
        fahrtAngebote.getElement().getClassList().add("menuItems");
        fahrtAngebote.addClickListener(e -> UI.getCurrent().navigate(OwnDriveOffersView.class));

        MenuItem fahrtAnfragen = projectSubMenu.addItem("Fahrtanfragen");
        fahrtAnfragen.getElement().getClassList().add("menuItems");
        fahrtAnfragen.addClickListener(e -> UI.getCurrent().navigate(DriveRequestListView.class));

        MenuItem geplanteFahrten = projectSubMenu.addItem("gebuchte Fahrten");
        geplanteFahrten.getElement().getClassList().add("menuItems");
        geplanteFahrten.addClickListener(e -> UI.getCurrent().navigate(PlannedDriveView.class));

        MenuItem abgeschlosseneFahrten = projectSubMenu.addItem("Abgeschlossene Fahrten");
        abgeschlosseneFahrten.getElement().getClassList().add("menuItems");
        abgeschlosseneFahrten.addClickListener(e -> UI.getCurrent().navigate(CompletedDriveView.class));


        // Untermenü Benutzer
        SubMenu usersSubMenu = benutzername.getSubMenu();

        MenuItem profilAnzeigen = usersSubMenu.addItem("Profil");
        profilAnzeigen.getElement().getClassList().add("menuItems");
        profilAnzeigen.addClickListener(e -> UI.getCurrent().navigate(ProfileView.class));

        MenuItem merkliste= usersSubMenu.addItem("Merkliste");
        merkliste.getElement().getClassList().add("menuItems");
        merkliste.addClickListener(e -> UI.getCurrent().navigate(BookmarksView.class));

        MenuItem abmelden = usersSubMenu.addItem("Abmelden");
        abmelden.getElement().getClassList().add("menuItems");

//        abmelden.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        abmelden.addClickListener(e -> UI.getCurrent().getPage().setLocation("/logout"));

        //Button für dunkel/hellen Modus
        ButtonSwitchTheme themeChangeButton = new ButtonSwitchTheme();

        HorizontalLayout header = new HorizontalLayout();
        header.add(logoFH, menuBar, themeChangeButton);
        header.setId("horizontalLayoutMenuBar");

        //die zwei Zeilen sorgen dafür das es Zentriert ist.
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
        setId("testi");

    }
}
