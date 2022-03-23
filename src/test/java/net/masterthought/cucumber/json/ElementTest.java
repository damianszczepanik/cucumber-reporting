package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ElementTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getSteps_ReturnsSteps() {

        // given
        Element element = features.get(0).getElements()[1];

        // when
        Step[] steps = element.getSteps();

        // then
        assertThat(steps).hasSize(6);
        assertThat(steps[0].getName()).isEqualTo("the account balance is 100");
    }

    @Test
    public void getBefore_ReturnsHooks() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Hook[] before = element.getBefore();

        // then
        assertThat(before).hasSize(2);
        assertThat(before[0].getResult().getDuration()).isEqualTo(10744700L);
    }

    @Test
    public void getAfter_ReturnsHooks() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Hook[] after = element.getAfter();

        // then
        assertThat(after).hasSize(1);
        assertThat(after[0].getResult().getDuration()).isEqualTo(64700000L);
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
        Element element = features.get(1).getElements()[0];

        // when
        Status status = element.getStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }


    @Test
    public void getBeforeStatus_ReturnsStatus() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Status status = element.getBeforeStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }

    @Test
    public void getBeforeStatus_OnNoExistingBefore_ReturnsStatus() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        Status status = element.getBeforeStatus();

        // then
        assertThat(element.getBefore()).isEmpty();
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    public void getAfterStatus_ReturnsStatus() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Status status = element.getAfterStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }

    @Test
    public void getAfterStatus_OnNoExistingBefore_ReturnsStatus() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        Status status = element.getAfterStatus();

        // then
        assertThat(element.getAfter()).isEmpty();
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    public void getStepsStatus_ReturnsStatus() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        Status status = element.getStepsStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }

    @Test
    public void getName_ReturnsName() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        String name = element.getName();

        // then
        assertThat(name).isEqualTo("Account may not have sufficient funds");
    }

    @Test
    public void getKeyword_ReturnsName() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        String keyword = element.getKeyword();

        // then
        assertThat(keyword).isEqualTo("Background");
    }

    @Test
    public void getType_ReturnsName() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        String type = element.getType();

        // then
        assertThat(type).isEqualTo("scenario");
    }

    @Test
    public void getDescription_ReturnsDescription() {

        // given
        Element element = features.get(1).getElements()[0];

        // when
        String description = element.getDescription();

        // then
        assertThat(description).isEqualTo("Account holder withdraws more cash");
    }

    @Test
    public void getDescription_OnMissingDescription_ReturnsEmptyString() {

        // given
        Element element = features.get(1).getElements()[1];

        // when
        String description = element.getDescription();

        // then
        assertThat(description).isEmpty();
    }

    @Test
    public void isScenario_ReturnsTrueForScenarios() {

        // given
        Feature feature = features.get(0);

        // when
        boolean[] results = {
                feature.getElements()[0].isScenario(),
                feature.getElements()[1].isScenario()};

        // then
        assertThat(results).containsOnly(false, true);
    }

    @Test
    public void getFeature_ReturnsFeature() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        Feature feature = element.getFeature();

        // then
        assertThat(feature.getId()).isEqualTo("account-holder-withdraws-cash");
    }

    @Test
    public void getDuration_ReturnsDuration() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        long duration = element.getDuration();

        // then
        assertThat(duration).isEqualTo(99124118111L);
    }

    @Test
    public void getFormattedDuration_ReturnsFormattedDuration() {

        // given
        Element element = features.get(0).getElements()[0];

        // when
        String duration = element.getFormattedDuration();

        // then
        assertThat(duration).isEqualTo("1:39.124");
    }
}
