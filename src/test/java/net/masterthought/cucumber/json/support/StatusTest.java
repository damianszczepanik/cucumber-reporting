package net.masterthought.cucumber.json.support;

import org.junit.Test;

import static net.masterthought.cucumber.json.support.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StatusTest {

    @Test
    public void valuesOf_ReturnsOrderedStatuses() {

        // given
        // tables displays result with following order
        final Status[] reference = { PASSED, FAILED, SKIPPED, PENDING, UNDEFINED };

        // when
        Status[] orderedStatuses = Status.values();

        // then
        assertThat(orderedStatuses).isEqualTo(reference);
    }

    @Test
    public void getName_ReturnsNameAsLowerCase() {

        // given
        final Status status = PASSED;
        final String refName = "passed";
        
        // when
        String rawName = status.getRawName();

        // then
        assertThat(rawName).isEqualTo(refName);
    }

    @Test
    public void getLabel_ReturnsNameStartingFromUpperCase() {

        // given
        final Status status = UNDEFINED;
        final String refLabel = "Undefined";

        // when
        String label = status.getLabel();

        // then
        assertThat(label).isEqualTo(refLabel);
    }

    @Test
    public void hasPassed_ReturnsTrueForPASSED() {

        // given
        Status status = PASSED;

        // when
        boolean isPassed = status.isPassed();

        // then
        assertThat(isPassed).isTrue();
    }

    @Test
    public void hasPassed_ReturnsFalseForNoPASSED() {

        // given
        Status[] notPassed = {FAILED, SKIPPED, PENDING, UNDEFINED};

        for (Status status : notPassed) {
            // when
            boolean isPassed = status.isPassed();

            // then
            assertThat(isPassed).isFalse();
        }
    }
}
