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
public class EmbeddingWithNameTest {

    private static final String NO_DECODING = null;

    @Parameters(name = "\"{0}\" into \"{2}\"")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
            { "application/javascript", "alert('Hello World!');", "hello-world.js", NO_DECODING, ".js" },
            { "application/xhtml+xml; charset=UTF-8", "c29tZSBkYXRh", "data.xhtm", "some data", "embedding_-1003041823.xhtm" },
            { "application/xslt+xml", "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" />", "filename-with-different-extension.xsl", NO_DECODING, ".xsl" },
            { "application/xslt+xml", "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" />", "filename-without-extension", NO_DECODING, ".xslt" },
            { "application/xslt+xml", "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" />", "filename-with.invalid_extension", NO_DECODING, ".xslt" },
            { "text/jscript", "alert('Hello World!');", "hello-world.js", NO_DECODING, ".js" },
            { "text/markdown", "c29tZSBkYXRh", "memo.md", "some data", "embedding_-1003041823.md" },
            { "text/php", "echo 'Hello World!';", "hello-world.php7", NO_DECODING, ".php7" },
        });
    }

    @Parameter(0)
    public String mimeType;

    @Parameter(1)
    public String data;

    @Parameter(2)
    public String name;

    @Parameter(3)
    public String decodedData;

    @Parameter(4)
    public String fileName;

    @Test
    public void getMimeType_ReturnsMimeType() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualMimeType = embedding.getMimeType();

        // then
        assertThat(actualMimeType).isEqualTo(this.mimeType);
    }

    @Test
    public void getData_ReturnsContent() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualContent = embedding.getData();

        // then
        assertThat(actualContent).isEqualTo(data);
    }

    @Test
    public void getDecodedData_ReturnsDecodedContent() {
        assumeThat(this.decodedData).isNotEqualTo(NO_DECODING);
        
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualDecodedContent = embedding.getDecodedData();

        // then
        assertThat(actualDecodedContent).isEqualTo(this.decodedData);
    }

    @Test
    public void getFileName_ReturnsFileName() {
        assumeThat(this.fileName).matches("^[^\\.]+\\.[^\\.]+$");

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualFileName = embedding.getFileName();

        // then
        assertThat(actualFileName).isEqualTo(this.fileName);
    }

    @Test
    public void getExtension_ReturnsFileExtension() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualExtension = embedding.getExtension();

        // then
        assertThat(actualExtension).isEqualTo(this.fileName.split("\\.")[1]);
    }

    @Test
    public void getName_ReturnsName() {
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualName = embedding.getName();

        // then
        assertThat(actualName).isEqualTo(this.name);
    }

}
