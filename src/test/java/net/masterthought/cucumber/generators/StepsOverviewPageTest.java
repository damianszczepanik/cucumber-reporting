package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsStepsOverviewFileName() {

        // given
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(StepsOverviewPage.WEB_PAGE);
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(11);
        assertThat(context.get("all_steps")).isEqualTo(steps);

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : reportResult.getAllSteps()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getDuration();
        }
        assertThat(context.get("all_occurrences")).isEqualTo(allOccurrences);
        assertThat(context.get("all_durations")).isEqualTo(Util.formatDuration(allDurations));
        long average = allDurations / (allOccurrences == 0 ? 1 : allOccurrences);
        assertThat(context.get("all_average")).isEqualTo(Util.formatDuration(average));
    }
}
