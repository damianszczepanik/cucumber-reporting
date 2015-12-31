package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.List;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.charts.FlashChartBuilder;
import net.masterthought.cucumber.charts.JsChartUtil;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.Util;

public class TagOverviewPage extends AbstractPage {

    public TagOverviewPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "tagOverview.vm");
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        List<TagObject> tags = reportInformation.getAllTags();
        contextMap.put("all_tags", tags);
        contextMap.put("all_tags_scenarios", reportInformation.getAllTagScenarios());
        contextMap.put("all_tags_passed_scenarios", reportInformation.getAllPassedTagScenarios());
        contextMap.put("all_tags_failed_scenarios", reportInformation.getAllFailedTagScenarios());
        contextMap.put("all_tags_steps", reportInformation.getAllTagSteps());
        contextMap.put("all_tags_passes", reportInformation.getAllPassesTags());
        contextMap.put("all_tags_failed", reportInformation.getAllFailsTags());
        contextMap.put("all_tags_skipped", reportInformation.getAllSkippedTags());
        contextMap.put("all_tags_pending", reportInformation.getAllPendingTags());
        contextMap.put("all_tags_undefined", reportInformation.getAllUndefinedTags());
        contextMap.put("all_tags_missing", reportInformation.getAllMissingTags());

        boolean flashCharts = this.reportBuilder.isFlashCharts();
        boolean highCharts = this.reportBuilder.isHighCharts();

        if (flashCharts) {
            contextMap.put("chart_data", FlashChartBuilder.generateStackedColumnChart(tags));
        } else {
            if (highCharts) {
                contextMap.put("chart_categories", JsChartUtil.getTags(tags));
                contextMap.put("chart_data", JsChartUtil.generateTagChartDataForHighCharts(tags));
            } else {
                contextMap.put("chart_rows", JsChartUtil.generateTagChartData(tags));
            }
        }
        contextMap.put("all_durations", Util.formatDuration(reportInformation.getAllTagDuration()));
        contextMap.put("flash_charts", flashCharts);
        contextMap.put("high_charts", highCharts);

        super.generateReport("tag-overview.html");
    }
}
