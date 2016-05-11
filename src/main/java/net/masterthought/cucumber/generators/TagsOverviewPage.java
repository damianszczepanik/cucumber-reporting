package net.masterthought.cucumber.generators;

import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.ChartUtil;

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
        context.put("all_tags", tags);
        context.put("report_summary", report.getTagReport());

        context.put("chart_categories", ChartUtil.getTags(tags));
        context.put("chart_data", ChartUtil.generateTagChartDataForHighCharts(tags));
    }
}
