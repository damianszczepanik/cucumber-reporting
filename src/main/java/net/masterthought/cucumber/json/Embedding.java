package net.masterthought.cucumber.json;

import org.apache.commons.io.Charsets;
import org.codehaus.plexus.util.Base64;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Embedding {

    // Start: attributes from JSON file report
    private final String mime_type = null;
    private final String data = null;
    // End: attributes from JSON file report

    public String getMimeType() {
        return mime_type;
    }

    public String getData() {
        return data;
    }

    public String getDecodedData() {
        return new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }
}
