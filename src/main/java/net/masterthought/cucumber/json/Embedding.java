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

    private static final String FILE_EXTENSION_PATTERN = "[a-z0-9]+";

    private static final String UNKNOWN_FILE_EXTENSION = "unknown";
    
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

    /**
     * Returns the file extension of this embedding (attachment).
     * 
     * In case the {{@link #getMimeType() embedding's MIME-type} is well-known, the according file extension is returned
     * immediately.
     * 
     * In case the MIME-type is unknown, as a first try the {@link #getName() embedding's name} will be used in order to
     * derive a file extension. If the name contains a file name delimiter (i.e., "{@code .}"), the following characters are
     * used as the file extension as long as they match {@value #FILE_EXTENSION_PATTERN}. As a second try, the MIME-type's
     * subtype will be used in order to derive a file extension (as long as such subtype is given). Similar, the subtype is
     * used as the file extension as long as it matches {@value #FILE_EXTENSION_PATTERN}.
     * 
     * Finally (if neither a file extension is known nor can be derived), the value {@value #UNKNOWN_FILE_EXTENSION} will be
     * returned.
     * 
     * @return the file extension of this embedding (attachment)
     */
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
            // image available remotely stored as link/url
            case "image/url":
                return "image";
            case "text/plain":
                return "txt";
            case "application/ecmascript":
                return "es";
            case "application/javascript":
                return "js";
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
                // assert the name is file-name formatted --> try file-name extension
                if ((name != null) && name.contains(".")) {
                    String extension = name.substring(name.lastIndexOf('.') + 1);
                    // the extension might by usable
                    if (extension.matches(FILE_EXTENSION_PATTERN)) {
                        return extension;
                    }
                }
                // assert the mime-type contains a subtype --> try subtype
                if (mime.contains("/")) {
                    String subtype = mime.substring(mime.indexOf('/') + 1);
                    // the subtype might by usable
                    if (subtype.matches(FILE_EXTENSION_PATTERN)) {
                        return subtype;
                    }
                }
                // if nothing works the extension is unknown
                return UNKNOWN_FILE_EXTENSION;
        }
    }
}
