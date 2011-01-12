package com.forbrugsforeningen.utils;

import com.forbrugsforeningen.data.StoreInfo;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: vavaka
 * Date: 12/25/10 1:53 PM
 */
public class StoreInfoParser {
    public static List<StoreInfo> ParseHtml(String htmlString) {
        ArrayList<StoreInfo> storeInfoList = new ArrayList<StoreInfo>();

        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);

        TagNode node;
        try {
            node = cleaner.clean(htmlString);
        } catch (IOException e) {
            System.out.println("Error occured while cleaning stores info html:");
            System.out.println(htmlString);
            e.printStackTrace();
            return null;
        }

        System.out.println("Looking for stores info table");
        Object[] objects;
        try {
            objects = node.evaluateXPath("//table[@id='ctl01_DG1']//tbody");
        } catch (XPatherException e) {
            System.out.println("XPath error occured while finding stores info table:");
            System.out.println(htmlString);
            e.printStackTrace();
            return null;
        }

        if (objects.length != 0) {
            System.out.println("Stores info table found");
        } else {
            System.out.println("Stores info table not found");
            return storeInfoList;
        }

        TagNode tbody = (TagNode) objects[0];
        for (int i = 3; i < tbody.getChildren().size(); i++) {
            try {
                StoreInfo storeInfo = new StoreInfo();
                storeInfo.name = ((TagNode) node.evaluateXPath("//tr[" + i + "]//td//b")[0]).getChildren().iterator().next().toString().trim();
                String col1Text = ((TagNode) node.evaluateXPath("//tr[" + i + "]//td")[0]).getText().toString();
                Pattern patternCol1 = Pattern.compile("^(.+?)\\s+(\\d{4})\\s+(.+)$", Pattern.DOTALL);
                Matcher matcherCol1 = patternCol1.matcher(col1Text);
                matcherCol1.find();
                storeInfo.address = matcherCol1.group(1).replace(storeInfo.name, "");
                storeInfo.postCode = matcherCol1.group(2);
                storeInfo.postName = matcherCol1.group(3);
                storeInfo.phone = ((TagNode) node.evaluateXPath("//tr[" + i + "]//td[2]")[0]).getChildren().iterator().next().toString().trim();
                storeInfo.discount = ((TagNode) node.evaluateXPath("//tr[" + i + "]//td[4]//p")[0]).getChildren().iterator().next().toString().trim();
                storeInfo.webSite = ((TagNode) node.evaluateXPath("//tr[" + i + "]//td[4]//p[2]//a[@href]")[0]).getAttributeByName("href");

                System.out.println("storeInfo.name = " + storeInfo.name);
                System.out.println("storeInfo.address = " + storeInfo.address);
                System.out.println("storeInfo.postCode = " + storeInfo.postCode);
                System.out.println("storeInfo.postName = " + storeInfo.postName);
                System.out.println("storeInfo.phone = " + storeInfo.phone);
                System.out.println("storeInfo.discount = " + storeInfo.discount);
                System.out.println("storeInfo.webSite = " + storeInfo.webSite);

                storeInfoList.add(storeInfo);
            } catch (XPatherException e) {
                System.out.println("XPath error occured while getting store info data:");
                System.out.println(htmlString);
                e.printStackTrace();
            }
        }

        return storeInfoList;
    }
}
