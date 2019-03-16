package net.masterthought.cucumber.json.deserializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import org.codehaus.plexus.util.Base64;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.json.Embedding;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Deserializes embedding and stores it in attachment directory.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingDeserializer extends CucumberJsonDeserializer<Embedding> {

    @Override
    public Embedding deserialize(JsonNode rootNode, Configuration configuration) {
        String data = rootNode.get("data").asText();
        String mimeType = findMimeType(rootNode);

        Pattern base64Pattern = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
        Embedding embedding;
        if (base64Pattern.matcher(data).matches()) {
            embedding = new Embedding(mimeType, data);
        } else {
            embedding = new Embedding(mimeType, new String(Base64.encodeBase64(data.getBytes(UTF_8)), UTF_8));
        }
        storeEmbedding(embedding, configuration.getEmbeddingDirectory());

        return embedding;
    }

    private String findMimeType(JsonNode rootNode) {
        JsonNode media = rootNode.get("media");

        if (media != null) {
            return media.get("type").asText();
        }

        return rootNode.get("mime_type").asText();
    }

    private void storeEmbedding(Embedding embedding, File embeddingDirectory) {
        Path file = FileSystems.getDefault().getPath(embeddingDirectory.getAbsolutePath(),
                embedding.getFileId() + "." + embedding.getExtension());
        byte[] decodedData = Base64.decodeBase64(embedding.getData().getBytes(UTF_8));
        try {
            Files.write(file, decodedData);
        } catch (IOException e) {
            throw new ValidationException(e);
        }
    }

}
