package com.example.dominikglueck.whatshouldaido;
import android.content.Context;
import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

/**
 * Created by dominik.glueck on 12.08.2016.
 */
public class googlemapsActivity {
    double latitude;
    double longitude;
    String suche;
    Context context;

    List<Address> geocodeMatches = null;
public void umgebung() {
    try {
        geocodeMatches =
                new Geocoder(context).getFromLocationName(
                        suche, 1);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    if (!geocodeMatches.isEmpty()) {
        latitude = geocodeMatches.get(0).getLatitude();
        longitude = geocodeMatches.get(0).getLongitude();
    }
}
}
