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

            contextMap.putAll(getGeneralParameters());
            contextMap.put("parallel", configuration.isParallelTesting());
            contextMap.put("feature", feature);
            contextMap.put("status_colour", feature.getStatus().color);
            contextMap.put("elements", feature.getElements());

            generateReport(feature.getReportFileName());
        }
    }

}
