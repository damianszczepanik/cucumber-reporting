package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    private final Feature feature;

    public FeatureReportPage(ReportResult reportResult, Configuration configuration, Feature feature) {
        super(reportResult, "featureReport.vm", configuration);
        this.feature = feature;
    }

    @Override
    public String getWebPage() {
        return feature.getReportFileName();
    }

    @Override
    public void prepareReport() {
        velocityContext.put("parallel", configuration.isParallelTesting());
        velocityContext.put("feature", feature);
        velocityContext.put("elements", feature.getElements());
    }

}
