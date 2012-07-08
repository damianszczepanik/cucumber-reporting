package net.masterthought.cucumber.charts;

import net.masterthought.cucumber.TagObject;

import java.util.*;

public class JsChartUtil {

    class ValueComparator implements Comparator {

        Map base;

        public ValueComparator(Map base) {
            this.base = base;
        }

        public int compare(Object a, Object b) {

            if ((Integer) base.get(a) < (Integer) base.get(b)) {
                return 1;
            } else if ((Integer) base.get(a) == (Integer) base.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public List<String> orderStepsByValue(int numberTotalPassed, int numberTotalFailed, int numberTotalSkipped, int numberTotalPending) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap(bvc);

        map.put("#88dd11", numberTotalPassed);
        map.put("#cc1134", numberTotalFailed);
        map.put("#88aaff", numberTotalSkipped);
        map.put("#FBB917", numberTotalPending);

        sorted_map.putAll(map);
        List<String> colours = new ArrayList<String>();
        for (String colour : sorted_map.keySet()) {
            colours.add(colour);
        }
        return colours;
    }

    public List<String> orderScenariosByValue(int numberTotalPassed, int numberTotalFailed) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap(bvc);

        map.put("#88dd11", numberTotalPassed);
        map.put("#cc1134", numberTotalFailed);

        sorted_map.putAll(map);
        List<String> colours = new ArrayList<String>();
        for (String colour : sorted_map.keySet()) {
            colours.add(colour);
        }
        return colours;
    }

    public static String generateTagChartData(List<TagObject> tagObjectList) {
        StringBuffer buffer = new StringBuffer();
        for (TagObject tag : tagObjectList) {
           buffer.append("[[" + tag.getNumberOfPasses() + "," + tag.getNumberOfFailures() + "," + tag.getNumberOfSkipped() + "," + tag.getNumberOfPending() + "],{label:'" + tag.getTagName() + "'}],");
        }
        return buffer.toString();


    }



}
