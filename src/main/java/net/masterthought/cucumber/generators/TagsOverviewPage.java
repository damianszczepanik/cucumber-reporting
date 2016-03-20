package net.masterthought.cucumber.generators;

import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.ChartUtil;
import net.masterthought.cucumber.util.Util;

public class TagsOverviewPage extends AbstractPage {

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
        velocityContext.put("all_tags", tags);
        velocityContext.put("all_tags_scenarios", report.getAllTagScenarios());
        velocityContext.put("all_tags_passed_scenarios", report.getAllPassedTagScenarios());
        velocityContext.put("all_tags_failed_scenarios", report.getAllFailedTagScenarios());
        velocityContext.put("all_tags_steps", report.getAllTagSteps());
        velocityContext.put("all_tags_passes", report.getAllPassesTags());
        velocityContext.put("all_tags_failed", report.getAllFailsTags());
        velocityContext.put("all_tags_skipped", report.getAllSkippedTags());
        velocityContext.put("all_tags_pending", report.getAllPendingTags());
        velocityContext.put("all_tags_undefined", report.getAllUndefinedTags());
        velocityContext.put("all_tags_missing", report.getAllMissingTags());

        velocityContext.put("chart_categories", ChartUtil.getTags(tags));
        velocityContext.put("chart_data", ChartUtil.generateTagChartDataForHighCharts(tags));
        velocityContext.put("all_durations", Util.formatDuration(report.getAllTagDuration()));
    }
}
