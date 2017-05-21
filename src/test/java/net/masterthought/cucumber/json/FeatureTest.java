package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getDeviceName_ReturnsDeviceName() {

        // given
        Feature feature = features.get(0);

        // when
        String deviceName = feature.getDeviceName();

        // when
        assertThat(deviceName).isEqualTo("sample");
    }

    @Test
    public void getId_ReturnsID() {

        // given
        Feature feature = features.get(1);

        // when
        String id = feature.getId();

        // then
        assertThat(id).isEqualTo("account-holder-withdraws-more-cash");
    }

    @Test
    public void getElements_ReturnsElements() {

        // given
        Feature feature = features.get(0);

        // when
        Element[] elements = feature.getElements();

        // then
        assertThat(elements).hasSize(2);
        assertThat(elements[0].getName()).isEqualTo("Activate Credit Card");
    }

    @Test
    public void getReportFileName_ReturnsFileName() {

        // given
        Feature feature = features.get(1);

        // when
        String fileName = feature.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("report-feature_net-masterthought-example-ATMK-feature.html");
    }

    @Test
    public void getReportFileName_OnParallelTesting_ReturnsFileNameWithNumberPostfix() {

        // given
        configuration.setParallelTesting(true);
        setUpWithJson(SAMPLE_JSON);
        Feature feature = features.get(2);

        // when
        String fileName = feature.getReportFileName();

        // then
        assertThat(fileName).isEqualTo("report-feature_net-masterthought-example-ATMK-feature_sample.html");
    }

    @Test
    public void getTags_ReturnsTags() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Tag[] tags = element.getTags();

        // then
        assertThat(tags).hasSize(1);
        assertThat(tags[0].getName()).isEqualTo("@checkout");
    }

    @Test
    public void getStatus_ReturnsStatus() {

        // given
        Feature feature = features.get(1);

        // when
        Status status = feature.getStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }

    @Test
    public void getName_ReturnsName() {

        // given
        Feature feature = features.get(0);

        // when
        String name = feature.getName();

        // then
        assertThat(name).isEqualTo("1st feature");
    }

    @Test
    public void getKeyword_ReturnsKeyword() {

        // given
        Feature feature = features.get(0);

        // when
        String keyword = feature.getKeyword();

        // then
        assertThat(keyword).isEqualTo("Feature");
    }

    @Test
    public void getDescription_ReturnsDescription() {

        // given
        Feature feature = features.get(0);

        // when
        String description = feature.getDescription();

        // then
        assertThat(description).isEqualTo("This is description of the feature");
    }

    @Test
    public void getFeatures_ReturnsOne() {

        // given
        Feature feature = features.get(0);

        // when
        int featureCounter = feature.getFeatures();

        // then
        assertThat(featureCounter).isEqualTo(1);
    }

    @Test
    public void getXXXFeatures_OnPassedFeature_ReturnsFeaturesForStatus() {

        // given
        Feature passedFeature = features.get(0);

        // then
        assertThat(passedFeature.getPassedFeatures()).isEqualTo(1);
        assertThat(passedFeature.getFailedFeatures()).isEqualTo(0);
    }

    @Test
    public void getXXXFeatures_OnFAiledFeature_ReturnsFeaturesForStatus() {

        // given
        Feature failedFeature = features.get(1);

        // then
        assertThat(failedFeature.getPassedFeatures()).isEqualTo(0);
        assertThat(failedFeature.getFailedFeatures()).isEqualTo(1);
    }

    @Test
    public void getScenarios_ReturnsNumberOfScenarios() {

        // given
        Feature feature = features.get(0);

        // when
        int scenarioCounter = feature.getScenarios();

        // then
        assertThat(scenarioCounter).isEqualTo(1);
    }

    @Test
    public void getXXXScenarios_ReturnsScenariosForStatus() {

        // given
        Feature feature = features.get(1);

        // then
        assertThat(feature.getPassedScenarios()).isEqualTo(1);
        assertThat(feature.getFailedScenarios()).isEqualTo(2);
    }

    @Test
    public void getSteps_ReturnsNumberOfSteps() {

        // given
        Feature feature = features.get(0);

        // when
        int stepsCounter = feature.getSteps();

        // then
        assertThat(stepsCounter).isEqualTo(10);
    }

    @Test
    public void getXXXSteps_ReturnsStepsForStatus() {

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
    public void getDuration_ReturnsDuration() {

        // given
        Feature feature = features.get(0);

        // when
        long duration = feature.getDuration();

        // then
        assertThat(duration).isEqualTo(99263122889L);
    }

    @Test
    public void getFormattedDuration_ReturnsFormattedDuration() {

        // given
        Feature feature = features.get(1);

        // when
        String formattedDuration = feature.getFormattedDuration();

        // then
        assertThat(formattedDuration).isEqualTo("092ms");
    }

    @Test
    public void getJsonFile_ReturnsFileName() {

        // given
        Feature feature = features.get(0);

        // when
        String fileName = feature.getJsonFile();

        // then
        assertThat(fileName).endsWith(SAMPLE_JSON);
    }

    @Test
    public void calculateDeviceName_ReturnsDeviceName() {

        // given
        Feature feature = new Feature();
        final String jsonFileName = "json_filename_without_extension";
        Deencapsulation.setField(feature, "jsonFile", jsonFileName + ".json");

        // when
        String deviceName = Deencapsulation.invoke(feature, "calculateDeviceName");

        // then
        assertThat(deviceName).isEqualTo(jsonFileName);
    }

    @Test
    public void calculateDeviceName_OnFileWithoutExtension_ReturnsDeviceName() {

        // given
        Feature feature = new Feature();
        final String jsonFileName = "json_filename_without_extension";
        Deencapsulation.setField(feature, "jsonFile", jsonFileName);

        // when
        String deviceName = Deencapsulation.invoke(feature, "calculateDeviceName");

        // then
        assertThat(deviceName).isEqualTo(jsonFileName);
    }
}