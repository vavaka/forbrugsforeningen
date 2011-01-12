package com.forbrugsforeningen.providers;

import com.forbrugsforeningen.data.AddressInfo;
import com.forbrugsforeningen.data.MapLocationInfo;
import com.forbrugsforeningen.data.StoreInfo;
import com.forbrugsforeningen.utils.AddressInfoParser;
import com.forbrugsforeningen.utils.CoordinateInfoParser;
import com.forbrugsforeningen.utils.HttpUtils;

/**
 * User: vavaka
 * Date: 12/27/10 7:22 PM
 */
public class MapLocationInfoProvider {
    public static MapLocationInfo GetMapLocationInfoByStoreInfo(StoreInfo storeInfo) {
        AddressInfo addressInfo = AddressInfoParser.GetAddressInfoFromString(storeInfo.address);
        if (addressInfo == null) {
            System.out.println("AddressInfo not found for:");
            System.out.println("storeInfo.name = " + storeInfo.name);
            System.out.println("storeInfo.address = " + storeInfo.address);
            return null;
        }

        String mapLocationInfoUrl = String.format("http://geo.oiorest.dk/adresser.json?postnr=%s&husnr=%s&vejnavn=%s", storeInfo.postCode, addressInfo.homeNumber, addressInfo.street.replace(" ", "%20"));
        System.out.println("Sending request to: " + mapLocationInfoUrl);
        String mapLocationInfoResponse = HttpUtils.SendHttpRequest(mapLocationInfoUrl);
        if (mapLocationInfoResponse != null) {
            MapLocationInfo mapLocationInfo = CoordinateInfoParser.ParseJson(mapLocationInfoResponse);
            if (mapLocationInfo != null) {
                mapLocationInfo.storeInfo = storeInfo;
                return mapLocationInfo;
            }
        }

        System.out.println("Map location info not found for:");
        System.out.println("storeInfo.name = " + storeInfo.name);
        System.out.println("storeInfo.postCode = " + storeInfo.postCode);
        System.out.println("storeInfo.address = " + storeInfo.address);

        return null;
    }
}
