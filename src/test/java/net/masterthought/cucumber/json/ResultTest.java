package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertThat(duration).isEqualTo("007ms");
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
    public void getErrorMessageTitle_ReturnsErrorMessageTitle() {

        // given
        Result result = features.get(1).getElements()[0].getSteps()[5].getResult();

        // when
        String errorMessageTitle = result.getErrorMessageTitle();

        // then
        assertThat(errorMessageTitle).isEqualTo("java.lang.AssertionError: ");
    }

    @Test
    public void hashCode_ReturnHashCode() throws Exception {
        Result result = features.get(0).getElements()[0].getSteps()[1].getResult();
        assertEquals(-73678307, result.hashCode());
    }

    @Test
    public void equals_ReturnTrueSameInstance() throws Exception {
        Result result1 = features.get(0).getElements()[0].getSteps()[1].getResult();
        Result result2 = result1;
        assertTrue(result1.equals(result2));
    }

    @Test
    public void equals_ReturnTrueSameValue() throws Exception {
        Result result1 = features.get(0).getElements()[0].getSteps()[1].getResult();
        Result result2 = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result2);
        TestUtils.setFieldViaReflection("duration", 9520000l, result2);
        assertTrue(result1.equals(result2));
    }

    @Test
    public void equals_ReturnFalseNotSameValue() throws Exception {
        Result result1 = features.get(0).getElements()[0].getSteps()[1].getResult();
        Result result2 = new Result();
        TestUtils.setFieldViaReflection("status", Status.FAILED, result2);
        TestUtils.setFieldViaReflection("duration", 9520666l, result2);
        assertFalse(result1.equals(result2));
    }

    @Test
    public void equals_ReturnFalseNotAnInstanceOf() throws Exception {
        Result result = features.get(0).getElements()[0].getSteps()[1].getResult();
        assertFalse(result.equals(new Step()));
    }

}
