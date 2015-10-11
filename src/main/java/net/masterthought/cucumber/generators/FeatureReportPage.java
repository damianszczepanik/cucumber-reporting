package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.Feature;

public class FeatureReportPage extends AbstractPage {

    public FeatureReportPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "featureReport.vm");
    }

    @Override
    public void generatePage() throws IOException {
        for (Map.Entry<String, List<Feature>> pairs : reportInformation.getFeatureMap().entrySet()) {
            List<Feature> featureList = pairs.getValue();

            for (Feature feature : featureList) {
                super.generatePage();

                contextMap.putAll(getGeneralParameters());
                contextMap.put("parallel", ReportBuilder.isParallel());
                contextMap.put("feature", feature);
                contextMap.put("report_status_colour", reportInformation.getReportStatusColour(feature));
                contextMap.put("scenarios", feature.getScenarios());
                contextMap.put("artifactsEnabled", ConfigurationOptions.instance().artifactsEnabled());

                generateReport(feature.getFileName());
            }
        }
    }

}
