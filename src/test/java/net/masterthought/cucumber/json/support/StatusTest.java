package net.masterthought.cucumber.json.support;

import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.json.support.Status.PENDING;
import static net.masterthought.cucumber.json.support.Status.SKIPPED;
import static net.masterthought.cucumber.json.support.Status.UNDEFINED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class StatusTest {

    @Test
    void valuesOf_ReturnsOrderedStatuses() {

        // given
        // tables displays result with following order
        final Status[] reference = { PASSED, FAILED, SKIPPED, PENDING, UNDEFINED };

        // when
        Status[] orderedStatuses = Status.values();

        // then
        assertThat(orderedStatuses).isEqualTo(reference);
    }

    @Test
    void getName_ReturnsNameAsLowerCase() {

        // given
        final Status status = PASSED;
        final String refName = "passed";
        
        // when
        String rawName = status.getRawName();

        // then
        assertThat(rawName).isEqualTo(refName);
    }

    @Test
    void getLabel_ReturnsNameStartingFromUpperCase() {

        // given
        final Status status = UNDEFINED;
        final String refLabel = "Undefined";

        // when
        String label = status.getLabel();

        // then
        assertThat(label).isEqualTo(refLabel);
    }

    @Test
    void isPassed_ReturnsTrueForPASSEDStatus() {

        // given
        Status status = PASSED;

        // when
        boolean isPassed = status.isPassed();

        // then
        assertThat(isPassed).isTrue();
    }

    @Test
    void hasPassed_ReturnsFalseForNoPASSED() {

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
