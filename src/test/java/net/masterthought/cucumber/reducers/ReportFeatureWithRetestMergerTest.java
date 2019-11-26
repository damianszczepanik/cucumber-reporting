package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import org.junit.Test;
import org.powermock.api.support.membermodification.MemberModifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_WITH_RETEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

public class ReportFeatureWithRetestMergerTest {

    private ReportFeatureWithRetestMerger merger = new ReportFeatureWithRetestMerger();

    private Element buildElement(String id, boolean isBackground) {
        try {
            Element result = new Element();
            MemberModifier.field(Element.class, "id").set(result, id);
            MemberModifier.field(Element.class, "type").set(result, isBackground ? "background" : "scenario");
            MemberModifier.field(Element.class, "startTime").set(result, LocalDateTime.now());
            return result;
        }
        catch (IllegalAccessException e) {
            return null;
        }
    }

    private Element buildBackground() {
        return buildElement(UUID.randomUUID().toString(), true);
    }

    private Element buildScenario() {
        return buildElement(UUID.randomUUID().toString(), false);
    }

    @Test
    public void test_UpdateElementsOfGivenFeature_NoCoincidencesById() throws IllegalAccessException {
        // given
        Feature feature = new Feature();
        MemberModifier
                .field(Feature.class, "elements")
                .set(feature, new Element[] {buildBackground(), buildScenario(), buildScenario()});

        Element[] newElements = {buildBackground(), buildScenario()};

        // when
        merger.updateElements(feature, newElements);

        // then
        assertThat(feature.getElements()).hasSize(5);
        assertThat(feature.getElements()[3]).isSameAs(newElements[0]);
        assertThat(feature.getElements()[4]).isSameAs(newElements[1]);
    }

    @Test
    public void test_UpdateElementsOfGivenFeature_WithCoincidenceById() throws IllegalAccessException {
        // given
        Feature feature = new Feature();
        Element[] elements = {buildScenario(), buildBackground(), buildScenario()};
        MemberModifier
                .field(Feature.class, "elements")
                .set(feature, elements);

        Element[] newElements = {buildElement(elements[0].getId(), false)};

        // when
        merger.updateElements(feature, newElements);

        // then
        assertThat(feature.getElements()).hasSize(3);
        assertThat(feature.getElements()[0]).isSameAs(newElements[0]);
    }

    @Test
    public void test_elementReplaceWithNewest() {
        // given
        LocalDateTime currentTime = LocalDateTime.now();

        Element target = spy(new Element());
        when(target.getStartTime()).thenReturn(currentTime);

        Element candidate = spy(new Element());
        when(candidate.getStartTime()).thenReturn(currentTime.plus(1, SECONDS));

        // when
        boolean result = merger.replaceIfExists(target, candidate);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void test_elementWithGivenIndexIsBackground() {
        // given
        Element first = buildScenario();
        Element second = buildBackground();

        // when
        boolean isBackground = merger.isBackground(1, new Element[]{first, second});

        // then
        assertThat(isBackground).isTrue();
    }

    @Test
    public void test_elementWithGivenIndexIsNotBackground() {
        // given
        Element first = buildScenario();
        Element second = buildBackground();

        // when
        boolean isBackground = merger.isBackground(0, new Element[]{first, second});

        // then
        assertThat(isBackground).isFalse();
    }

    @Test
    public void test_isBackgroundCheckFailsDueToNegativeIndex() {
        // given
        // when
        boolean isBackground = merger.isBackground(-1, new Element[]{});

        // then
        assertThat(isBackground).isFalse();
    }

    @Test
    public void test_isBackgroundCheckFailsDueToNullCollection() {
        // given
        // when
        boolean isBackground = merger.isBackground(1, null);

        // then
        assertThat(isBackground).isFalse();
    }

    @Test
    public void test_isBackgroundCheckFailsDueToIndexOutOfBounds() {
        // given
        // when
        boolean isBackground = merger.isBackground(1, new Element[]{});

        // then
        assertThat(isBackground).isFalse();
    }

    @Test
    public void test_findElementOfTheSameTypeWithSameId() {
        // given
        Element first = buildScenario();
        Element second = buildScenario();

        // when
        int index = merger.find(new Element[]{first, second}, second);

        // then
        assertThat(index).isEqualTo(1);
    }

    @Test
    public void test_ThereIsNoElementOfTheSameTypeWithSameId() {
        // given
        Element first = buildScenario();
        Element second = buildScenario();

        // when
        int index = merger.find(new Element[]{first}, second);

        // then
        assertThat(index).isEqualTo(-1);
    }

    @Test
    public void check_MergerIsApplicableByType_NullParam() {
        // given
        // when
        boolean isApplicable = merger.test(null);

        // then
        assertThat(isApplicable).isFalse();
    }

    @Test
    public void check_MergerIsApplicableByType_CorrectParam() {
        // given
        // when
        boolean isApplicableByType = merger.test(Arrays.asList(MERGE_FEATURES_WITH_RETEST));

        // then
        assertThat(isApplicableByType).isTrue();
    }
}
