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
        velocityContext.put("all_features", report.getAllFeatures());
        velocityContext.put("report_summary", report.getFeatureReport());
        velocityContext.put("all_features_passed", report.getAllPassedFeatures());
        velocityContext.put("all_features_failed", report.getAllFailedFeatures());

        velocityContext.put("parallel", configuration.isParallelTesting());
    }
}
