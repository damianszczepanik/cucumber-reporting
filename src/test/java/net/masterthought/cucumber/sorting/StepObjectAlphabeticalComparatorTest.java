package net.masterthought.cucumber.sorting;

import net.masterthought.cucumber.json.support.StepObject;
import org.junit.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepObjectAlphabeticalComparatorTest {

    private final Comparator<StepObject> comparator = new StepObjectAlphabeticalComparator();

    @Test
    public void compareTo_OnDifferentLocation_ReturnsNoneZero() {

        // given
        StepObject step1 = new StepObject("one");
        StepObject step2 = new StepObject("two");

        // when
        int result = comparator.compare(step1, step2);

        // then
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    public void compareTo_OnSameLocation_ReturnsZero() {

        // given
        StepObject step1 = new StepObject("one");
        StepObject step2 = new StepObject("one");

        // when
        int result = comparator.compare(step1, step2);

        // then
        assertThat(result).isEqualTo(0);
    }
}
