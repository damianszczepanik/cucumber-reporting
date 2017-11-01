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
        Hook before = features.get(0).getElements()[1].getAfter()[0];

        // when
        Result result = before.getResult();

        // then
        assertThat(result.getDuration()).isEqualTo(60744700);
    }

    @Test
    public void getMatch_ReturnsMatch() {

        // given
        Hook after = features.get(0).getElements()[1].getAfter()[0];

        // when
        Match match = after.getMatch();

        // then
        assertThat(match.getLocation()).isEqualTo("MachineFactory.timeout()");
    }

    @Test
    public void getOutputs_ReturnsOutputs() {

        // given
        Hook before = features.get(1).getElements()[0].getBefore()[0];

        // when
        Output[] outputs = before.getOutputs();

        // then
        assertThat(outputs).hasSize(1);
        assertThat(outputs[0].getMessages()[0]).isEqualTo("System version: beta3");
    }

    @Test
    public void getEmbeddings_ReturnsEmbeddings() {

        // given
        Hook after = features.get(1).getElements()[0].getAfter()[0];

        // when
        Embedding[] embeddings = after.getEmbeddings();

        // then
        assertThat(embeddings).hasSize(1);
        assertThat(embeddings[0].getMimeType()).isEqualTo("image/png");
    }
}