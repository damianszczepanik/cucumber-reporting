package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.ChartUtil;
import net.masterthought.cucumber.util.Util;

public class TagOverviewPage extends AbstractPage {

    public TagOverviewPage(ReportBuilder reportBuilder, Configuration configuration) {
        super(reportBuilder, "tagOverview.vm", configuration);
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        List<TagObject> tags = report.getAllTags();
        contextMap.put("all_tags", tags);
        contextMap.put("all_tags_scenarios", report.getAllTagScenarios());
        contextMap.put("all_tags_passed_scenarios", report.getAllPassedTagScenarios());
        contextMap.put("all_tags_failed_scenarios", report.getAllFailedTagScenarios());
        contextMap.put("all_tags_steps", report.getAllTagSteps());
        contextMap.put("all_tags_passes", report.getAllPassesTags());
        contextMap.put("all_tags_failed", report.getAllFailsTags());
        contextMap.put("all_tags_skipped", report.getAllSkippedTags());
        contextMap.put("all_tags_pending", report.getAllPendingTags());
        contextMap.put("all_tags_undefined", report.getAllUndefinedTags());
        contextMap.put("all_tags_missing", report.getAllMissingTags());

        contextMap.put("chart_categories", ChartUtil.getTags(tags));
        contextMap.put("chart_data", ChartUtil.generateTagChartDataForHighCharts(tags));
        contextMap.put("all_durations", Util.formatDuration(report.getAllTagDuration()));

        super.generateReport("tag-overview.html");
    }
}
