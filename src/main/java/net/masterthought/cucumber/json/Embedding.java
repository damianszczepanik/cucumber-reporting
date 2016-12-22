package net.masterthought.cucumber.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.masterthought.cucumber.json.deserializers.EmbeddingDeserializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.plexus.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = EmbeddingDeserializer.class)
public class Embedding {

    private static final SecureRandom salt = new SecureRandom();

    // Start: attributes from JSON file report
    private final String mimeType;
    private final String data;
    // End: attributes from JSON file report

    private final String fileId;

    public Embedding(String mimeType, String data) {
        this.mimeType = mimeType;
        this.data = data;

        this.fileId = "embedding_" + data.hashCode() + "_" + salt.nextInt(999999999);
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

    /** Returns encoded and escaped data so it is ready to display in HTML page. */
    public String getEscapedDecodedData() {
        return StringEscapeUtils.escapeHtml(getDecodedData());
    }

    public String getFileName() {
        return fileId + "." + getExtension();
    }

    /** Returns file name without extension. */
    public String getFileId() {
        return fileId;
    }

    public String getExtension() {
        switch (mimeType) {
        case "image/png":
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
