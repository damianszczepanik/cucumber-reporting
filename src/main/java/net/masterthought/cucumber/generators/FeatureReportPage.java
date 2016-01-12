package net.masterthought.cucumber.generators;

import java.io.IOException;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    public FeatureReportPage(ReportBuilder reportBuilder, Configuration configuration) {
        super(reportBuilder, "featureReport.vm", configuration);
    }

    @Override
    public void generatePage() throws IOException {
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
