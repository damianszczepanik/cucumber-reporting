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

        final String contentId = "embedding_" + hashCode();

        switch (mime_type) {
        case "image/png":
            return buildImage("png", contentId, index);
        case "image/bmp":
            return buildImage("bmp", contentId, index);
        case "image/jpeg":
            return buildImage("jpeg", contentId, index);
        case "text/plain":
            return buildPlainText("plain text", contentId, index);
        case "text/html":
            return buildhHTML(contentId, index);
        default:
            return buildUnknown(mime_type, contentId, index);
        }
    }

    private String buildImage(String imgType, String contentId, int index) {
        final String encodedImageContent = "data:image/" + imgType + ";base64," + this.data;

        return toExpandable(contentId, index, imgType,
                String.format("<img id=\"%s\" src=\"%s\">", contentId, encodedImageContent));
    }

    private String buildPlainText(String mimeType, String contentId, int index) {
        return toExpandable(contentId, index, mimeType, String.format("<pre>%s</pre>", decodeDataFromBase()));
    }

    private String buildhHTML(String contentId, int index) {
        return toExpandable(contentId, index, "HTML", decodeDataFromBase());
    }

    private String buildUnknown(String mimeType, String contentId, int index) {
        return toExpandable(contentId, index, mimeType,
                "File the <a href=\"https://github.com/damianszczepanik/cucumber-reporting/issues\">bug</a> so support for this mimetype can be added.");
    }

    private static String toExpandable(String contentId, int index, String mimeType, String content) {
        return String.format(
                "<div class=\"embedding\"><a onclick=\"attachment=document.getElementById('%s'); attachment.className = (attachment.className == 'hidden' ? 'visible' : 'hidden'); return false\" href=\"#\">"
                        + "Attachment %d (%s)</a><div id=\"%s\" class=\"hidden\">%s</div></div>",
                contentId, index + 1, mimeType, contentId, content);
    }

    private String decodeDataFromBase() {
        return new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }
}
