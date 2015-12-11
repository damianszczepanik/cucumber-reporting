package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.util.Util;

/**
 * Presents details about how long steps are executed (adds the same steps and presents sum).
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StepOverviewPage extends AbstractPage {

    public StepOverviewPage(ReportBuilder reportBuilder) {
        super(reportBuilder, "stepOverview.vm");
    }

    @Override
    public void generatePage() throws IOException {
        super.generatePage();

        contextMap.put("all_steps", sortStepsByDuration());

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : reportInformation.getTotalSteps().values()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getTotalDuration();
        }
        contextMap.put("all_occurrences", allOccurrences);
        contextMap.put("all_durations", Util.formatDuration(allDurations));
        contextMap.put("all_average", Util.formatDuration(allDurations / allOccurrences));

        super.generateReport("step-overview.html");
    }

    private StepObject[] sortStepsByDuration() {
        Map<String, StepObject> steps = reportInformation.getTotalSteps();
        StepObject[] array = new StepObject[steps.size()];
        Arrays.sort(steps.values().toArray(array), new DurationCompator());

        return array;
    }

    private static class DurationCompator implements Comparator<StepObject> {

        @Override
        public int compare(StepObject o1, StepObject o2) {
            return Long.signum(o2.getTotalDuration() - o1.getTotalDuration());
        }
    }
}
