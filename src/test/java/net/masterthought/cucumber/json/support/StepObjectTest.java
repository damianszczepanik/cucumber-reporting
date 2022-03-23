package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.ValidationException;

/**
 * @author Sam Park (midopa@github)
 */
public class StepObjectTest {

    private StepObject stepObject;

    @Before
    public void setUp() {
        stepObject = new StepObject("Test step location");
        stepObject.addDuration(1000000000L, Status.PASSED);
        stepObject.addDuration(2200000000L, Status.FAILED);
        stepObject.addDuration( 303000000L, Status.UNDEFINED);
    }

    @Test
    public void StepObject_OnNullLocation_ThrowsException() {

        // given
        String location = null;

        // when & then
        assertThatThrownBy(() -> new StepObject(location))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void getLocation_ReturnsLocation() {

        // given
        // from @Before

        // when
        String location = stepObject.getLocation();

        // then
        assertThat(location).isEqualTo("Test step location");
    }

    @Test
    public void addDuration_ReturnsSumsDurations() {

        // give
        StepObject step = new StepObject("ble bla ble");

        // when
        step.addDuration(20L, Status.PASSED);
        step.addDuration(5L, Status.PASSED);
        step.addDuration(700L, Status.UNDEFINED);

        // then
        assertThat(step.getDuration()).isEqualTo(725L);
        assertThat(step.getTotalOccurrences()).isEqualTo(3);
        assertThat(step.getStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    public void getFormattedTotalDuration_ReturnsFormattedSumDurations() {

        // give
        // from @Before

        // when
        String duration = stepObject.getFormattedTotalDuration();

        // then
        assertThat(duration).isEqualTo("3.503");
    }

    @Test
    public void getFormattedMaxDuration_ReturnsFormattedMaxDurations() {

        // give
        // from @Before

        // when
        String duration = stepObject.getFormattedTotalDuration();

        // then
        assertThat(duration).isEqualTo("3.503");
    }

    @Test
    public void getFormattedMaxDuration_ReturnsdMaxDurations() {

        // give
        // from @Before

        // when
        long maxDuration = stepObject.getMaxDuration();

        // then
        assertThat(maxDuration).isEqualTo(2200000000L);
    }

    @Test
    public void getAverageDurations_ReturnsTime() {

        // given
        // from @Before

        // when
        long avgDuration = stepObject.getAverageDuration();

        // then
        assertThat(avgDuration).isEqualTo(3503000000L / 3);
    }

    @Test
    public void getFormattedAverageDuration_ReturnsFormattedSumDurations() {

        // given
        // from @Before

        // when
        String duration = stepObject.getFormattedAverageDuration();

        // then
        assertThat(duration).isEqualTo("1.167");
    }

    @Test
    public void getPercentageResult_Returns0Percent() {

        // given
        // from @Before

        // when
        String percentage = stepObject.getPercentageResult();

        // then
        assertThat(percentage).isEqualTo("33.33%");
    }

    @Test
    public void getPercentageResult_OnOnlyFailures_Returns0Percent() {

        // given
        StepObject step = new StepObject("Test step location");
        step.addDuration(2200000000L, Status.FAILED);
        step.addDuration(303000000L, Status.UNDEFINED);

        // when
        String percentage = step.getPercentageResult();

        // then
        assertThat(percentage).isEqualTo("0.00%");
    }
}
