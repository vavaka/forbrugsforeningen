package com.forbrugsforeningen.utils;

import com.forbrugsforeningen.data.LocationInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: vavaka
 * Date: 12/25/10 1:43 PM
 */
public class LocationInfoParser {
    public static LocationInfo ParseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            LocationInfo postCodeInfo = new LocationInfo();
            postCodeInfo.postCode = jsonObject.getString("nr");
            postCodeInfo.postName = jsonObject.getString("navn");
            postCodeInfo.area = jsonObject.getString("areal");

            System.out.println("PostCodeInfo from http://geo.oiorest.dk");
            System.out.println("postCodeInfo.postCode = " + postCodeInfo.postCode);
            System.out.println("postCodeInfo.postName = " + postCodeInfo.postName);
            System.out.println("postCodeInfo.postName = " + postCodeInfo.area);

            return postCodeInfo;
        } catch (JSONException e) {
            System.out.println("Error parsing location info from json string:");
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
