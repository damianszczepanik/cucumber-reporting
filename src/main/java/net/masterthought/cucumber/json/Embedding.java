package net.masterthought.cucumber.json;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.codehaus.plexus.util.Base64;

import net.masterthought.cucumber.json.deserializers.EmbeddingDeserializer;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = EmbeddingDeserializer.class)
public class Embedding {

    // Start: attributes from JSON file report
    private final String mimeType;
    private final String data;
    // End: attributes from JSON file report

    private final String fileId;

    public Embedding(String mimeType, String data) {
        this.mimeType = mimeType;
        this.data = data;

        this.fileId = "embedding_" + data.hashCode();
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getData() {
        return data;
    }

    public String getDecodedData() {
        return new String(Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public String getFileName() {
        return fileId + "." + getExtension();
    }

    /**
     * Returns unique file ID.
     *
     * @return ID of the file
     */
    public String getFileId() {
        return fileId;
    }

    public String getExtension() {
        switch (mimeType) {
        case "image/png":
        case "image/gif":
        case "image/bmp":
        case "image/jpeg":
        case "text/html":
        case "text/xml":
        case "application/json":
        case "application/xml":
            return mimeType.substring(mimeType.indexOf('/') + 1);
        // image available remotely stored as link/url
        case "image/url":
            return "image";
        case "text/plain":
            return "txt";
        default:
            return "unknown";
        }
    }
}
