package net.masterthought.cucumber.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Embedding;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Deserializes embedding.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingDeserializer extends CucumberJsonDeserializer<Embedding> {

    @Override
    public Embedding deserialize(JsonNode rootNode, Configuration configuration) {
        String data = rootNode.get("data").asText();
        String mimeType = findMimeType(rootNode);

        String encodedData = getBase64EncodedData(data);

        Embedding embedding;
        String nameField = "name";
        if (rootNode.has(nameField)) {
            String name = rootNode.get(nameField).asText();
            embedding = new Embedding(mimeType, encodedData, name);
        } else {
            embedding = new Embedding(mimeType, encodedData);
        }

        return embedding;
    }

    private String getBase64EncodedData(String data) {
        try {
            // If we can successfully decode the data we consider it to be base64 encoded,
            // so we do not need to do anything here
            Base64.getDecoder().decode(data);
            return data;
        } catch (IllegalArgumentException e) {
            // decoding failed, therefore we consider the data not to be encoded,
            // so we need to encode it
            return new String(Base64.getEncoder().encode(data.getBytes(UTF_8)), UTF_8);
        }
    }

    private String findMimeType(JsonNode rootNode) {
        JsonNode media = rootNode.get("media");

        if (media != null) {
            return media.get("type").asText();
        }

        return rootNode.get("mime_type").asText();
    }

}
