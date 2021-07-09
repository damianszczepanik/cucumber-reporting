package net.masterthought.cucumber.json.support;

import static java.util.Arrays.asList;
import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.json.support.Status.PENDING;
import static net.masterthought.cucumber.json.support.Status.SKIPPED;
import static net.masterthought.cucumber.json.support.Status.UNDEFINED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
@RunWith(Parameterized.class)
public class StatusTest {

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
            { PASSED, "passed", "Passed", true },
            { FAILED, "failed", "Failed", false },
            { SKIPPED, "skipped", "Skipped", false },
            { PENDING, "pending", "Pending", false },
            { UNDEFINED, "undefined", "Undefined", false },
        });
    }

    @Parameter(0)
    public Status status;

    @Parameter(1)
    public String rawName;

    @Parameter(2)
    public String label;

    @Parameter(3)
    public boolean isPassed;

    @Test
    public void getName_ReturnsNameAsLowerCase() {
        // when
        String actualRawName = status.getRawName();

        // then
        assertThat(actualRawName).isEqualTo(this.rawName);
    }

    @Test
    public void getLabel_ReturnsNameStartingFromUpperCase() {
        // when
        String actualLabel = status.getLabel();

        // then
        assertThat(actualLabel).isEqualTo(this.label);
    }

    @Test
    public void isPassed_ReturnsPassedStatus() {
        // when
        boolean actualIsPassed = status.isPassed();

        // then
        assertThat(actualIsPassed).isEqualTo(this.isPassed);
    }

}
