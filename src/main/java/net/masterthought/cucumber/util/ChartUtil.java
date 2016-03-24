package net.masterthought.cucumber.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.masterthought.cucumber.json.support.TagObject;

public final class ChartUtil {

    public static String getTags(List<TagObject> tagObjectList) {

        String[] tagNames = new String[tagObjectList.size()];
        for (int i = 0; i < tagNames.length; i++) {
            tagNames[i] = StringUtils.wrap(tagObjectList.get(i).getRawName(), "'");
        }

        return "[" + StringUtils.join(tagNames, ",") + "]";
    }

    public static String generateTagChartDataForHighCharts(List<TagObject> tagObjectList) {
    	StringBuilder buffer = new StringBuilder();

        if (!tagObjectList.isEmpty()) {
            for (TagObject tag : tagObjectList) {
                buffer.append("[");
                buffer.append(tag.getPassedSteps());
                buffer.append(",");
                buffer.append(tag.getFailedSteps());
                buffer.append(",");
                buffer.append(tag.getSkippedSteps());
                buffer.append(",");
                buffer.append(tag.getPendingSteps());
                buffer.append("]");
                buffer.append(",");
            }

            buffer.setLength(buffer.length() - 1);
        }

        return "[" + buffer.toString() + "]";
    }
}
