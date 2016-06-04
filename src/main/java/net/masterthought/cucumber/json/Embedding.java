package net.masterthought.cucumber.json;

import java.nio.charset.StandardCharsets;

import org.codehaus.plexus.util.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Embedding {

    // Start: attributes from JSON file report
    @JsonProperty("mime_type")
    private final String mimeType = null;
    private final String data = null;
    // End: attributes from JSON file report

    public String getMimeType() {
        return mimeType;
    }

    public String getData() {
        return data;
    }

    public String getDecodedData() {
        return new String(Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public String getExtension() {
        switch (mimeType) {
        case "image/png":
        case "image/bmp":
        case "image/jpeg":
        case "text/html":
        case "application/json":
            return mimeType.substring(mimeType.indexOf("/") + 1);
        case "text/plain":
            return "txt";
        default:
            return "unknown";
        }
    }
}
