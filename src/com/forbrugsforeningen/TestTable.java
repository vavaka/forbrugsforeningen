package com.forbrugsforeningen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: vavaka
 * Date: 12/24/10 4:53 PM
 */
public class TestTable {
    public static void main(String[] args) {
        String text = "2tal Virum Radio & TvVirum Torv 4\n2830  Virum";

        //Pattern patternTable = Pattern.compile("/<table[^>].+?ctl01_DG1.+?</table>");
        Pattern patternTable = Pattern.compile("^(.+?)\\s+(\\d{4})\\s+(.+)$", Pattern.DOTALL);
        Matcher matcherTable = patternTable.matcher(text);
        matcherTable.find();
        String table = matcherTable.group(0);
        System.out.println(matcherTable.group(1));
        System.out.println(matcherTable.group(2));
        System.out.println(matcherTable.group(3));

        /*Pattern rowsCountTable = Pattern.compile("<table[^>]+ctl01_DG1.+?</table>", Pattern.DOTALL);
        Matcher matcherTable = patternTable.matcher(str);
        matcherTable.find();
        String table = matcherTable.group(0);*/

        /*System.out.println("Table found = " + matcher.find());
        System.out.println("Table content:");
        System.out.println(matcher.group(0));*/
    }
}
