package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class FeatureTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getId_ReturnsID() {

        // given
        Feature feature = features.get(1);

        // when
        String id = feature.getId();

        // then
        assertThat(id).isEqualTo("account-holder-withdraws-more-cash");
    }

    @Test
    void addElements_AddsScenarios() {

        // given
        Feature feature = features.get(0);
        int scenarioCount = feature.getElements().length;
        Element[] scenarioToAdd = new Element[]{features.get(0).getElements()[0]};

        // when
        feature.addElements(scenarioToAdd);

        // then
        assertThat(feature.getElements()).hasSize(scenarioCount + scenarioToAdd.length);
    }

    @Test
    void getElements_ReturnsElements() {

        // given
        Feature feature = features.get(0);

        // when
        Element[] elements = feature.getElements();

        // then
        assertThat(elements).hasSize(2);
        assertThat(elements[0].getName()).isEqualTo("Activate Credit Card");
    }

    @Test
    void calculateReportFileName_ReturnsFileName() throws Exception {

        // given
        Feature feature = features.get(1);
        final int jsonFileNo = 3;

        // when
        String reportFileName = Whitebox.invokeMethod(feature, "calculateReportFileName", jsonFileNo);

        // then
        assertThat(reportFileName).isEqualTo("report-feature_3_1515379431.html");
    }

    @Test
    void getReportFileName_ReturnsFileName() {

        // given
        Feature feature = features.get(1);

        // when
        String fileName = feature.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("report-feature_1_1515379431.html");
    }

    @Test
    void getQualifier_ReturnsFileNameWithoutExtension() {

        // given
        Feature feature = features.get(1);

        // when
        String qualifier = feature.getQualifier();

        // then
        assertThat(qualifier).isEqualTo("sample");
    }

    @Test
    void getTags_ReturnsTags() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Tag[] tags = element.getTags();

        // then
        assertThat(tags).hasSize(1);
        assertThat(tags[0].getName()).isEqualTo("@checkout");
    }

    @Test
    void getStatus_ReturnsStatus() {

        // given
        Feature feature = features.get(1);

        // when
        Status status = feature.getStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }

    @Test
    void getName_ReturnsName() {

        // given
        Feature feature = features.get(0);

        // when
        String name = feature.getName();

        // then
        assertThat(name).isEqualTo("1st feature");
    }

    @Test
    void getKeyword_ReturnsKeyword() {

        // given
        Feature feature = features.get(0);

        // when
        String keyword = feature.getKeyword();

        // then
        assertThat(keyword).isEqualTo("Feature");
    }

    @Test
    void getDescription_ReturnsDescription() {

        // given
        Feature feature = features.get(0);

        // when
        String description = feature.getDescription();

        // then
        assertThat(description).isEqualTo("This is description of the feature");
    }

    @Test
    void getFeatures_ReturnsOne() {

        // given
        Feature feature = features.get(0);

        // when
        int featureCounter = feature.getFeatures();

        // then
        assertThat(featureCounter).isEqualTo(1);
    }

    @Test
    void getXXXFeatures_OnPassedFeature_ReturnsFeaturesForStatus() {

        // given
        Feature passedFeature = features.get(0);

        // then
        assertThat(passedFeature.getPassedFeatures()).isOne();
        assertThat(passedFeature.getFailedFeatures()).isZero();
    }

    @Test
    void getXXXFeatures_OnFAiledFeature_ReturnsFeaturesForStatus() {

        // given
        Feature failedFeature = features.get(1);

        // then
        assertThat(failedFeature.getPassedFeatures()).isZero();
        assertThat(failedFeature.getFailedFeatures()).isOne();
    }

    @Test
    void getScenarios_ReturnsNumberOfScenarios() {

        // given
        Feature feature = features.get(0);

        // when
        int scenarioCounter = feature.getScenarios();

        // then
        assertThat(scenarioCounter).isEqualTo(1);
    }

    @Test
    void getXXXScenarios_ReturnsScenariosForStatus() {

        // given
        Feature feature = features.get(1);

        // then
        assertThat(feature.getPassedScenarios()).isEqualTo(1);
        assertThat(feature.getFailedScenarios()).isEqualTo(2);
    }

    @Test
    void getSteps_ReturnsNumberOfSteps() {

        // given
        Feature feature = features.get(0);

        // when
        int stepsCounter = feature.getSteps();

        // then
        assertThat(stepsCounter).isEqualTo(10);
    }

    @Test
    void getXXXSteps_ReturnsStepsForStatus() {

        // given
        Feature passingFeature = features.get(0);
        Feature feature2 = features.get(1);

        // then
        assertThat(passingFeature.getPassedSteps()).isEqualTo(10);

        assertThat(feature2.getFailedSteps()).isEqualTo(1);
        assertThat(feature2.getSkippedSteps()).isEqualTo(2);
        assertThat(feature2.getPendingSteps()).isEqualTo(1);
        assertThat(feature2.getUndefinedSteps()).isEqualTo(3);
    }

    @Test
    void getDuration_ReturnsDuration() {

        // given
        Feature feature = features.get(0);

        // when
        long duration = feature.getDuration();

        // then
        assertThat(duration).isEqualTo(99263122889L);
    }

    @Test
    void getFormattedDuration_ReturnsFormattedDuration() {

        // given
        Feature feature = features.get(1);

        // when
        String formattedDuration = feature.getFormattedDuration();

        // then
        assertThat(formattedDuration).isEqualTo("0.092");
    }
}