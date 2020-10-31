package net.masterthought.cucumber.json;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

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
        // prepare/ensure switch-case matching
        String mime = mimeType;
        // remove subtype's suffix (if existing)
        if (mime.contains("+")) mime = mime.substring(0, mime.indexOf('+'));
        // remove subtype's parameter (if existing)
        if (mime.contains(";")) mime = mime.substring(0, mime.indexOf(';'));
        // normalise
        mime = mime.toLowerCase(Locale.ENGLISH).trim();

        switch (mime) {
            case "image/png":
            case "image/gif":
            case "image/bmp":
            case "image/jpeg":
            case "image/svg":
            case "text/html":
            case "text/xml":
            case "text/csv":
            case "application/json":
            case "application/pdf":
            case "application/xml":
            case "application/zip":
            case "video/mp4":
                return mime.substring(mime.indexOf('/') + 1);
            // image available remotely stored as link/url
            case "image/url":
                return "image";
            case "text/plain":
                return "txt";
            case "application/x-tar":
                return "tar";
            case "application/x-bzip2":
                return "bz2";
            case "application/gzip":
                return "gz";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "xlsx";
            case "application/vnd.ms-excel":
                return "xls";
            default:
                return "unknown";
        }
    }
}
