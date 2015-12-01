package net.masterthought.cucumber.generators;

import java.io.IOException;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    public FeatureReportPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "featureReport.vm");
    }

    @Override
    public void generatePage() throws IOException {
        for (Feature feature : reportInformation.getFeatures()) {
            super.generatePage();

            contextMap.putAll(getGeneralParameters());
            contextMap.put("parallel", ReportBuilder.isParallel());
            contextMap.put("feature", feature);
            contextMap.put("report_status_colour", feature.getStatus().color);
            contextMap.put("scenarios", feature.getScenarios());

            generateReport(feature.getReportFileName());
        }
    }

}
