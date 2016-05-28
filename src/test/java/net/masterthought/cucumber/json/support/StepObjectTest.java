package net.masterthought.cucumber.json.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.ValidationException;

/**
 * @author Sam Park (midopa@github)
 */
public class StepObjectTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private StepObject stepObject;

    @Before
    public void setUp() {
        stepObject = new StepObject("Test step location");
        stepObject.addDuration(1000000000L, Status.PASSED);
        stepObject.addDuration(2200000000L, Status.FAILED);
        stepObject.addDuration( 303000000L, Status.UNDEFINED);
    }

    @Test
    public void StepObjectOnNullLocationThrowsException() {
        // given
        // nothing

        // then
        thrown.expect(ValidationException.class);
        new StepObject(null);
    }

    @Test
    public void getLocationReturnsLocation() {

        // given
        // from @Before

        // when
        String location = stepObject.getLocation();

        // then
        assertThat(location).isEqualTo("Test step location");
    }

    @Test
    public void addDurationSumsDurations() {

        // give
        StepObject step = new StepObject("ble bla ble");

        // when
        step.addDuration(20L, Status.PASSED);
        step.addDuration(5L, Status.PASSED);
        step.addDuration(700L, Status.UNDEFINED);

        // then
        assertThat(step.getDurations()).isEqualTo(725L);
        assertThat(step.getTotalOccurrences()).isEqualTo(3);
        assertThat(step.getStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    public void getFormattedTotalDurationReturnsFormattedSumDurations() {

        // give
        // from @Before

        // when
        String duration = stepObject.getFormattedTotalDuration();

        // then
        assertThat(duration).isEqualTo("3s 503ms");
    }

    @Test
    public void getFormattedAverageDurationReturnsFormattedSumDurations() {

        // give
        // from @Before

        // when
        String duration = stepObject.getFormattedAverageDuration();

        // then
        assertThat(duration).isEqualTo("1s 167ms");
    }

    @Test
    public void getAverageDurationsReturnsTime() {

        // given
        // from @Before

        // when
        long avgDuration = stepObject.getAverageDuration();

        // then
        assertThat(avgDuration).isEqualTo(3503000000L / 3);
    }

    @Test
    public void getPercentageResultReturns0Percent() {

        // given
        // from @Before

        // when
        String percentage = stepObject.getPercentageResult();

        // then
        assertThat(percentage).isEqualTo("33.33%");
    }

    @Test
    public void getPercentageResultOnOnlyFailuresReturns0Percent() {

        // given
        StepObject step = new StepObject("Test step location");
        step.addDuration(2200000000L, Status.FAILED);
        step.addDuration(303000000L, Status.UNDEFINED);

        // when
        String percentage = step.getPercentageResult();

        // then
        assertThat(percentage).isEqualTo("0.00%");
    }

    @Test
    public void compareToOnDifferentLocationReturnsNoneZero() {

        // given
        StepObject step1 = new StepObject("one");
        StepObject step2 = new StepObject("two");

        // when
        int result = step1.compareTo(step2);

        // then
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    public void compareToOnSameLocationReturnsZero() {

        // given
        StepObject step1 = new StepObject("one");
        StepObject step2 = new StepObject("one");

        // when
        int result = step1.compareTo(step2);

        // then
        assertThat(result).isEqualTo(0);
    }
}
