package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils;

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.*;

import java.util.List;

/**
 * Die Klasse RouteString erstellt den String für eine URL,
 * welche die Website google-Maps öffnet und die Route für
 * die übergebenen Adressen anzeigt.
 *
 * @author Ivonne Kneißig
 */
public class RouteString {

    private String route;

    private Start startAddress;
    private Destination destinationAddress;
    private List<Stopover> stopoverAddresses;

    public RouteString(Start startAddress, Destination destinationAddress, List<Stopover> stopoverAddresses){
        this.startAddress = startAddress;
        this.destinationAddress = destinationAddress;
        this.stopoverAddresses = stopoverAddresses;
        buildRouteURL();
    }

    /**
     * Die Methode buildRouteURL bildet aus den gespeicherten
     * Adress-Komponenten eine URL zum Anzeigen der wahrscheinlichen
     * Route für die Strecke zwischen Start und Ziel bei google-Maps.
     */
    private void buildRouteURL(){

        route = "https://www.google.com/maps/dir/?api=1&origin=" +
                startAddress.getAddress().getStreet() + "+" +
                startAddress.getAddress().getHouseNumber() + "+" +
                startAddress.getAddress().getPostal() + "+" +
                startAddress.getAddress().getPlace();

        if(!stopoverAddresses.isEmpty()){
            route = route + "&waypoints=";
            for(int i = 0; i < stopoverAddresses.size(); i++){
                route = route +
                        stopoverAddresses.get(i).getAddress().getStreet() + "+" +
                        stopoverAddresses.get(i).getAddress().getHouseNumber() + "+" +
                        stopoverAddresses.get(i).getAddress().getPostal() + "+" +
                        stopoverAddresses.get(i).getAddress().getPlace() + "+";
                if(stopoverAddresses.size() > 1 && i < stopoverAddresses.size() - 1){
                    route = route + "|";
                };
            }
        }

        route = route + "&destination=" +
                destinationAddress.getAddress().getStreet() + "+" +
                destinationAddress.getAddress().getHouseNumber() + "+" +
                destinationAddress.getAddress().getPostal() + "+" +
                destinationAddress.getAddress().getPlace() + "&travelmode=driving";
    }

    /**
     * Getter-Methode für die Routen-URL für google-Maps
     *
     * @return              Routen-URL für google-Maps
     */
    public String getRoute() {
        return route;
    }

    /**
     * Setter-Methode für die Routen-URL
     *
     * @param route         Routen-URL für google-Maps
     */
    public void setRoute(String route) {
        if(route.isEmpty()){
            throw new IllegalArgumentException("Fehler: String für Routen-URL ist leer");
        }
        this.route = route;
    }

}
