package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;

public class FeatureOverviewPage extends AbstractPage {

    public FeatureOverviewPage(ReportResult reportResult, Configuration configuration) {
        super(reportResult, "featureOverview.vm", configuration);
    }

    @Override
    public void generatePage() {
        super.generatePage();

        contextMap.put("all_features", report.getAllFeatures());
        contextMap.put("all_steps", report.getStepsCounter());
        contextMap.put("all_steps_passes", report.getAllPassedSteps());
        contextMap.put("all_steps_failed", report.getAllFailedSteps());
        contextMap.put("all_steps_skipped", report.getAllSkippedSteps());
        contextMap.put("all_steps_pending", report.getPendingStepsl());
        contextMap.put("all_steps_undefined", report.getUndefinedSteps());
        contextMap.put("all_steps_missing", report.getTotalStepsMissing());
        contextMap.put("all_scenarios", report.getAllScenarios().size());
        contextMap.put("all_scenarios_passed", report.getAllPassedScenarios());
        contextMap.put("all_scenarios_failed", report.getAllFailedScenarios());

        contextMap.put("all_durations", report.getAllDurationsAsString());
        contextMap.put("parallel", configuration.isParallelTesting());

        super.generateReport("feature-overview.html");
    }
}
