package net.masterthought.cucumber.json.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.Embedding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmbeddingDeserializerTest {

    private static final String RANDOM_DIR = "target" + File.separator + "generated-reports" + File.separatorChar + System.currentTimeMillis() + File.separator;

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        configuration = new Configuration(new File(RANDOM_DIR), "TestProject");

        final String directoryPath = RANDOM_DIR + ReportBuilder.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator() + "/embeddings";
        final File dir = new File(directoryPath);
        if (!dir.exists()) {
            final boolean created = dir.mkdirs();
            if (!created) {
                Assertions.fail("Could not create folder " + directoryPath);
            }
        }
    }

    @Test
    void deserialize_OnEncodedData_returnsEmbeddingWithEncodedData() {

        // given
        EmbeddingDeserializer embeddingDeserializer = new EmbeddingDeserializer();
        JsonNode node = mock(JsonNode.class);
        String data = "This String will be Base64 encoded";
        String encodedData = new String(Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        when(node.get("data")).thenReturn(node);
        when(node.get("mime_type")).thenReturn(node);

        when(node.asText()).thenReturn(encodedData).thenReturn("text/plain");

        // when
        Embedding embedding = embeddingDeserializer.deserialize(node, configuration);

        // then
        assertEquals(data, embedding.getDecodedData(), "The Decoded Data should be the same as the input Data");
    }

    @Test
    void deserialize_OnUnEncodedData_returnsEmbeddingWithEncodedData() {

        // given
        EmbeddingDeserializer embeddingDeserializer = new EmbeddingDeserializer();
        JsonNode node = mock(JsonNode.class);
        String data = "This String will NOT be Base64 encoded";
        when(node.get("data")).thenReturn(node);
        when(node.get("mime_type")).thenReturn(node);

        when(node.asText()).thenReturn(data).thenReturn("text/plain");

        // when
        Embedding embedding = embeddingDeserializer.deserialize(node, configuration);

        // then
        assertEquals(data, embedding.getDecodedData(), "The Decoded Data should be the same as the input Data");
    }

    @Test
    void deserialize_OnUnEncodedDataWithOnlyValidCharsAndWhiteSpaces_returnsEmbeddingWithEncodedData() {

        // given
        EmbeddingDeserializer embeddingDeserializer = new EmbeddingDeserializer();
        JsonNode node = mock(JsonNode.class);
        String data = "This is an normal String";
        when(node.get("data")).thenReturn(node);
        when(node.get("mime_type")).thenReturn(node);

        when(node.asText()).thenReturn(data).thenReturn("text/plain");

        // when
        Embedding embedding = embeddingDeserializer.deserialize(node, configuration);

        // thens
        assertEquals(data, embedding.getDecodedData(), "The Decoded Data should be the same as the input Data");
    }
}
