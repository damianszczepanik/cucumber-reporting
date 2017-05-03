package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getRows_ReturnsRows() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[2];

        // when
        Row[] rows = step.getRows();

        // then
        assertThat(rows).hasSize(5);
        assertThat(rows[0].getCells()).containsOnlyOnce("Müller", "Deutschland");
    }

    @Test
    public void getName_ReturnsName() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[0];

        // when
        String name = step.getName();

        // then
        assertThat(name).isEqualTo("I have a new credit card");
    }

    @Test
    public void getKeyword_ReturnsKeyword() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[1];

        // when
        String keyword = step.getKeyword();

        // then
        assertThat(keyword).isEqualTo("And");
    }

    @Test
    public void getOutput_ReturnsOutput() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[7];

        // when
        Output[] outputs = step.getOutputs();

        // then
        assertThat(getMessages(outputs)).containsOnlyOnce(
                "Could not connect to the server @Rocky@",
                "Could not connect to the server @Mike@");
    }

    @Test
    public void getMatch_ReturnsMatch() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[4];

        // when
        Match match = step.getMatch();

        // then
        assertThat(match.getLocation()).isEqualTo("ATMScenario.checkMoney(int)");
    }

    @Test
    public void getEmbeddings_ReturnsEmbeddings() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[6];

        // when
        Embedding[] embeddings = step.getEmbeddings();

        // then
        assertThat(embeddings).hasSize(1);
        assertThat(embeddings[0].getMimeType()).isEqualTo("application/json");
    }

    @Test
    public void getResult_ReturnResult() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[5];

        // when
        Result result = step.getResult();

        // then
        assertThat(result.getErrorMessage()).contains("java.lang.AssertionError");
    }

    @Test
    public void getStatus_ReturnsStatus() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[5];

        // when
        Status status = step.getResult().getStatus();

        // then
        assertThat(status).isEqualTo(Status.SKIPPED);
    }

    @Test
    public void getDuration_ReturnsDuration() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[1];

        // when
        long duration = step.getDuration();

        // then
        assertThat(duration).isEqualTo(13000L);
    }

    @Test
    public void getDuration_OnMissingDuration_ReturnsZero() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[0];

        // when
        long duration = step.getDuration();

        // then
        assertThat(duration).isZero();
    }

    @Test
    public void getResult_OnMissingResult_ReturnsEmptyResult() {

        // given
        Step step = features.get(1).getElements()[2].getSteps()[0];

        // when
        Result result = step.getResult();

        // then
        assertThat(result.getStatus()).isEqualTo(Status.UNDEFINED);
        assertThat(result.getDuration()).isEqualTo(0L);
        assertThat(result.getErrorMessage()).isNull();
    }
}