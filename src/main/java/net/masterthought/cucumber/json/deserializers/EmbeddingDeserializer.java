package net.masterthought.cucumber.json.deserializers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import org.codehaus.plexus.util.Base64;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.json.Embedding;

/**
 * Deserializes embedding and stores it in attachment directory.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingDeserializer extends CucumberJsonDeserializer<Embedding> {

    @Override
    public Embedding deserialize(JsonNode rootNode, Configuration configuration) {
        String data = rootNode.get("data").asText();
        String mimeType = rootNode.get("mime_type").asText();

        Embedding embedding = new Embedding(mimeType, data);
        storeEmbedding(embedding, configuration.getEmbeddingDirectory());

        return embedding;
    }

    private void storeEmbedding(Embedding embedding, File embeddingDirectory) {
        Path file = FileSystems.getDefault().getPath(embeddingDirectory.getAbsolutePath(),
                embedding.getFileId() + "." + embedding.getExtension());
        byte[] decodedData = Base64.decodeBase64(embedding.getData().getBytes(StandardCharsets.UTF_8));
        try {
            Files.write(file, decodedData);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }

}
