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
        page = new StepsOverviewPage();

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(StepsOverviewPage.WEB_PAGE);
    }

    @Test
    public void preparePageContext_AddsCustomProperties() {

        // given
        VelocityContext pageContext = new VelocityContext();
        page = new StepsOverviewPage();

        // when
        page.preparePageContext(pageContext, configuration, reportResult);

        // then
        assertThat(pageContext.getKeys()).hasSize(4);
        assertThat(pageContext.get("all_steps")).isEqualTo(steps);

        int allOccurrences = 0;
        long allDurations = 0;
        for (StepObject stepObject : reportResult.getAllSteps()) {
            allOccurrences += stepObject.getTotalOccurrences();
            allDurations += stepObject.getDuration();
        }
        assertThat(pageContext.get("all_occurrences")).isEqualTo(allOccurrences);
        assertThat(pageContext.get("all_durations")).isEqualTo(Util.formatDuration(allDurations));
        long average = allDurations / (allOccurrences == 0 ? 1 : allOccurrences);
        assertThat(pageContext.get("all_average")).isEqualTo(Util.formatDuration(average));
    }
}
