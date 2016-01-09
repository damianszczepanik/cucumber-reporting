package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.charts.FlashChartBuilder;
import net.masterthought.cucumber.json.support.Status;

public class FeatureOverviewPage extends AbstractPage {

    public FeatureOverviewPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "featureOverview.vm");
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        contextMap.put("all_features", reportInformation.getAllFeatures());
        contextMap.put("all_steps", reportInformation.getStepsCounter());
        contextMap.put("all_steps_passes", reportInformation.getAllPassedSteps());
        contextMap.put("all_steps_failed", reportInformation.getAllFailedSteps());
        contextMap.put("all_steps_skipped", reportInformation.getAllSkippedSteps());
        contextMap.put("all_steps_pending", reportInformation.getPendingStepsl());
        contextMap.put("all_steps_undefined", reportInformation.getUndefinedSteps());
        contextMap.put("all_steps_missing", reportInformation.getTotalStepsMissing());

        contextMap.put("all_scenarios", reportInformation.getAllScenarios().size());
        contextMap.put("all_scenarios_passed", reportInformation.getAllPassedScenarios());
        contextMap.put("all_scenarios_failed", reportInformation.getAllFailedScenarios());
        if (reportBuilder.isFlashCharts()) {
            contextMap.put("step_data", FlashChartBuilder.getStepsChart(reportInformation.getAllPassedSteps(),
                    reportInformation.getAllFailedSteps(), reportInformation.getAllSkippedSteps(),
                    reportInformation.getPendingStepsl(), reportInformation.getUndefinedSteps(),
                    reportInformation.getTotalStepsMissing()));
            contextMap.put("scenario_data", FlashChartBuilder.pieScenariosChart(
                    reportInformation.getAllPassedScenarios(), reportInformation.getAllFailedScenarios()));
        } else {
            contextMap.put("step_data", Arrays.asList(Status.getOrderedColors()));
            List<String> scenarioColours = Arrays.asList(Status.PASSED.color, Status.FAILED.color);
            contextMap.put("scenario_data", scenarioColours);
        }
        contextMap.put("all_durations", reportInformation.getAllDurationsAsString());
        contextMap.put("flash_charts", reportBuilder.isFlashCharts());
        contextMap.put("high_charts", reportBuilder.isHighCharts());
        contextMap.put("parallel", ReportBuilder.isParallel());

        super.generateReport("feature-overview.html");
    }
}
