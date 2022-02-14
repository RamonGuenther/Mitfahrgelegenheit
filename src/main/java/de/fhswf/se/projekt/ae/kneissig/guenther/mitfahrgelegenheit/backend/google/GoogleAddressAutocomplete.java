package de.fhswf.se.projekt.ae.kneissig.guenther.mitfahrgelegenheit.backend.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;
import java.util.List;

public class GoogleAddressAutocomplete implements GoogleApiKey{
    static final LatLng LOCATION = new LatLng(51.51589968326413, 7.457918235941882);

    private GeoApiContext context;
    private AutocompletePrediction[] results;
    private PlaceDetails placeDetails;
    private PlaceAutocompleteRequest.SessionToken token;

    public GoogleAddressAutocomplete() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();
        results = new AutocompletePrediction[0];
        token = new PlaceAutocompleteRequest.SessionToken();
    }

    public List<String> test(List<String> daten, String eventValue) throws IOException, InterruptedException, ApiException {
        results = PlacesApi.placeAutocomplete(context, eventValue, token)
                .location(LOCATION)
                .offset(4)
                .radius(169991)
                .components(ComponentFilter.country("DE"))
                .types(PlaceAutocompleteType.ADDRESS)
                .strictBounds(true)
                .await();

        for (AutocompletePrediction s : results) {
            placeDetails = PlacesApi.placeDetails(context, s.placeId, token).language("DE").await();
            daten.add(placeDetails.formattedAddress);
        }

        return daten;
    }
}
