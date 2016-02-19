package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;

public class FeatureOverviewPage extends AbstractPage {

    public FeatureOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "featureOverview.vm", configuration, "feature-overview.html");
    }

    @Override
    public void prepareReport() {
        velocityContext.put("all_features", report.getAllFeatures());
        velocityContext.put("all_steps", report.getStepsCounter());
        velocityContext.put("all_steps_passes", report.getAllPassedSteps());
        velocityContext.put("all_steps_failed", report.getAllFailedSteps());
        velocityContext.put("all_steps_skipped", report.getAllSkippedSteps());
        velocityContext.put("all_steps_pending", report.getPendingStepsl());
        velocityContext.put("all_steps_undefined", report.getUndefinedSteps());
        velocityContext.put("all_steps_missing", report.getTotalStepsMissing());
        velocityContext.put("all_scenarios", report.getAllScenarios().size());
        velocityContext.put("all_scenarios_passed", report.getAllPassedScenarios());
        velocityContext.put("all_scenarios_failed", report.getAllFailedScenarios());

        velocityContext.put("all_durations", report.getAllDurationsAsString());
        velocityContext.put("parallel", configuration.isParallelTesting());
    }
}
