package net.masterthought.cucumber.generators.integrations.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.Charsets;
import org.codehaus.plexus.util.Base64;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingAssertion extends ReportAssertion {

    public void hasImageContent(String content) {
        assertThat(getBox().oneBySelector("img", WebAssertion.class).attr("src")).endsWith(content);
    }

    public void hasTextContent(String content) {
        assertThat(getBox().html()).isEqualTo(getDecodedData(content));
    }

    public void hasHtmlContent(String content) {
        assertThat(getBox().oneBySelector("pre", WebAssertion.class).html()).isEqualTo(content);
    }

    private WebAssertion getBox() {
        return oneByClass("embedding-box", WebAssertion.class);
    }

    private String getDecodedData(String data) {
        return new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }
}
