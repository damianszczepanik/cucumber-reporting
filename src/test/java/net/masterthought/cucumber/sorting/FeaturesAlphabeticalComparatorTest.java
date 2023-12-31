package net.masterthought.cucumber.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class FeaturesAlphabeticalComparatorTest extends PageTest {

    private final Comparator<Feature> comparator = new FeaturesAlphabeticalComparator();

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void compareTo_OnSameFeature_ReturnsZero() {

        // given
        Feature feature1 = features.get(0);
        Feature feature2 = features.get(0);

        // when
        int result = comparator.compare(feature1, feature2);

        // then
        assertThat(result).isZero();
    }

    @Test
    void compareTo_OnSameName_ReturnsNotZero() {

        // given
        Feature feature1 = features.get(0);
        Feature feature2 = buildFeature(feature1.getName(), "myId", "myFile.json");

        // then
        int result = comparator.compare(feature1, feature2);

        // then
        assertThat(result).isEqualTo(feature1.getId().compareTo(feature2.getId()));
    }

    @Test
    void compareTo_OnSameNameAndId_ReturnsNotZero() {

        // given
        Feature feature1 = features.get(0);
        Feature feature2 = buildFeature(feature1.getName(), feature1.getId(), "myFile.json");

        // then
        int result = comparator.compare(feature1, feature2);

        // then
        assertThat(result).isEqualTo(feature1.getReportFileName().compareTo(feature2.getReportFileName()));
    }

    @Test
    void compareTo_OnDifferentName_ReturnsNotZero() {

        // given
        Feature feature1 = features.get(0);
        Feature feature2 = buildFeature(feature1.getName() + "_", feature1.getId(), feature1.getReportFileName());

        // then
        int result = comparator.compare(feature1, feature2);

        // then
        assertThat(result).isEqualTo(feature1.getName().compareTo(feature2.getName()));
    }

    private static Feature buildFeature(final String name, final String id, final String reportFileName) {
        Feature feature = new Feature();
        Whitebox.setInternalState(feature, "name", name);
        Whitebox.setInternalState(feature, "id", id);
        Whitebox.setInternalState(feature, "reportFileName", reportFileName);

        return feature;
    }
}
