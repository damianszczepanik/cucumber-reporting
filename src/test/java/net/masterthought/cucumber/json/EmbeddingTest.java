package net.masterthought.cucumber.json;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingTest {

    @Test
    public void getMimeType_ReturnsMimeType() {

        // given
        final String refMimeType = "my mime TYPE";
        Embedding embedding = new Embedding(refMimeType, "abc");

        // when
        String mimeType = embedding.getMimeType();

        // then
        assertThat(mimeType).isEqualTo(refMimeType);
    }

    @Test
    public void getData_ReturnsContent() {

        // given
        final String data = "your data";
        Embedding embedding = new Embedding("mime/type", data);

        // when
        String content = embedding.getData();

        // then
        assertThat(content).isEqualTo(data);
    }

    @Test
    public void getDecodedData_ReturnsDecodedContent() {

        // given
        Embedding embedding = new Embedding("mime/type", "ZnVuY3Rpb24gbG9nZ2VyKG1lc3NhZ2UpIHsgIH0=");

        // when
        String content = embedding.getDecodedData();

        // then
        assertThat(content).isEqualTo("function logger(message) {  }");
    }

    @Test
    public void getEscapedDecodedData_OnXMLText_ReturnsEspacedData() {

        // given
        Embedding embedding = new Embedding("mimeType", "PHhtbD48c29tZU5vZGUgYXR0cj0idmFsdWUiIC8+PC94bWw+");

        // when
        String data = embedding.getEscapedDecodedData();

        // then
        assertThat(data).isEqualTo("&lt;xml&gt;&lt;someNode attr=&quot;value&quot; /&gt;&lt;/xml&gt;");
    }

    @Test
    public void getEscapedDecodedData_OnPlainText_ReturnsData() {

        // given
        Embedding embedding = new Embedding("mimeType", "b25lLCB0d28sIHRocmVlIQ==");

        // when
        String data = embedding.getEscapedDecodedData();

        // then
        assertThat(data).isEqualTo("one, two, three!");
    }

    @Test
    public void getFileName_ReturnsFileName() {

        // given
        Embedding embedding = new Embedding("text/xml", "some data");

        // when
        String fileName = embedding.getFileName();

        // then
        assertThat(fileName).startsWith("embedding_-642587818");
        assertThat(fileName).endsWith(".xml");
    }


    @Test
    public void getFileName_isAlwaysUnique() {

        // given
        Embedding embedding1 = new Embedding("text/plain", "some data");
        Embedding embedding2 = new Embedding("text/plain", "some data");

        // when
        String fileName1 = embedding1.getFileName();
        String fileName2 = embedding2.getFileName();

        // then
        assertThat(fileName1).isNotEqualTo(fileName2);
    }

    @Test
    public void getExtension__OnCommonMimeType_ReturnsFileExtension() {

        // given
        Embedding embedding = new Embedding("text/html", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("html");
    }

    @Test
    public void getExtension__OnTextMimeType_ReturnsText() {

        // given
        Embedding embedding = new Embedding("text/plain", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void getExtension__OnImageUrlMimeType_ReturnsTxt() {

        // given
        Embedding embedding = new Embedding("image/url", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("image");
    }

    @Test
    public void getExtension__OnUnknownType_ResurnsUnknown() {

        // given
        Embedding embedding = new Embedding("js", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("unknown");
    }
}
