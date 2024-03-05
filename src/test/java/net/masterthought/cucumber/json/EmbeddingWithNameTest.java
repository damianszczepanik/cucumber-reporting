package net.masterthought.cucumber.json;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
public class EmbeddingWithNameTest {

    private static final String NO_DECODING = null;

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
    public String mimeType;
    public String data;
    public String name;
    public String decodedData;
    public String fileName;

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getMimeType_ReturnsMimeType(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualMimeType = embedding.getMimeType();

        // then
        assertThat(actualMimeType).isEqualTo(this.mimeType);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getData_ReturnsContent(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualContent = embedding.getData();

        // then
        assertThat(actualContent).isEqualTo(data);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getDecodedData_ReturnsDecodedContent(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // This assumeThat will cause 6 tests to be skipped from our 'data' usage
        assumeThat(this.decodedData).isNotEqualTo(NO_DECODING);

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualDecodedContent = embedding.getDecodedData();

        // then
        assertThat(actualDecodedContent).isEqualTo(this.decodedData);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getFileName_ReturnsFileName(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // This assumeThat will cause 6 tests to be skipped from our 'data' usage
        assumeThat(this.fileName).matches("^[^\\.]+\\.[^\\.]+$");

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualFileName = embedding.getFileName();

        // then
        assertThat(actualFileName).isEqualTo(this.fileName);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getExtension_ReturnsFileExtension(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualExtension = embedding.getExtension();

        // then
        assertThat(actualExtension).isEqualTo(this.fileName.split("\\.")[1]);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" into \"{2}\"")
    void getName_ReturnsName(String mimeType, String data, String name, String decodedData, String fileName) {
        initEmbeddingWithNameTest(mimeType, data, name, decodedData, fileName);

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data, this.name);

        // when
        String actualName = embedding.getName();

        // then
        assertThat(actualName).isEqualTo(this.name);
    }

    public void initEmbeddingWithNameTest(String mimeType, String data, String name, String decodedData, String fileName) {
        this.mimeType = mimeType;
        this.data = data;
        this.name = name;
        this.decodedData = decodedData;
        this.fileName = fileName;
    }

}
