package net.masterthought.cucumber.json;

import org.apache.commons.io.Charsets;
import org.codehaus.plexus.util.Base64;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Embedded {

    // Start: attributes from JSON file report
    private final String mime_type = null;
    private final String data = null;
    // End: attributes from JSON file report

    /**
     * Generates HTML code with attachment.
     * 
     * @param index
     *            number of the attachment for given step
     * @return generated HTML page
     */
    public String render(int index) {

        final String contentId = "embedding-" + generateUniqueId();

        switch (mime_type) {
        case "image/png":
            return buildImage("png", contentId, index);
        case "image/bmp":
            return buildImage("bmp", contentId, index);
        case "image/jpeg":
            return buildImage("jpeg", contentId, index);
        case "text/plain":
            return buildPlainText(contentId, index);
        case "text/html":
            return buildhHTML(contentId, index);
        default:
            return buildUnknown(mime_type, index);
        }
    }

    private String buildImage(String imgType, String imageId, int index) {
        String encodedImageContent = "data:image/" + imgType + ";base64," + data;

        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(imageId, "image", index));
        sb.append(String.format("<a href=\"%s\">", encodedImageContent));
        sb.append(String.format("<img id=\"%s\" src=\"%s\" class=\"hidden\"/></a></br>", imageId, encodedImageContent));

        return sb.toString();
    }

    private String buildPlainText(String contentId, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(contentId, "plain text", index));
        sb.append(String.format("<pre id=\"%s\" class=\"hidden\">%s</pre><br>", contentId,
                decodeDataFromBase()));
        return sb.toString();
    }

    private String buildhHTML(String contentId, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(contentId, "HTML text", index));
        sb.append(String.format("<span id=\"%s\" class=\"hidden\">%s</span><br>", contentId,
                decodeDataFromBase()));
        return sb.toString();
    }

    private String buildUnknown(String mimeType, int index) {
        return String.format(
                "<span>Attachment number %d, has unsupported mimetype '%s'.<br>File the bug <a href=\"https://github.com/damianszczepanik/cucumber-reporting/issues\">here</a> so support will be added!</span>",
                index, mimeType);
    }

    private static String getExpandAnchor(String contentId, String label, int index) {
        return String.format(
                "<a onclick=\"attachment=document.getElementById('%s'); attachment.className = (attachment.className == 'hidden' ? 'visible' : 'hidden'); return false\" href=\"#\">Attachment %d (%s)</a>",
                contentId, index + 1, label);
    }

    private String decodeDataFromBase() {
        return new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8)));
    }

    private int generateUniqueId() {
        return super.hashCode();
    }
}
