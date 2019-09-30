package net.masterthought.cucumber.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Embedding;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = JsonNode.class)
public class EmbeddingDeserializerTest {

    private static final String TARGET_DIR = "target" + File.separator;
    private Configuration configuration;

    @Before
    public void setUp(){
        final String directoryPath = TARGET_DIR + "cucumber-html-reports/embeddings";
        final File dir = new File(directoryPath);
        if (!dir.exists()) {
            final boolean created = dir.mkdirs();
            if (!created) {
                Assert.fail("Could not create folder " + directoryPath);
            }
        }
        configuration = new Configuration(new File(TARGET_DIR),"TestProject");
    }

    @Test
    public void deserialize_OnEncodedData_returnsEmbeddingWithEncodedData(){
        EmbeddingDeserializer embeddingDeserializer = new EmbeddingDeserializer();
        JsonNode node = mock(JsonNode.class);
        String data = "This String will be Base64 encoded";
        String encodedData = new String(Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
        when(node.get("data")).thenReturn(node);
        when(node.get("mime_type")).thenReturn(node);

        when(node.asText()).thenReturn(encodedData).thenReturn("text/plain");

        Embedding embedding = embeddingDeserializer.deserialize(node, configuration);

        assertEquals("The Decoded Data should be the same as the input Data",data,embedding.getDecodedData());
    }

    @Test
    public void deserialize_OnUnEncodedData_returnsEmbeddingWithEncodedData(){
        EmbeddingDeserializer embeddingDeserializer = new EmbeddingDeserializer();
        JsonNode node = mock(JsonNode.class);
        String data = "This String will NOT be Base64 encoded";
        when(node.get("data")).thenReturn(node);
        when(node.get("mime_type")).thenReturn(node);

        when(node.asText()).thenReturn(data).thenReturn("text/plain");

        Embedding embedding = embeddingDeserializer.deserialize(node, configuration);

        assertEquals("The Decoded Data should be the same as the input Data",data,embedding.getDecodedData());
    }

}
