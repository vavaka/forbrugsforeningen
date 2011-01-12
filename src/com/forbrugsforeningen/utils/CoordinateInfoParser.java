package com.forbrugsforeningen.utils;

import com.forbrugsforeningen.data.MapLocationInfo;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * User: vavaka
 * Date: 12/25/10 1:43 PM
 */
public class CoordinateInfoParser {
    public static MapLocationInfo ParseJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            if(jsonArray.length() == 0){
                System.out.println("Coordinates Info not found at http://geo.oiorest.dk");
                return null;
            }

            MapLocationInfo mapLocationInfo = new MapLocationInfo();
            mapLocationInfo.latitude = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("wgs84koor").getString("latitude"));
            mapLocationInfo.longitude = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("wgs84koor").getString("longitude"));

            System.out.println("Coordinates Info from http://geo.oiorest.dk");
            System.out.println("mapLocationInfo.latitude = " + mapLocationInfo.latitude);
            System.out.println("mapLocationInfo.longitude = " + mapLocationInfo.longitude);

            return mapLocationInfo;
        } catch (JSONException e) {
            System.out.println("Error parsing coordinate info from json string:");
            System.out.println(jsonString);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Unknown exception occured while parsing location info from json string:");
            System.out.println(jsonString);
            e.printStackTrace();
            return null;
        }
    }
}
