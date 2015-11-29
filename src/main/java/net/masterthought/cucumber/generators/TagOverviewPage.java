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

        List<TagObject> tags = this.reportInformation.getTags();
        contextMap.put("tags", tags);
        contextMap.put("total_scenarios", reportInformation.getTotalTagScenarios());
        contextMap.put("total_passed_scenarios", reportInformation.getTotalTagScenariosPassed());
        contextMap.put("total_failed_scenarios", reportInformation.getTotalTagScenariosFailed());
        contextMap.put("total_steps", reportInformation.getTotalTagSteps());
        contextMap.put("total_passes", reportInformation.getTotalTagPasses());
        contextMap.put("total_fails", reportInformation.getTotalTagFails());
        contextMap.put("total_skipped", reportInformation.getTotalTagSkipped());
        contextMap.put("total_pending", reportInformation.getTotalTagPending());
        contextMap.put("total_undefined", reportInformation.getTotalTagUndefined());
        contextMap.put("total_missing", reportInformation.getTotalTagMissing());

        boolean flashCharts = this.reportBuilder.isFlashCharts();
        boolean highCharts = this.reportBuilder.isHighCharts();

        contextMap.put("backgrounds", reportInformation.getBackgroundInfo());
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
        contextMap.put("total_duration", Util.formatDuration(reportInformation.getTotalTagDuration()));
        contextMap.put("flash_charts", flashCharts);
        contextMap.put("high_charts", highCharts);
        long durationl = reportInformation.getBackgroundInfo().getTotalDuration()
                + reportInformation.getTotalTagDuration();
        String duration = Util.formatDuration(durationl);
        contextMap.put("total_duration", duration);

        super.generateReport("tag-overview.html");
    }
}
