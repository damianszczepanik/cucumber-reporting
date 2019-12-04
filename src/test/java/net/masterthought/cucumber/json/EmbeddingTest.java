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
    public void getFileName_ReturnsFileName() {

        // given
        Embedding embedding = new Embedding("text/xml", "some data");

        // when
        String fileName = embedding.getFileName();

        // then
        assertThat(fileName).isEqualTo("embedding_-642587818.xml");
    }

    @Test
    public void getFileName_ReturnsFileNameForSVG() {

        // given
        Embedding embedding = new Embedding("image/svg+xml", "some data");

        // when
        String fileName = embedding.getFileName();

        // then
        assertThat(fileName).isEqualTo("embedding_-642587818.svg");
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
    public void getExtension__OnApplicationZipMimeType_ReturnsZip() {

        // given
        Embedding embedding = new Embedding("application/zip", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("zip");
    }

    @Test
    public void getExtension__OnApplicationXTarMimeType_ReturnsTar() {

        // given
        Embedding embedding = new Embedding("application/x-tar", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("tar");
    }

    @Test
    public void getExtension__OnApplicationXBZip2MimeType_ReturnsBZ2() {

        // given
        Embedding embedding = new Embedding("application/x-bzip2", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("bz2");
    }

    @Test
    public void getExtension__OnApplicationGZipMimeType_ReturnsGZ() {

        // given
        Embedding embedding = new Embedding("application/gzip", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("gz");
    }

    @Test
    public void getExtension__OnVideoMp4MimeType_ReturnsMp4() {
        // given
        Embedding embedding = new Embedding("video/mp4", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("mp4");
    }

    @Test
    public void getExtension__OnUnknownType_ReturnsUnknown() {

        // given
        Embedding embedding = new Embedding("js", "");

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("unknown");
    }

    @Test
    public void getName_ReturnsName() {

        // given
        String embeddingName = "embeddingName";
        Embedding embedding = new Embedding("application/pdf", "some data", embeddingName);

        String name = embedding.getName();

        // then
        assertThat(name).isEqualTo(embeddingName);
    }
}
