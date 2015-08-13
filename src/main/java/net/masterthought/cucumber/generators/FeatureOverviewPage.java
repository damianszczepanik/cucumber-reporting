package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.List;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.charts.FlashChartBuilder;
import net.masterthought.cucumber.charts.JsChartUtil;

public class FeatureOverviewPage extends AbstractPage {

    public FeatureOverviewPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "featureOverview.vm");
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        contextMap.put("features", reportInformation.getFeatures());
        contextMap.put("parallel", ReportBuilder.isParallel());
        contextMap.put("total_features", reportInformation.getTotalFeatures());

        contextMap.put("total_steps", reportInformation.getTotalSteps());
        contextMap.put("total_passes", reportInformation.getTotalStepsPassed());
        contextMap.put("total_fails", reportInformation.getTotalStepsFailed());
        contextMap.put("total_skipped", reportInformation.getTotalStepsSkipped());
        contextMap.put("total_pending", reportInformation.getTotalStepsPending());
        contextMap.put("total_undefined", reportInformation.getTotalStepsUndefined());
        contextMap.put("total_missing", reportInformation.getTotalStepsMissing());

        contextMap.put("scenarios_passed", reportInformation.getTotalScenariosPassed());
        contextMap.put("scenarios_failed", reportInformation.getTotalScenariosFailed());
        contextMap.put("total_scenarios", reportInformation.getTotalScenarios());
        if (reportBuilder.isFlashCharts()) {
            contextMap.put("step_data", FlashChartBuilder.getStepsChart(reportInformation.getTotalStepsPassed(),
                    reportInformation.getTotalStepsFailed(), reportInformation.getTotalStepsSkipped(),
                    reportInformation.getTotalStepsPending(), reportInformation.getTotalStepsUndefined(),
                    reportInformation.getTotalStepsMissing()));
            contextMap.put(
                    "scenario_data",
                    FlashChartBuilder.pieScenariosChart(reportInformation.getTotalScenariosPassed(),
                            reportInformation.getTotalScenariosFailed()));
        } else {
            JsChartUtil pie = new JsChartUtil();
            List<String> stepColours = pie.orderStepsByValue(reportInformation.getTotalStepsPassed(),
                    reportInformation.getTotalStepsFailed(), reportInformation.getTotalStepsSkipped(),
                    reportInformation.getTotalStepsPending(), reportInformation.getTotalStepsUndefined(),
                    reportInformation.getTotalStepsMissing());
            contextMap.put("step_data", stepColours);
            List<String> scenarioColours = pie.orderScenariosByValue(reportInformation.getTotalScenariosPassed(),
                    reportInformation.getTotalScenariosFailed());
            contextMap.put("scenario_data", scenarioColours);
        }
        contextMap.put("total_duration", reportInformation.getTotalDurationAsString());
        contextMap.put("flashCharts", reportBuilder.isFlashCharts());
        contextMap.put("highCharts", reportBuilder.isHighCharts());

        super.generateReport("feature-overview.html");
    }
}
