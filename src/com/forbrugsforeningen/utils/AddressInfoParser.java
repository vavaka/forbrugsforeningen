package com.forbrugsforeningen.utils;

import com.forbrugsforeningen.data.AddressInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: vavaka
 * Date: 12/27/10 7:37 PM
 */
public class AddressInfoParser {
    public static AddressInfo GetAddressInfoFromString(String address){
        Pattern pattern = Pattern.compile("(\\D+)(\\d+)");
        Matcher matcher = pattern.matcher(address);
        if(!matcher.find()){
            return null;
        }
        else{
            AddressInfo addressInfo = new AddressInfo();
            addressInfo.street = matcher.group(1);
            addressInfo.homeNumber = matcher.group(2);
            return addressInfo;
        }
    }
}
