package net.masterthought.cucumber.generators;

import org.apache.velocity.VelocityContext;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    private final Feature feature;

    public FeatureReportPage(Feature feature) {
        super("reportFeature.vm");
        this.feature = feature;
    }

    @Override
    public String getWebPage() {
        return feature.getReportFileName();
    }

    @Override
    public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
        context.put("parallel", configuration.isParallelTesting());
        context.put("feature", feature);
    }

}
