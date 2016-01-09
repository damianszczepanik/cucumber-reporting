package net.masterthought.cucumber.json.support;

import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.MISSING;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.json.support.Status.PENDING;
import static net.masterthought.cucumber.json.support.Status.SKIPPED;
import static net.masterthought.cucumber.json.support.Status.UNDEFINED;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StatusTest {

    @Test
    public void valuesReturnsOrderUsedByFrontEnd() {

        // given
        final Status[] reference = { PASSED, FAILED, SKIPPED, PENDING, UNDEFINED, MISSING };

        // then
        assertArrayEquals(reference, Status.values());
    }

    @Test
    public void getOrderedColorsReturnsColorsInDeclaredOrder() {

        // given
        final Status[] reference = { PASSED, FAILED, SKIPPED, PENDING, UNDEFINED, MISSING };

        // when
        String[] colors = Status.getOrderedColors();

        // then
        for (int i = 0; i < reference.length; i++) {
            assertEquals(reference[i].color, colors[i]);
        }
    }

    @Test
    public void getNameReturnsNameToLowerCase() {

        // given
        final String statusName = "PASSED";
        final String name = "passed";
        
        // when
        Status status = Status.valueOf(statusName);

        // then
        assertEquals(status.getName(), name);
    }

    @Test
    public void getLabelReturnsNameStartingFRomUpperCase() {

        // given
        final String statusName = "PASSED";
        final String label = "Passed";

        // when
        Status status = Status.valueOf(statusName);

        // then
        assertEquals(status.getLabel(), label);
    }
}
