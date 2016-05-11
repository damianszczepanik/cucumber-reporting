package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;

public class FeaturesOverviewPage extends AbstractPage {

    public FeaturesOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "featuresOverview.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return ReportBuilder.HOME_PAGE;
    }

    @Override
    public void prepareReport() {
        context.put("all_features", report.getAllFeatures());
        context.put("report_summary", report.getFeatureReport());
        context.put("all_features_passed", report.getAllPassedFeatures());
        context.put("all_features_failed", report.getAllFailedFeatures());

        context.put("parallel", configuration.isParallelTesting());
    }
}
