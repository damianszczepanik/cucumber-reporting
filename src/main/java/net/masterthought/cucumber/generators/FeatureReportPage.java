package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    public FeatureReportPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "featureReport.vm", configuration);
    }

    @Override
    public void generatePage() {
        for (Feature feature : report.getAllFeatures()) {
            super.generatePage();

            velocityContext.put("parallel", configuration.isParallelTesting());
            velocityContext.put("feature", feature);
            velocityContext.put("elements", feature.getElements());

            generateReport(feature.getReportFileName());
        }
    }

}
