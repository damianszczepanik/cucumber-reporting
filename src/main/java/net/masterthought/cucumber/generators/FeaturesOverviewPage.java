package net.masterthought.cucumber.generators;

import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;

public class FeaturesOverviewPage extends AbstractPage {

    public static final String WEB_PAGE = ReportBuilder.HOME_PAGE;

    public FeaturesOverviewPage() {
        super("overviewFeatures.vm");
    }

    @Override
    public String getWebPage() {
        return WEB_PAGE;
    }

    @Override
    public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
        context.put("all_features", reportResult.getAllFeatures());
        context.put("report_summary", reportResult.getFeatureReport());

        context.put("classifications", configuration.getClassifications());
        context.put("parallel", configuration.isParallelTesting());
    }
}
