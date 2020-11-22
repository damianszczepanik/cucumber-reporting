package net.masterthought.cucumber.json.support;

import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.json.support.Status.PENDING;
import static net.masterthought.cucumber.json.support.Status.SKIPPED;
import static net.masterthought.cucumber.json.support.Status.UNDEFINED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
public class StatusOrderTest {

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

}
