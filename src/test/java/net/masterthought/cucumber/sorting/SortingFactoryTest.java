package net.masterthought.cucumber.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class SortingFactoryTest extends PageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void sortFeatures_OnNATURAL_ReturnsSameList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.NATURAL);

        // when
        List<Feature> featureList = sortingFactory.sortFeatures(features);

        // then
        assertThat(featureList).containsExactly(features.get(0), features.get(1));
    }

    @Test
    public void sortFeatures_OnALPHABETICAL_ReturnsSortedList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.ALPHABETICAL);

        // when
        List<Feature> featureList = sortingFactory.sortFeatures(features);

        // then
        assertThat(featureList).containsExactly(features.get(0), features.get(1));
    }

    @Test
    public void sortFeatures_OnINVALID_ThrowsException() {

        // given
        // INVALID is available only for test profile and the reason of this shadow Enum in test profile is
        // to be able to test default: block which throws an exception for unsupported values
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.INVALID);

        // when
        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(buildErrorMessage());
        sortingFactory.sortFeatures(features);
    }

    @Test
    public void sortTags_OnNATURAL_ReturnsSameList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.NATURAL);

        // when
        List<TagObject> tagObjects = sortingFactory.sortTags(tags);

        // then
        // TODO: as the tags are stored in TreeMap, this returns already sorted elements
        assertThat(tagObjects).containsExactly(tags.get(0), tags.get(1), tags.get(2));
    }

    @Test
    public void sortTags_OnALPHABETICAL_ReturnsSameList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.ALPHABETICAL);

        // when
        List<TagObject> tagObjects = sortingFactory.sortTags(tags);

        // then
        assertThat(tagObjects).containsExactly(tags.get(0), tags.get(1), tags.get(2));
    }

    @Test
    public void sortTags_OnINVALID_ThrowsException() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.INVALID);

        // when
        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(buildErrorMessage());
        sortingFactory.sortTags(tags);
    }

    @Test
    public void sortSteps_OnNATURAL_ReturnsSameList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.NATURAL);

        // when
        List<StepObject> stepObjects = sortingFactory.sortSteps(steps);

        // then
        assertThat(stepObjects).hasSize(16);
        assertThat(stepObjects).first().isEqualTo(steps.get(0));
        assertThat(stepObjects).last().isEqualTo(steps.get(15));
    }

    @Test
    public void sortSteps_OnALPHABETICAL_ReturnsSameList() {

        // given
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.ALPHABETICAL);

        // when
        List<StepObject> stepObjects = sortingFactory.sortSteps(steps);

        // then
        // TODO: as the tags are stored in TreeMap, this returns already sorted elements
        assertThat(stepObjects).hasSize(16);
        assertThat(stepObjects).first().isEqualTo(steps.get(0));
        assertThat(stepObjects).last().isEqualTo(steps.get(15));
    }

    @Test
    public void sortSteps_OnINVALID_ThrowsException() {

        // given
        SoringMethod soringMethod = SoringMethod.INVALID;
        SortingFactory sortingFactory = new SortingFactory(soringMethod);

        // when
        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(buildErrorMessage());
        sortingFactory.sortSteps(steps);
    }

    @Test
    public void createUnknownMethodException_CreatesException() {

        // given
        SoringMethod invalidSorthingMethod = SoringMethod.ALPHABETICAL;
        SortingFactory sortingFactory = new SortingFactory(SoringMethod.ALPHABETICAL);

        // when
        Exception e = Deencapsulation.invoke(sortingFactory, "createUnknownMethodException", invalidSorthingMethod);

        // then
        assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("Unsupported sorting method: " + invalidSorthingMethod);
    }

    private String buildErrorMessage() {
        return "Unsupported sorting method: " + SoringMethod.INVALID;
    }
}