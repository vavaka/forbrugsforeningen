package com.forbrugsforeningen.providers;

import com.forbrugsforeningen.data.LocationInfo;
import com.forbrugsforeningen.data.StoreInfo;
import com.forbrugsforeningen.utils.HttpUtils;
import com.forbrugsforeningen.utils.StoreInfoParser;

import java.util.List;

/**
 * User: vavaka
 * Date: 12/25/10 2:45 PM
 */
public class StoresInfoProvider {
    public static List<StoreInfo> GetStoresInfoByPostCodeAndQueryString(String postInfo, String queryString) {
        String storesInfoUrl = String.format("http://www.forbrugsforeningen.dk/Default.aspx?ID=2860&aq=%s+%s", postInfo.replace(" ", "%20"), queryString);
        String storesInfoResponse = HttpUtils.SendHttpRequest(storesInfoUrl);

        if (storesInfoResponse != null) {
            return StoreInfoParser.ParseHtml(storesInfoResponse);
        } else {
            return null;
        }
    }
}
