package net.masterthought.cucumber.generators;

import mockit.Deencapsulation;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.support.Status;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

public class FailureOverviewPageTest extends PageTest {
    private List<Element> testFailReport(String jsonToTest, int expectedFailedScenarios) {
        List<Element> failures = new ArrayList<>();

        // given
        setUpWithJson(jsonToTest);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        Object failsFromContext = context.get("failures");

        assertThat(failsFromContext)
                .isNotNull()
                .isInstanceOf(failures.getClass())
                .asList()
                .hasSize(expectedFailedScenarios);

        //noinspection unchecked
        failures = (List<Element>) failsFromContext;

        for (Element failure : failures) {
            assertFalse(failure.getElementStatus().isPassed());
        }

        return failures;
    }

    @Test
    public void shouldBeEmptyForPassedTest() {
        testFailReport(SIMPLE_JSON, 0);
    }

    @Test
    public void shouldHaveFailedScenariosForFailedTests() {
        List<Element> failures = testFailReport(FAILURES_JSON, 2);

        // first scenario is before failure, causing steps to be skipped, which
        // is considered a failure with the configuration PageTest uses
        assertThat(failures.get(0).getBeforeStatus()).isEqualTo(Status.FAILED);
        assertThat(failures.get(0).getStepsStatus()).isEqualTo(Status.SKIPPED);

        // second scenario is step failure
        assertThat(failures.get(1).getBeforeStatus()).isEqualTo(Status.PASSED);
        assertThat(failures.get(1).getStepsStatus()).isEqualTo(Status.FAILED);
    }
}
