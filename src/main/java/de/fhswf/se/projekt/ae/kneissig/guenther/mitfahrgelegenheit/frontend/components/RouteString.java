package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.frontend.components;

// TODO
// Später wieder mögliche Umwege einbauen

import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.DriveRequest;

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

    private final String streetStart;
    private final String numberStart;
    private final String plzStart;
    private final String placeStart;

    private final String streetDestination;
    private final String numberDestination;
    private final String plzDestination;
    private final String placeDestination;
    
//    private String streetDetour;
//    private String numberDetour;
//    private String plzDetour;
//    private String placeDetour;


    public RouteString(String streetStart, String numberStart, String plzStart, String placeStart,
                       String streetDestination, String numberDestination, String plzDestination, String placeDestination) {

        this.streetStart = streetStart.trim();
        this.numberStart = numberStart.trim();
        this.plzStart = plzStart.trim();
        this.placeStart = placeStart.trim();

        this.streetDestination = streetDestination.trim();
        this.numberDestination = numberDestination.trim();
        this.plzDestination =plzDestination.trim();
        this.placeDestination = placeDestination.trim();
        buildRouteURL();

    }

    /**
     * Die Methode buildRouteURL bildet aus den gespeicherten
     * Adress-Komponenten eine URL zum Anzeigen der wahrscheinlichen
     * Route für die Strecke zwischen Start und Ziel bei google-Maps.
     */
    private void buildRouteURL(){
        route = "https://www.google.com/maps/dir/?api=1&origin=" + streetStart + "+" +
                numberStart + "+" + plzStart + "+" + placeStart + "&destination=" + streetDestination + "+" +
                numberDestination + "+" + plzDestination + "+" + placeDestination + "&travelmode=driving";
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

//    /**
//     * Die Methode buildDetourRouteURL bildet aus übergebenen
//     * Adressteilen die einzelnen Komponenten für einen möglichen
//     * Umweg der ursprünglichen Route und speichert diese in den
//     * Klassenattributen. Daraus wir eine neue google-Maps-URL
//     * gebaut, welche die neue Route mit Umweg anzeigt.
//     *
//     * @param address           Straße und Haußnummer der Umwegadresse
//     * @param plz               Postleitzahl der Umwegadresse
//     * @param place             Ort der Umwegadresse
//     */
//    public void buildDetourRouteURL(String address, String plz, String place){
//
//        address = replaceUmlaut(address);
//
//        Pattern pattern = Pattern.compile(STREET_PATTERN);
//
//        Matcher matcherStart = pattern.matcher(address);
//        boolean matchFound = matcherStart.find();
//        if (matchFound) {
//            streetDetour= matcherStart.group(1);
//            numberDetour = matcherStart.group(2);
//            plzDetour = plz;
//            placeDetour = place;
//        }
//
//        detour = "https://www.google.com/maps/dir/?api=1&origin=" + streetStart + "+" +
//                numberStart + "+" + plzStart + "+" + placeStart +
//                "&waypoints=" + streetDetour + "+" + numberDetour + "+" + plzDetour + "+" + placeDetour +
//                "&destination=" + streetDestination + "+" +
//                numberDestination + "+" + plzDestination + "+" + placeDestination + "&travelmode=driving";
//    }
//
//    /**
//     * Getter-Methode für die Umweg-URL für google-Maps
//     *
//     * @return              URL für Route mit Umweg google-Maps
//     */
//    public String getDetour(){
//        return detour;
//    }
//
//    /**
//     * Setter-Methode für die Routen-URL
//     *
//     * @param detour         URL für Route mit Umweg google-Maps
//     */
//    public void setDetour(String detour){
//
//        if(detour.isEmpty()){
//            throw new IllegalArgumentException("Fehler: String für Route mit Umweg-URL ist leer");
//        }
//
//        this.detour = detour;
//    }

}
