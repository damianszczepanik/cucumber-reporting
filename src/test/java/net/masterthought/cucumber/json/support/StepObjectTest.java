package net.masterthought.cucumber.json.support;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Park (midopa@github)
 */
public class StepObjectTest {

    @Test
    public void averageDurationsReturnsTime() {
        // given
        StepObject stepObj = new StepObject("Test step location");
        stepObj.addDuration(100L, "passed");
        stepObj.addDuration(200L, "failed");
        stepObj.addDuration(300L, "undefined");

        // when
        long avgDuration = stepObj.getAverageDuration();

        // then
        assertEquals(avgDuration, (100L + 200L + 300L)/3);
    }
}
