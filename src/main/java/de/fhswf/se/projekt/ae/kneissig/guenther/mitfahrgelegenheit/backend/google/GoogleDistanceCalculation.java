package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.enums.DriveType;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Address;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Destination;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Start;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.entities.valueobjects.Stopover;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.exceptions.InvalidAddressException;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.AddressConverter;
import de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.utils.RouteString;

import java.io.IOException;
import java.util.*;

/**
 * Die Klasse GoogleDistanceCalculation berechnet die Entfernung zwischen mehreren Adressen,
 * um die optimale Route zurückzugeben.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class GoogleDistanceCalculation implements GoogleApiKey {

    private final GeoApiContext context;

    public GoogleDistanceCalculation() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    }

    /**
     * Berechnet die Entfernung zwischen Startadresse und den Zwischenstopps zu der Zieladresse, sortiert diese,
     * und gibt anschließend eine Google Maps URL zurück.
     *
     * @param start        Startadresse
     * @param destination  Zieladresse
     * @param stopoverList Liste aus Zwischenstopps
     * @return Google Maps URL
     * @throws IOException             -
     * @throws InterruptedException    -
     * @throws ApiException            -
     * @throws InvalidAddressException ungültige Adresse
     */
    public String calculate(Start start, Destination destination, List<Stopover> stopoverList, DriveType driveType) throws IOException, InterruptedException, ApiException, InvalidAddressException {

        String[] origins = new String[stopoverList.size()];

        String[] destinations = new String[1];

        TreeMap<Double, String> sortedAddresses;

        if (driveType.equals(DriveType.OUTWARD_TRIP)) {
            destinations[0] = destination.getFullAddressToString();
            sortedAddresses = new TreeMap<>(Collections.reverseOrder());
        } else {
            destinations[0] = start.getFullAddressToString();
            sortedAddresses = new TreeMap<>();
        }

        for (int i = 0; i < stopoverList.size(); i++) {
            origins[i] = stopoverList.get(i).getFullAddressToString();
        }

        DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations)
                .mode(TravelMode.DRIVING)
                .await();


        for (int i = 0; i < matrix.rows.length; i++) {
            sortedAddresses.put(Double.parseDouble(
                    matrix.rows[i].elements[0].distance.toString().replace(" km", "")), origins[i]
            );
        }

        List<String> result = new ArrayList<>();

        for (Map.Entry<Double, String> entry : sortedAddresses.entrySet()) {
            String value = entry.getValue();
            result.add(value);
        }

        List<Stopover> newStopoverList = new ArrayList<>();

        for (String res : result) {
            AddressConverter addressConverter = new AddressConverter(res);
            newStopoverList.add(new Stopover(new Address(addressConverter.getPostalCode(), addressConverter.getPlace(), addressConverter.getStreet(), addressConverter.getNumber())));
        }

        RouteString routeString = new RouteString(start, destination, newStopoverList);

        return routeString.getRoute();
    }
}

