package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class HookTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getResult_ReturnsResult() {

        // given
        Hook hook = features.get(0).getElements()[1].getAfter()[0];

        // when
        Result result = hook.getResult();

        // then
        assertThat(result.getDuration()).isEqualTo(60744700);
    }

    @Test
    public void getMatch_ReturnsMatch() {

        // given
        Hook hook = features.get(0).getElements()[1].getAfter()[0];

        // when
        Match match = hook.getMatch();

        // then
        assertThat(match.getLocation()).isEqualTo("MachineFactory.timeout()");
    }

    @Test
    public void getOutputs_ReturnsOutputs() {

        // given
        Hook hook = features.get(1).getElements()[0].getBefore()[0];

        // when
        Output[] outputs = hook.getOutputs();

        // then
        assertThat(outputs).hasSize(1);
        assertThat(outputs[0].getMessages()[0]).isEqualTo("System version: beta3");
    }

    @Test
    public void getEmbeddings_ReturnsEmbeddings() {

        // given
        Hook hook = features.get(1).getElements()[0].getAfter()[0];

        // when
        Embedding[] embeddings = hook.getEmbeddings();

        // then
        assertThat(embeddings).hasSize(1);
        assertThat(embeddings[0].getMimeType()).isEqualTo("image/png");
    }

    @Test
    public void hasContent_WithEmbedding_ReturnsTrue() {

        // given
        Hook hook = features.get(1).getElements()[0].getSteps()[0].getBefore()[0];

        // when
        boolean hasContent = hook.hasContent();

        // then
        assertThat(hook.getEmbeddings()).isNotEmpty();
        assertThat(hasContent).isTrue();
    }

    @Test
    public void hasContent_WithErrorMessage_ReturnsTrue() {

        // given
        Hook hook = features.get(0).getElements()[1].getAfter()[0];

        // when
        boolean hasContent = hook.hasContent();

        // then
        assertThat(hook.getResult().getErrorMessage()).isNotEmpty();
        assertThat(hasContent).isTrue();
    }

    @Test
    public void hasContent_WithEmptyResult_ReturnsFalse() {

        // given
        Hook hook = features.get(1).getElements()[0].getSteps()[1].getAfter()[0];

        // when
        boolean hasContent = hook.hasContent();

        // then
        assertThat(hook.getResult().getErrorMessage()).isNull();
        assertThat(hasContent).isFalse();
    }

    @Test
    public void hasContent_OnEmptyHook_ReturnsFalse() {

        // given
        Hook hook = features.get(1).getElements()[0].getBefore()[0];

        // when
        boolean hasContent = hook.hasContent();

        // then
        assertThat(hook.getEmbeddings()).isEmpty();
        assertThat(hasContent).isFalse();
    }
}