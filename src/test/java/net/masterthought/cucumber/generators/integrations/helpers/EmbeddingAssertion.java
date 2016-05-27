package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.Charsets;
import org.codehaus.plexus.util.Base64;

import net.masterthought.cucumber.json.Embedding;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingAssertion extends ReportAssertion {

    public void hasImageContent(Embedding embedding) {
        String src = getBox().oneBySelector("img", WebAssertion.class).attr("src");
        assertThat(src).endsWith(embedding.hashCode() + "." + embedding.getExtension());
    }

    public void hasTextContent(String content) {
        assertThat(getBox().html()).isEqualTo(getDecodedData(content));
    }

    public void hasHtmlContent(String content) {
        assertThat(getBox().oneBySelector("pre", WebAssertion.class).html()).isEqualTo(content);
    }

    private WebAssertion getBox() {
        return oneByClass("embedding-content", WebAssertion.class);
    }

    private String getDecodedData(String data) {
        return new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }
}
