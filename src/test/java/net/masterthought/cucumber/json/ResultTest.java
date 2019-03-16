package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ResultTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getStatus_ReturnsStatus() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[1].getResult();

        // when
        Status status = result.getStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    public void getDuration_ReturnsDuration() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[3].getResult();

        // when
        long duration = result.getDuration();

        // then
        assertThat(duration).isEqualTo(111111L);
    }

    @Test
    public void getFormattedDuration_ReturnsDurationAsString() {

        // give
        Result result = features.get(0).getElements()[0].getSteps()[2].getResult();

        // when
        String duration = result.getFormattedDuration();

        // then
        assertThat(duration).isEqualTo("0.007");
    }

    @Test
    public void getErrorMessage_ReturnsErrorMessage() {

        // given
        Result result = features.get(1).getElements()[0].getSteps()[5].getResult();

        // when
        String errorMessage = result.getErrorMessage();

        // then
        assertThat(errorMessage).isEqualTo("java.lang.AssertionError: \n" +
                "Expected: is <80>\n" +
                "     got: <90>\n" +
                "\n" +
                "\tat org.junit.Assert.assertThat(Assert.java:780)\n" +
                "\tat org.junit.Assert.assertThat(Assert.java:738)\n" +
                "\tat net.masterthought.example.ATMScenario.checkBalance(ATMScenario.java:69)\n" +
                "\tat ✽.And the account balance should be 90(net/masterthought/example/ATMK.feature:12)\n");
    }

    @Test
    public void getErrorMessageTitle_OnEmptyMessage_ReturnsEmptyTitle() {

        // given
        Result result = features.get(1).getElements()[0].getBefore()[1].getResult();

        // when
        String messageTitle = result.getErrorMessageTitle();

        // then
        assertThat(messageTitle).isEmpty();
    }

    @Test
    public void getErrorMessageTitle_OnNullMessage_ReturnsEmptyTitle() {

        // given
        Result result = features.get(0).getElements()[0].getSteps()[0].getResult();

        // when
        String messageTitle = result.getErrorMessageTitle();

        // then
        assertThat(messageTitle).isEmpty();
    }
}
