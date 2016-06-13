package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;

public class FailuresOverviewPage extends AbstractPage {

    public FailuresOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "failuresOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return "failures-overview.html";
    }

    @Override
    public void prepareReport() {
        context.put("all_features", report.getAllFeatures());
    }
}
