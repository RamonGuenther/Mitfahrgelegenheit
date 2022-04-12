package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DecimalStyle;
import java.util.*;

public class GoogleDistanceCalculation implements GoogleApiKey {

    private final GeoApiContext context;

    public GoogleDistanceCalculation() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    }

    public String calculate(Start start, Destination destination, List<Stopover> stopoverList) throws IOException, InterruptedException, ApiException, InvalidAddressException {

        String[] origins = new String[stopoverList.size() + 1];
        origins[0] = start.getFullAddressToString();

        String[] destinations = new String[1];
        destinations[0] = destination.getFullAddressToString();

        for (int i = 0; i < stopoverList.size(); i++) {
            origins[i + 1] = stopoverList.get(i).getFullAddressToString();
        }

        DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations)
                .mode(TravelMode.DRIVING)
                .await();

        TreeMap<Double, String> sortedAddresses = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < matrix.rows.length; i++) {
            System.out.println(matrix.rows[i].elements[0].distance);
            sortedAddresses.put(Double.parseDouble(
                    matrix.rows[i].elements[0].distance.toString().replace(" km", "")), origins[i]
            );
        }

        List<String> result = new ArrayList<>();


        for (Map.Entry<Double, String> entry : sortedAddresses.entrySet()) {
            String value = entry.getValue();
            result.add(value);
        }

        result.remove(start.getFullAddressToString());

        List<Stopover> newStopoverList = new ArrayList<>();

        for (String res : result) {
            AddressConverter addressConverter = new AddressConverter(res);
            newStopoverList.add(new Stopover(new Address(addressConverter.getPostalCode(), addressConverter.getPlace(), addressConverter.getStreet(), addressConverter.getNumber())));
        }

        RouteString routeString = new RouteString(start, destination, newStopoverList);


        return routeString.getRoute();
    }
}

