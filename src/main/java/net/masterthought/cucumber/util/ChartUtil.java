package net.masterthought.cucumber.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.masterthought.cucumber.json.support.TagObject;

public final class ChartUtil {

    public static String getTags(List<TagObject> tagObjectList) {
        List<String> tagNames = new ArrayList<>();

        for (TagObject tagObject : tagObjectList) {
            tagNames.add(StringUtils.wrap(tagObject.getName(), "'"));
        }
        return "[" + StringUtils.join(tagNames, ",") + "]";
    }

    public static String generateTagChartDataForHighCharts(List<TagObject> tagObjectList) {
        List<String> buffers = new ArrayList<>();

        for (TagObject tag : tagObjectList) {
            buffers.add(String.format("[%d, %d, %d, %d]", tag.getPassedSteps(), tag.getFailedSteps(),
                    tag.getSkippedSteps(), tag.getPendingSteps()));
        }
        return "[" + StringUtils.join(buffers, ",") + "]";
    }
}
