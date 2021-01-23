package net.masterthought.cucumber.json;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
@RunWith(Parameterized.class)
@Ignore // TODO: https://github.com/damianszczepanik/cucumber-reporting/pull/978/files
public class EmbeddingTest {

    private static final String NO_DECODING = null;

    @Parameters(name = "\"{0}\" with \"{1}\"")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
            { "application/ecmascript", "console.log('Hello world');", NO_DECODING, ".es" },
            { "application/gzip", "c29tZSBkYXRh", "some data", "embedding_-1003041823.gz" },
            { "application/javascript", "alert('Hello World!');", NO_DECODING, ".js" },
            { "application/json", "c29tZSBkYXRh", "some data", "embedding_-1003041823.json" },
            { "application/pdf", "c29tZSBkYXRh", "some data", "embedding_-1003041823.pdf" },
            { "application/vnd.ms-excel", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xls" },
            { "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xlsx" },
            { "application/vnd.tcpdump.pcap", "c29tZSBkYXRh", "some data", "embedding_-1003041823.unknown" },
            { "application/xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xml" },
            { "application/xslt+xml", "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" />", NO_DECODING, ".xslt" },
            { "application/x-bzip2", "c29tZSBkYXRh", "some data", "embedding_-1003041823.bz2" },
            { "application/x-tar", "c29tZSBkYXRh", "some data", "embedding_-1003041823.tar" },
            { "application/zip", "c29tZSBkYXRh", "some data", "embedding_-1003041823.zip" },
            { "image/bmp", "c29tZSBkYXRh", "some data", "embedding_-1003041823.bmp" },
            { "image/gif", "c29tZSBkYXRh", "some data", "embedding_-1003041823.gif" },
            { "image/jpeg", "c29tZSBkYXRh", "some data", "embedding_-1003041823.jpeg" },
            { "image/png", "c29tZSBkYXRh", "some data", "embedding_-1003041823.png" },
            { "image/svg", "c29tZSBkYXRh", "some data", "embedding_-1003041823.svg" },
            { "image/svg+xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.svg" },
            { "image/url", "c29tZSBkYXRh", "some data", "embedding_-1003041823.image" },
            { "js", "c29tZSBkYXRh", "some data", "embedding_-1003041823.unknown" },
            { "mime/type", "your data", NO_DECODING, ".type" },
            { "mime/type", "ZnVuY3Rpb24gbG9nZ2VyKG1lc3NhZ2UpIHsgIH0=", "function logger(message) {  }", ".type" },
            { "my mime TYPE", "abc", NO_DECODING, ".unknown" },
            { "text/html", "<html />", NO_DECODING, ".html" },
            { "text/html; charset=UTF-8", "c29tZSBkYXRh", "some data", "embedding_-1003041823.html" },
            { "text/php", "echo 'Hello World!';", NO_DECODING, ".php" },
            { "text/plain", "c29tZSBkYXRh", "some data", "embedding_-1003041823.txt" },
            { "text/xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xml" },
            { "video/mp4", "c29tZSBkYXRh", "some data", "embedding_-1003041823.mp4" },
        });
    }

    @Parameter(0)
    public String mimeType;

    @Parameter(1)
    public String data;

    @Parameter(2)
    public String decodedData;

    @Parameter(3)
    public String fileName;

    @Test
    public void getMimeType_ReturnsMimeType() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualMimeType = embedding.getMimeType();

        // then
        assertThat(actualMimeType).isEqualTo(this.mimeType);
    }

    @Test
    public void getData_ReturnsContent() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualContent = embedding.getData();

        // then
        assertThat(actualContent).isEqualTo(data);
    }

    @Test
    public void getDecodedData_ReturnsDecodedContent() {
        assumeThat(this.decodedData).isNotEqualTo(NO_DECODING);
        
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualDecodedContent = embedding.getDecodedData();

        // then
        assertThat(actualDecodedContent).isEqualTo(this.decodedData);
    }

    @Test
    public void getFileName_ReturnsFileName() {
        assumeThat(this.fileName).matches("^[^\\.]+\\.[^\\.]+$");

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualFileName = embedding.getFileName();

        // then
        assertThat(actualFileName).isEqualTo(this.fileName);
    }

    @Test
    public void getExtension_ReturnsFileExtension() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualExtension = embedding.getExtension();

        // then
        assertThat(actualExtension).isEqualTo(this.fileName.split("\\.")[1]);
    }

    @Test
    public void getName_ReturnsNull() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualName = embedding.getName();

        // then
        assertThat(actualName).isNull();
    }

}
