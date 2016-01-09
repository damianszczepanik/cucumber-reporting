package net.masterthought.cucumber.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;

public class JsChartUtil {

    public List<String> orderStepsByValue(int numberTotalPassed, int numberTotalFailed, int numberTotalSkipped,
            int numberTotalPending, int numberTotalUndefined, int numberTotalMissing) {

        EnumMap<Status, Integer> map = new EnumMap<>(Status.class);

        map.put(Status.PASSED, numberTotalPassed);
        map.put(Status.FAILED, numberTotalFailed);
        map.put(Status.SKIPPED, numberTotalSkipped);
        map.put(Status.PENDING, numberTotalPending);
        map.put(Status.UNDEFINED, numberTotalUndefined);
        map.put(Status.MISSING, numberTotalMissing);

        return getKeysSortedByValue(map);
    }

    public List<String> orderScenariosByValue(int numberTotalPassed, int numberTotalFailed) {

        EnumMap<Status, Integer> map = new EnumMap<>(Status.class);

        map.put(Status.PASSED, numberTotalPassed);
        map.put(Status.FAILED, numberTotalFailed);

        return getKeysSortedByValue(map);
    }

    private List<String> getKeysSortedByValue(Map<Status, Integer> map) {
        List<Map.Entry<Status, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Status, Integer>>() {
            @Override
            public int compare(Map.Entry<Status, Integer> o1, Map.Entry<Status, Integer> o2) {
                int valueOrder = o2.getValue().compareTo(o1.getValue());
                if (valueOrder != 0) {
                    return valueOrder;
                }
                else {
                    // if values are the same keep the same order as implemented in Status
                    int colorOrder = o1.getKey().compareTo(o2.getKey());
                    return colorOrder;
                }
            }
        });


        List<String> keys = new ArrayList<>();
        for (Map.Entry<Status, Integer> entry : list) {
            keys.add(entry.getKey().color);
        }
        return keys;
    }

    public static String generateTagChartData(List<TagObject> tagObjectList) {
    	StringBuilder buffer = new StringBuilder();
        for (TagObject tag : tagObjectList) {
            buffer.append("[[");
            buffer.append(tag.getNumberOfPasses());
            buffer.append(",");
            buffer.append(tag.getNumberOfFailures());
            buffer.append(",");
            buffer.append(tag.getNumberOfSkipped());
            buffer.append(",");
            buffer.append(tag.getNumberOfPending());
            buffer.append("],");
            buffer.append("{label:'").append(tag.getTagName()).append("'}],");
        }
        return buffer.toString();
    }

    public static String getTags(List<TagObject> tagObjectList) {
        StringBuilder tags = new StringBuilder();

        if (!tagObjectList.isEmpty()) {
            for (TagObject tag : tagObjectList) {
                tags.append("'").append(tag.getTagName()).append("',");
            }

            tags.setLength(tags.length() - 1);
        }
        return "[" + tags.toString() + "]";
    }

    public static String generateTagChartDataForHighCharts(List<TagObject> tagObjectList) {
    	StringBuilder buffer = new StringBuilder();

        if (!tagObjectList.isEmpty()) {
            for (TagObject tag : tagObjectList) {
                // TODO: could be merged with generateTagChartData
                buffer.append("[");
                buffer.append(tag.getNumberOfPasses());
                buffer.append(",");
                buffer.append(tag.getNumberOfFailures());
                buffer.append(",");
                buffer.append(tag.getNumberOfSkipped());
                buffer.append(",");
                buffer.append(tag.getNumberOfPending());
                buffer.append("]");
                buffer.append(",");
            }

            buffer.setLength(buffer.length() - 1);
        }

        return "[" + buffer.toString() + "]";
    }



}
