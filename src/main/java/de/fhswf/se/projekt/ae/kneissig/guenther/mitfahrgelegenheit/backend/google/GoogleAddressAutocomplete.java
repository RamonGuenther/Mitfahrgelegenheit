package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Die Klasse GoogleAddressAutocomplete wird von der Klasse TextFieldAddress verwendet
 * um dem Nutzer mithilfe der Google Places API, Adressen vorzuschlagen.
 *
 * @author Ramon Günther & Ivonne Kneißig
 */
public class GoogleAddressAutocomplete implements GoogleApiKey{
    static final LatLng LOCATION = new LatLng(51.51589968326413, 7.457918235941882);
    private final GeoApiContext context;
    private AutocompletePrediction[] results;
    private final PlaceAutocompleteRequest.SessionToken token;

    public GoogleAddressAutocomplete() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
        results = new AutocompletePrediction[0];
        token = new PlaceAutocompleteRequest.SessionToken();
    }

    /**
     * Gibt eine Liste aus Strings zurück, die Straßenvorschläge der Google API enthält.
     *
     * @param input Eingabe des Nutzers
     * @return Eine Liste von Vorschlägen der Google API
     *
     * @throws IOException -
     * @throws InterruptedException -
     * @throws ApiException -
     */
    public List<String> findStreets(String input) throws IOException, InterruptedException, ApiException {
        List<String> newPredictions = new ArrayList<>();
        results = PlacesApi.placeAutocomplete(context, input, token)
                .location(LOCATION)
                .offset(10)
                .radius(169991)
                .components(ComponentFilter.country("DE"))
                .types(PlaceAutocompleteType.ADDRESS)
                .strictBounds(true)
                .await();

        for (AutocompletePrediction s : results) {
            PlaceDetails placeDetails = PlacesApi.placeDetails(context, s.placeId, token).language("DE").await();
            newPredictions.add(placeDetails.formattedAddress);
        }

        return newPredictions;
    }
}
