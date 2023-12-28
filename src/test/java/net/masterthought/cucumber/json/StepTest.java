package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class StepTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getRows_ReturnsRows() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[2];

        // when
        Row[] rows = step.getRows();

        // then
        assertThat(rows).hasSize(5);
        assertThat(rows[0].getCells()).containsOnlyOnce("Müller", "Deutschland");
    }

    @Test
    void getRows_OnArguments_ReturnsRows() {

        // given
        Step step = features.get(0).getElements()[1].getSteps()[5];

        // when
        Row[] rows = step.getRows();

        // then
        assertThat(rows).hasSize(2);
        assertThat(rows[0].getCells()).containsOnlyOnce("max", "min");
    }

    @Test
    void getName_ReturnsName() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[0];

        // when
        String name = step.getName();

        // then
        assertThat(name).isEqualTo("I have a new credit card");
    }

    @Test
    void getKeyword_ReturnsKeyword() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[1];

        // when
        String keyword = step.getKeyword();

        // then
        assertThat(keyword).isEqualTo("And");
    }

    @Test
    void getOutput_ReturnsOutput() {

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
    void getMatch_ReturnsMatch() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[4];

        // when
        Match match = step.getMatch();

        // then
        assertThat(match.getLocation()).isEqualTo("ATMScenario.checkMoney(int)");
    }

    @Test
    void getEmbeddings_ReturnsEmbeddings() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[6];

        // when
        Embedding[] embeddings = step.getEmbeddings();

        // then
        assertThat(embeddings).hasSize(1);
        assertThat(embeddings[0].getMimeType()).isEqualTo("application/json");
    }

    @Test
    void getResult_ReturnResult() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[5];

        // when
        Result result = step.getResult();

        // then
        assertThat(result.getErrorMessage()).contains("java.lang.AssertionError");
    }

    @Test
    void getStatus_ReturnsStatus() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[5];

        // when
        Status status = step.getResult().getStatus();

        // then
        assertThat(status).isEqualTo(Status.SKIPPED);
    }

    @Test
    void getDuration_ReturnsDuration() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[1];

        // when
        long duration = step.getDuration();

        // then
        assertThat(duration).isEqualTo(13000L);
    }

    @Test
    void getDuration_OnMissingDuration_ReturnsZero() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[0];

        // when
        long duration = step.getDuration();

        // then
        assertThat(duration).isZero();
    }

    @Test
    void getResult_OnMissingResult_ReturnsEmptyResult() {

        // given
        Step step = features.get(1).getElements()[2].getSteps()[0];

        // when
        Result result = step.getResult();

        // then
        assertThat(result.getStatus()).isEqualTo(Status.UNDEFINED);
        assertThat(result.getDuration()).isZero();
        assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    void getBeforeHook_ReturnsBeforeHooks() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[0];

        // when
        Hook[] afterHooks = step.getBefore();

        // then
        assertThat(afterHooks).hasSize(1);
        assertThat(afterHooks[0].getResult().getDuration()).isEqualTo(410802047);
    }

    @Test
    void getAfterHook_ReturnsAfterHooks() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[1];

        // when
        Hook[] beforeHooks = step.getAfter();

        // then
        assertThat(beforeHooks).hasSize(1);
        assertThat(beforeHooks[0].getResult().getDuration()).isEqualTo(410802048);
    }

    @Test
    void getBeforeStatus_ReturnsStatusForBeforeHooks() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[0];

        // when
        Status status = step.getBeforeStatus();

        // then
        assertThat(status).isEqualTo(Status.FAILED);
    }


    @Test
    void getAfterStatus_ReturnsStatusForAfterHooks() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[2];

        // when
        Status status = step.getAfterStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }


    @Test
    void getBeforeStatus_OnEmptyHooks_ReturnsPassed() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[2];

        // when
        Status status = step.getBeforeStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    void getAfterStatus_OnEmptyHooks_ReturnsPassed() {

        // given
        Step step = features.get(1).getElements()[0].getSteps()[2];

        // when
        Status status = step.getAfterStatus();

        // then
        assertThat(status).isEqualTo(Status.PASSED);
    }

    @Test
    void getComments_ReturnsCommentsFromArrayOfString() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[0];

        // when
        List<String> keyword = step.getComments();

        // then
        assertThat(keyword).containsExactly("# Some comments", "# Some more comments");
    }

    @Test
    void getComments_ReturnsCommentsFromArrayOfCommentObject() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[1];

        // when
        List<String> keyword = step.getComments();

        // then
        assertThat(keyword).containsExactly("# First comments", "# Second comments");
    }

    @Test
    void getComments_ReturnsEmptyCommentList() {

        // given
        Step step = features.get(0).getElements()[0].getSteps()[2];

        // when
        List<String> keyword = step.getComments();

        // then
        assertThat(keyword).isEmpty();
    }
}
