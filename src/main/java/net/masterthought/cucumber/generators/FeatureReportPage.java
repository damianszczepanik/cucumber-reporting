package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.presentation.PresentationMode;

public class FeatureReportPage extends AbstractPage {

    private final Feature feature;

    public FeatureReportPage(ReportResult reportResult, Configuration configuration, Feature feature) {
        super(reportResult, "reportFeature.vm", configuration);
        this.feature = feature;
    }

    @Override
    public String getWebPage() {
        return feature.getReportFileName();
    }

    @Override
    public void prepareReport() {
        context.put("feature", feature);
        context.put("parallel_testing", configuration.containsPresentationMode(PresentationMode.PARALLEL_TESTING));
    }

}
