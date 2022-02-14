package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("R & I | Adminseite")
public class Test extends VerticalLayout {
    public Test(){
        add(new Label("Krasse Adminverwaltung..."));
    }
}