package net.masterthought.cucumber.generators;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

        contextMap.put("all_steps", sortStepsByDuration(reportInformation.getAllSteps()));

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : reportInformation.getAllSteps()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getTotalDuration();
        }
        contextMap.put("all_occurrences", allOccurrences);
        contextMap.put("all_durations", Util.formatDuration(allDurations));
        // make sure it does not divide by 0 - may happens if there is no step at all or all results have 0 ms durations
        long average = allDurations / (allOccurrences == 0 ? 1 : allOccurrences);
        contextMap.put("all_average", Util.formatDuration(average));

        super.generateReport("step-overview.html");
    }

    private StepObject[] sortStepsByDuration(List<StepObject> steps) {
        StepObject[] array = new StepObject[steps.size()];
        Arrays.sort(steps.toArray(array), new DurationCompator());

        return array;
    }

    private static class DurationCompator implements Comparator<StepObject> {

        @Override
        public int compare(StepObject o1, StepObject o2) {
            return Long.signum(o2.getTotalDuration() - o1.getTotalDuration());
        }
    }
}
