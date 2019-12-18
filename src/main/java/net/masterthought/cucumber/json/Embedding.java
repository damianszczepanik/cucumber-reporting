package net.masterthought.cucumber.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.masterthought.cucumber.json.deserializers.EmbeddingDeserializer;
import org.codehaus.plexus.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = EmbeddingDeserializer.class)
public class Embedding {

    // Start: attributes from JSON file report
    private final String mimeType;
    private final String data;
    private final String name;
    // End: attributes from JSON file report

    private final String fileId;

    public Embedding(String mimeType, String data) {
        this(mimeType, data, null);
    }

    public Embedding(String mimeType, String data, String name) {
        this.mimeType = mimeType;
        this.data = data;
        this.name = name;

        this.fileId = "embedding_" + data.hashCode();
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
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
            case "text/csv":
            case "application/json":
            case "application/xml":
            case "application/zip":
            case "video/mp4":
                return mimeType.substring(mimeType.indexOf('/') + 1);
            // image available remotely stored as link/url
            case "image/url":
                return "image";
            case "image/svg+xml":
                return "svg";
            case "text/plain":
                return "txt";
            case "application/pdf":
                return "pdf";
            case "application/x-tar":
                return "tar";
            case "application/x-bzip2":
                return "bz2";
            case "application/gzip":
                return "gz";
            default:
                return "unknown";
        }
    }
}
