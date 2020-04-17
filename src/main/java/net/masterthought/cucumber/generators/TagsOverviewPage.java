package net.masterthought.cucumber.generators;

import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = "overview-tags.html";

    public TagsOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "overviewTags.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void prepareReport() {
        List<TagObject> tags = reportResult.getAllTags();
        context.put("all_tags", tags);
        context.put("report_summary", reportResult.getTagReport());

        context.put("chart_categories", generateTagLabels(tags));
        context.put("chart_data", generateTagValues(tags));
    }

    static String[] generateTagLabels(List<TagObject> tagsObjectList) {
        int tagCount = tagsObjectList.size();
        String[] tagNames = new String[tagCount];

        for (int i = 0; i < tagCount; i++) {
            tagNames[i] = tagsObjectList.get(i).getName();
        }
        return tagNames;
    }


    static String[][] generateTagValues(List<TagObject> tagsObjectList) {
        int tagsCount = tagsObjectList.size();
        String[][] values = new String[Status.values().length][tagsCount];
        for (int i = 0; i < tagsCount; i++) {
            final TagObject tagObject = tagsObjectList.get(i);
            final int allSteps = tagObject.getSteps();
            values[0][i] = Util.formatAsDecimal(tagObject.getPassedSteps(), allSteps);
            values[1][i] = Util.formatAsDecimal(tagObject.getFailedSteps(), allSteps);
            values[2][i] = Util.formatAsDecimal(tagObject.getSkippedSteps(), allSteps);
            values[3][i] = Util.formatAsDecimal(tagObject.getPendingSteps(), allSteps);
            values[4][i] = Util.formatAsDecimal(tagObject.getUndefinedSteps(), allSteps);
        }

        return values;
    }
}
