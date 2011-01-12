package com.forbrugsforeningen.providers;

import android.location.Location;
import android.location.LocationManager;
import com.forbrugsforeningen.data.Coordinates;

/**
 * User: vavaka
 * Date: 12/27/10 7:53 PM
 */
public class CoordinatesProvider {
    public static Coordinates GetCoordinatesFromLocationManager(LocationManager locationManager) {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            /*Coordinates coordinate = new Coordinates();
            coordinate.latitude = 55.785336902096731;
            coordinate.longitude = 12.451068626837296;
            return coordinate;*/
            return null;
        } else {
            Coordinates coordinates = new Coordinates();
            coordinates.latitude = location.getLatitude();
            coordinates.longitude = location.getLongitude();

            return coordinates;
        }
    }
}
