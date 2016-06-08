package net.masterthought.cucumber.generators;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;

public class TagsOverviewPage extends AbstractPage {

    private static final NumberFormat DECIMAL_FORMATTER = DecimalFormat.getInstance(Locale.US);
    static {
        DECIMAL_FORMATTER.setMinimumFractionDigits(2);
        DECIMAL_FORMATTER.setMaximumFractionDigits(2);
    }

    public TagsOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "tagsOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return "tag-overview.html";
    }

    @Override
    public void prepareReport() {
        List<TagObject> tags = report.getAllTags();
        context.put("all_tags", tags);
        context.put("report_summary", report.getTagReport());

        context.put("chart_categories", generateTagLabels(tags));
        context.put("chart_data", generateTagValues(tags));
    }

    static String generateTagLabels(List<TagObject> tagsObjectList) {
        int tagCount = tagsObjectList.size();
        String[] tagNames = new String[tagCount];

        for (int i = 0; i < tagCount; i++) {
            tagNames[i] = StringUtils.wrap(tagsObjectList.get(i).getName(), "\"");
        }
        return "[" + StringUtils.join(tagNames, ",") + "]";
    }

    static List<String> generateTagValues(List<TagObject> tagsObjectList) {
        int tagsCount = tagsObjectList.size();
        String[][] values = new String[Status.values().length][tagsCount];
        for (int i = 0; i < tagsCount; i++) {
            final TagObject tagObject = tagsObjectList.get(i);
            final int allSteps = tagObject.getSteps();
            values[0][i] = format(tagObject.getPassedSteps(), allSteps);
            values[1][i] = format(tagObject.getFailedSteps(), allSteps);
            values[2][i] = format(tagObject.getSkippedSteps(), allSteps);
            values[3][i] = format(tagObject.getPendingSteps(), allSteps);
            values[4][i] = format(tagObject.getUndefinedSteps(), allSteps);
            values[5][i] = format(tagObject.getMissingSteps(), allSteps);
        }

        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < Status.values().length; i++) {
            statuses.add("[" + StringUtils.join(values[i], ',') + "]");
        }

        return statuses;
    }

    static String format(int value, int sum) {
        return DECIMAL_FORMATTER.format(100F * value / sum);
    }
}
