package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums;

public enum Faculty {

    ELEKTROTECHNIK_UND_INFORMATIONSTECHNIK("Elektrotechnik und Informationstechnik"),
    TECHNISCHE_BETRIEBSWIRTSCHAFT("Technische Betriebswirtschaft"),
    INFORMATIK_UND_NATURWISSENSCHAFTEN("Informatik und Naturwissenschaften"),
    MASCHINENBAU("Maschinenbau"),
    INGENIEUR_UND_WIRTSCHAFTSWISSENSCHAFTEN("Ingenieur- und Wirtschaftswissenschaften"),
    AGRARWIRTSCHAFT("Agrarwirtschaft"),
    BILDUNGS_UND_GESELLSCHAFTSWISSENSCHAFTEN("Bildungs- und Gesellschaftswissenschaften"),
    ELEKTRISCHE_ENERGIERTECHNIK("Elektrische Energiertechnik"),
    MASCHINENBAU_AUTOMATISIERUNGSTECHNIK("Maschinenbau-Automatisierungstechnik");

    public final String label;

    Faculty(String label) {
        this.label = label;
    }
}
