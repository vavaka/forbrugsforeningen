package com.forbrugsforeningen.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * User: vavaka
 * Date: 12/25/10 1:48 PM
 */
public class HttpUtils {
    public static String SendHttpRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("Error sending request to " + url);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Unknown exception occured while sending request to" + url);
            e.printStackTrace();
            return null;
        }
    }
}
