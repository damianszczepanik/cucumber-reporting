package net.masterthought.cucumber.util;

import java.util.List;

import net.masterthought.cucumber.json.support.TagObject;

public final class ChartUtil {

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
