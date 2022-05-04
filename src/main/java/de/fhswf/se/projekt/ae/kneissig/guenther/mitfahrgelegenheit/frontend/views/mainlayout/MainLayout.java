package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.mainlayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.services.UserService;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.ButtonSwitchTheme;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components.dialogs.PrivacyDialog;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.DashboardView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.OfferDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.drive.SearchDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.CompletedDriveView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.DriveRequestListView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.OwnDriveOffersView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.managedrive.BookingsView;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.views.profile.ProfileView;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.Target;

/**
 * Die MainView dient dem Erstellen einer MenuBar zum Navigieren in
 * der Applikation.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
@PWA(name = "Drive Together", shortName = "Drive Together", enableInstallPrompt = false)
@Theme(themeFolder = "mitfahrgelegenheit")
@CssImport("/themes/mitfahrgelegenheit/views/main-view.css")
@CssImport(value = "/themes/mitfahrgelegenheit/menu-bar-button.css", themeFor = "vaadin-menu-bar-button")
@CssImport(value = "/themes/mitfahrgelegenheit/buttons.css", themeFor = "vaadin-button")
@CssImport(value = "/themes/mitfahrgelegenheit/radiobutton.css", themeFor = "vaadin-radio-button")
@CssImport(value = "/themes/mitfahrgelegenheit/checkbox.css", themeFor = "vaadin-checkbox")
public class MainLayout extends AppLayout implements PageConfigurator {

    /**
     * Die Konstruktor erstellt eine Menüleiste für die Applikation.
     */
    private final UserService userService;

    public MainLayout(UserService userService){
        this.userService = userService;
        createMenuBar();
    }

    /**
     * Die Methode createMenuBar() baut die Menüleiste der Applikation
     * zusammen. Außerdem weißt sie den RouterLinks die entsprechenden
     * Views zur Navigation zu.
     */
    private void createMenuBar(){

        StreamResource streamResource = new StreamResource("LogoDriveTogether.png",
                () -> MainLayout.class.getClassLoader().
                        getResourceAsStream("images/LogoDriveTogether.png"));

        Image logoFH = new Image (streamResource, "FH SWF");
        logoFH.addClickListener(e -> UI.getCurrent().navigate(DashboardView.class));

        logoFH.setId("logoFH");

        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        menuBar.setOpenOnHover(true);

        /*-------------------------------------------------------------------------------------------------------------
                                                         Haupt-MenuItems
        -------------------------------------------------------------------------------------------------------------*/
        MenuItem searchDrive = menuBar.addItem("Fahrt suchen");
        searchDrive.addClickListener(e -> UI.getCurrent().navigate(SearchDriveView.class));

        MenuItem offerDrive = menuBar.addItem(" Fahrt anbieten");
        offerDrive.addClickListener(e -> UI.getCurrent().navigate(OfferDriveView.class));

        MenuItem manageOwnDriveOffers = menuBar.addItem("Fahrten verwalten");

        MenuItem benutzername = menuBar.addItem(SecurityContextHolder.getContext().getAuthentication().getName());

        /*-------------------------------------------------------------------------------------------------------------
                                                   Untermenü Fahrten verwalten
        -------------------------------------------------------------------------------------------------------------*/
        SubMenu projectSubMenu = manageOwnDriveOffers.getSubMenu();

        MenuItem dashboard = projectSubMenu.addItem("Dashboard");
        dashboard.getElement().getClassList().add("menuItems");
        dashboard.addClickListener(e -> UI.getCurrent().navigate(DashboardView.class));

        MenuItem driveOffers = projectSubMenu.addItem("Eigene Fahrtangebote");
        driveOffers.getElement().getClassList().add("menuItems");
        driveOffers.addClickListener(e -> UI.getCurrent().navigate(OwnDriveOffersView.class));

        MenuItem driveRequests = projectSubMenu.addItem("Fahrtanfragen");
        driveRequests.getElement().getClassList().add("menuItems");
        driveRequests.addClickListener(e -> UI.getCurrent().navigate(DriveRequestListView.class));

        MenuItem bookings = projectSubMenu.addItem("Gebuchte Fahrten");
        bookings.getElement().getClassList().add("menuItems");
        bookings.addClickListener(e -> UI.getCurrent().navigate(BookingsView.class));

        MenuItem completedDrives = projectSubMenu.addItem("Abgeschlossene Fahrten");
        completedDrives.getElement().getClassList().add("menuItems");
        completedDrives.addClickListener(e -> UI.getCurrent().navigate(CompletedDriveView.class));

        /*-------------------------------------------------------------------------------------------------------------
                                                      Untermenü Benutzer
        -------------------------------------------------------------------------------------------------------------*/
        SubMenu usersSubMenu = benutzername.getSubMenu();

        MenuItem profile = usersSubMenu.addItem("Profil");
        profile.getElement().getClassList().add("menuItems");
        profile.addClickListener(e ->
                UI.getCurrent().navigate(ProfileView.class,
                new RouteParameters(new RouteParam("id",
                        userService.getCurrentUser().getId().toString()))));

        MenuItem userGuide = usersSubMenu.addItem("");

        StreamResource res = new StreamResource("UserGuide_Laborordnung.pdf",
                () -> MainLayout.class.getClassLoader().getResourceAsStream("manuals/UserGuide.pdf"));
        Anchor anchorUserGuide = new Anchor();
        anchorUserGuide.setId("anchor_user_guide_light");
        anchorUserGuide.setHref(res);
        anchorUserGuide.setTarget("_blank");
        anchorUserGuide.setText("Bedienungsanleitung");
        userGuide.add(anchorUserGuide);

        MenuItem privacy = usersSubMenu.addItem("Datenschutzerklärung");
        privacy.addClickListener(e->{
            PrivacyDialog privacyDialog = new PrivacyDialog();
            privacyDialog.open();
        });

        
        MenuItem logout = usersSubMenu.addItem("Abmelden");
        logout.getElement().getClassList().add("menuItems");
        logout.addClickListener(e -> UI.getCurrent().getPage().setLocation("/drivetogether/logout"));

        //Button für dunkel/hellen Modus
        ButtonSwitchTheme themeChangeButton = new ButtonSwitchTheme(userService, anchorUserGuide);

        HorizontalLayout header = new HorizontalLayout();
        header.add(logoFH, menuBar, themeChangeButton);
        header.setId("horizontalLayoutMenuBar");

        //die zwei Zeilen sorgen dafür das es Zentriert ist.
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    @Override
    public void configurePage(InitialPageSettings initialPageSettings) {
        LoadingIndicatorConfiguration conf = initialPageSettings.getLoadingIndicatorConfiguration();
        // disable default theme -> loading indicator will not be shown
        conf.setApplyDefaultTheme(false);
    }
}
