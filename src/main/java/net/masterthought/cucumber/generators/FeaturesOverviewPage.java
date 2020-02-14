package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.presentation.PresentationMode;

public class FeaturesOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = ReportBuilder.HOME_PAGE;

    public FeaturesOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "overviewFeatures.vm", configuration);
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void prepareReport() {
        context.put("all_features", reportResult.getAllFeatures());
        context.put("report_summary", reportResult.getFeatureReport());

        context.put("parallel_testing", configuration.containsPresentationMode(PresentationMode.PARALLEL_TESTING));
        context.put("classifications", configuration.getClassifications());
    }
}
