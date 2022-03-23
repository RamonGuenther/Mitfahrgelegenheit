package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.*;

public class GoogleDistanceCalculation implements GoogleApiKey {

    private final GeoApiContext context;

    public GoogleDistanceCalculation() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    }

    public List<String> calculate(List<String> newOrigins, String destination) throws IOException, InterruptedException, ApiException {

        List<String> result = new ArrayList<>();
        TreeMap<Double, String> sortedAddresses = new TreeMap<>(Collections.reverseOrder());

        String[] origins = newOrigins.toArray(new String[0]);
        String[] destinations = new String[1];
        destinations[0] = destination;

        DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations)
                .mode(TravelMode.DRIVING)
                .await();

        for (int i = 0; i < matrix.rows.length; i++) {
            System.out.println(matrix.rows[i].elements[0].distance);
            sortedAddresses.put(Double.parseDouble(
                            matrix.rows[i].elements[0].distance.toString().replace(" km", "")),
                    origins[i]
            );
        }

        for (Map.Entry<Double, String> entry : sortedAddresses.entrySet()) {
            String value = entry.getValue();
            result.add(value);
        }
        return result;
    }
}

