package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getMimeTypeReturnsMimeType() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[0];

        // when
        String mimeType = embedding.getMimeType();

        // then
        assertThat(mimeType).isEqualTo("image/png");
    }

    @Test
    public void getDataReturnsContent() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[2];

        // when
        String content = embedding.getData();

        // then
        assertThat(content).isEqualTo("amF2YS5sYW5nLlRocm93YWJsZQ==");
    }

    @Test
    public void getDecodedDataReturnsgetDecodedContent() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[3];

        // when
        String content = embedding.getDecodedData();

        // then
        assertThat(content).isEqualTo("<i>Hello</i> <b>World!</b>");
    }

    @Test
    public void getExtensionOnCommonMimeTypeResurnsFileExtension() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[3];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("html");
    }

    @Test
    public void getExtensionOnTextMimeTypeResurnsTxt() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[2];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void getExtensionOnOtherMimeTypeResurnsUnknown() {

        // give
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[4];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("unknown");
    }
}
