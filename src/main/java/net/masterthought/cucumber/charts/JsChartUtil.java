package net.masterthought.cucumber.charts;

import net.masterthought.cucumber.TagObject;

import java.util.*;
import java.util.logging.Logger;

public class JsChartUtil {

    private static Logger logger = Logger.getLogger("net.masterthought.cucumber.charts.jschartutil");

    public List<String> orderStepsByValue(int numberTotalPassed, int numberTotalFailed, int numberTotalSkipped, int numberTotalPending) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put("#88dd11", numberTotalPassed);
        map.put("#cc1134", numberTotalFailed);
        map.put("#88aaff", numberTotalSkipped);
        map.put("#FBB917", numberTotalPending);

        return getKeysSortedByValue(map);
    }

    public List<String> orderScenariosByValue(int numberTotalPassed, int numberTotalFailed) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("#88dd11", numberTotalPassed);
        map.put("#cc1134", numberTotalFailed);

        return getKeysSortedByValue(map);
    }

    private List<String> getKeysSortedByValue(Map<String, Integer> map) {
        List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });


        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : list) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public static String generateTagChartData(List<TagObject> tagObjectList) {
        StringBuffer buffer = new StringBuffer();
        for (TagObject tag : tagObjectList) {
           buffer.append("[[" + tag.getNumberOfPasses() + "," + tag.getNumberOfFailures() + "," + tag.getNumberOfSkipped() + "," + tag.getNumberOfPending() + "],{label:'" + tag.getTagName() + "'}],");
        }
        return buffer.toString();
    }

    public static String getTags(List<TagObject> tagObjectList) {
        StringBuffer tags = new StringBuffer();
        int counter = 0;
        for (TagObject tag : tagObjectList) {
            tags.append("'" + tag.getTagName() + "'");

            if (tagObjectList.size() != 1 && tagObjectList.size() <= counter) {
                tags.append(",");
            }

            counter++;
        }

        return "[" + tags.toString() + "]";
    }

    public static String generateTagChartDataForHighCharts(List<TagObject> tagObjectList) {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;

        for (TagObject tag : tagObjectList) {
            buffer.append("[" + tag.getNumberOfPasses() + "," + tag.getNumberOfFailures() + "," + tag.getNumberOfSkipped() + "," + tag.getNumberOfPending() + "]");

            if (tagObjectList.size() != 1 && tagObjectList.size() <= counter) {
                buffer.append(",");
            }

            counter++;
        }

        return "[" + buffer.toString() + "]";
    }



}
