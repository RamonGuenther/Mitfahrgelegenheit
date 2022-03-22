package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;


import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.List;

public class GoogleDistanceCalculation implements GoogleApiKey{

    private GeoApiContext context;
    private DistanceMatrix matrix;

    public GoogleDistanceCalculation () {

        try {
            context = new GeoApiContext.Builder().apiKey(API_KEY).build();

//  Entweder kann man die Liste der Start- / Zielpunkte so übergeben
//          DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
//          matrix = req.origins("Vancouver BC","Seattle")
//                .destinations("San Francisco","Victoria BC")
//                .mode(TravelMode.DRIVING)
//                .language("de-DE")
//                .await();

//  Oder man füllt vorher Listen und übergibt die ganze Liste direkt (Achtung: anderer Methodenaufruf bei GoogleMatrixApi
//  Wenn man ne Liste von Orten einfügt, die er checken soll, dann ist es trotzdem nur eine Anfrage

            String[] origins = new String[]{
                    "Diesterwegstraße 6, 58095 Hagen",
                    "Sundernallee 75, 58636 Iserlohn",

            };

            String[] destinations = new String[]{ //hier also nur eine destination da er sonst immer nur mit dem ersten vergleicht?
                    "Frauenstuhlweg 31, 58644 Iserlohn",
                    "Sundernallee 75, 58636 Iserlohn",

            };
            //Also brauchen wir ein Select, das alle Fahrten rauszieht die dem Ziel bzw. dem Start entsprechen und sortieren die dann anschließend der Km anzahl nach


            matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations)
                    .mode(TravelMode.DRIVING)
                    .await();

//  Testausgabe:
            for (int i = 0; i < matrix.rows.length; i++) {
                System.out.println(matrix.rows[i].elements[0].distance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeoApiContext getContext() {
        return context;
    }

    public void setContext(GeoApiContext context) {
        this.context = context;
    }

    public DistanceMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(DistanceMatrix matrix) {
        this.matrix = matrix;
    }
}

