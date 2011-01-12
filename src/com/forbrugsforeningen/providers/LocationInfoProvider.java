package com.forbrugsforeningen.providers;

import com.forbrugsforeningen.data.Coordinates;
import com.forbrugsforeningen.data.LocationInfo;
import com.forbrugsforeningen.utils.HttpUtils;
import com.forbrugsforeningen.utils.LocationInfoParser;

/**
 * User: vavaka
 * Date: 12/25/10 2:43 PM
 */
public class LocationInfoProvider {
    public static LocationInfo GetLocationInfoByCoordinate(Coordinates coordinate) {
        String locationInfoUrl = String.format("http://geo.oiorest.dk/postnumre/%s,%s.json", String.valueOf(coordinate.latitude), String.valueOf(coordinate.longitude));
        String locationInfoResponse = HttpUtils.SendHttpRequest(locationInfoUrl);
        if (locationInfoResponse != null) {
            return LocationInfoParser.ParseJson(locationInfoResponse);
        } else {
            return null;
        }
    }
}
