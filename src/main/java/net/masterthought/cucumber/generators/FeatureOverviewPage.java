package net.masterthought.cucumber.generators;

import java.util.Arrays;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.support.Status;

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

        contextMap.put("scenario_data", Arrays.asList(Status.PASSED.color, Status.FAILED.color));

        contextMap.put("all_durations", report.getAllDurationsAsString());
        contextMap.put("parallel", configuration.isParallelTesting());

        super.generateReport("feature-overview.html");
    }
}
