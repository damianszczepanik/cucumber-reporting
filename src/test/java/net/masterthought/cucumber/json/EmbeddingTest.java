package net.masterthought.cucumber.json;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
    public void getFileName_ReturnsFileName() {

        // given
        Embedding embedding = new Embedding("text/xml", "some data");

        // when
        String fileName = embedding.getFileName();

        // then
        assertThat(fileName).isEqualTo("embedding_-642587818.xml");
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
    public void getExtension__OnApplicationPdfMimeType_ReturnsPdf() {

        // given
        Embedding embedding = new Embedding("application/pdf", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("pdf");
    }

    @Test
    public void getExtension__OnImagePngMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("image/png", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("png");
    }

    @Test
    public void getExtension__OnImageGifMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("image/gif", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("gif");
    }

    @Test
    public void getExtension__OnImageBmpMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("image/bmp", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("bmp");
    }

    @Test
    public void getExtension__OnImageJpegMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("image/jpeg", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("jpeg");
    }

    @Test
    public void getExtension__OnTextHtmlMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("text/html", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("html");
    }

    @Test
    public void getExtension__OnTextXmlMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("text/xml", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("xml");
    }

    @Test
    public void getExtension__OnApplicationJsonMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("application/json", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("json");
    }

    @Test
    public void getExtension__OnApplicationXmlMimeType_ReturnsPng() {

        // given
        Embedding embedding = new Embedding("application/xml", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("xml");
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

    @Test
    public void hashCode_ReturnHashCode() {
        Embedding embedding = new Embedding("js", "");
        assertEquals(-630454186, embedding.hashCode());
    }

    @Test
    public void equals_ReturnTrueSameInstance() throws Exception {
        Embedding embedding1 = new Embedding("js", "");
        Embedding embedding2 = embedding1;
        assertTrue(embedding1.equals(embedding2));
    }

    @Test
    public void equals_ReturnFalseNotSameValue() throws Exception {
        Embedding embedding1 = new Embedding("js", "");
        Embedding embedding2 = new Embedding("js", "var pupper = \"good boy\";");
        assertFalse(embedding1.equals(embedding2));
    }

    @Test
    public void equals_ReturnTrueSameValue() throws Exception {
        Embedding embedding1 = new Embedding("js", "");
        Embedding embedding2 = new Embedding("js", "");
        assertTrue(embedding1.equals(embedding2));
    }

    @Test
    public void equals_ReturnFalseNotAnInstanceOf() throws Exception {
        Embedding embedding = new Embedding("js", "");
        assertFalse(embedding.equals(new Step()));
    }
}
