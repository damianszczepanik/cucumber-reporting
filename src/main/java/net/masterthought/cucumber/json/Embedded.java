package net.masterthought.cucumber.json;

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
     * Unique number of the attachment on the HTML page. This must be unique to fully support expand/collapse option
     * when the same step is added to the report more than once.
     */
    private static int counter = 0;

    /**
     * Generates HTML code with attachment.
     * 
     * @param index
     *            number of the attachment for given step
     * @return generated HTML page
     */
    public String render(int index) {

        final String uniqueContentId = generateNextUniqueId();

        switch (mime_type) {
        case "image/png":
            return publishImg("png", uniqueContentId, index);
        case "image/bmp":
            return publishImg("bmp", uniqueContentId, index);
        case "text/plain":
            return publishPlainType(uniqueContentId, index);
        case "text/html":
            return publishHTMLType(uniqueContentId, index);
        default:
            return publishUnknownType(mime_type, index);
        }
    }

    private static String getImg(String imageId, String encodedImageContent) {
        return String.format("<img id=\"%s\" src=\"%s\" style=\"max-width:250px; display:none;\"/>", imageId,
                encodedImageContent);
    }


    private String publishImg(String imgType, String imageId, int index) {
        String encodedImageContent = "data:image/" + imgType + ";base64," + data;

        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(imageId, "Screenshot", index));
        sb.append(String.format("<a href=\"%s\">", encodedImageContent));
        sb.append(getImg(imageId, encodedImageContent));
        sb.append("</a></br>");

        return sb.toString();
    }

    private String publishPlainType(String contentId, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(contentId, "Plain text", index));
        sb.append(String.format("<pre id=\"%s\" style=\"max-width:250px; display:none;\">%s</pre><br>", contentId,
                decodeDataFromBase()));
        return sb.toString();
    }

    private String publishHTMLType(String contentId, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(contentId, "HTML text", index));
        sb.append(String.format("<span id=\"%s\" style=\"max-width:250px; display:none;\">%s</span><br>", contentId,
                decodeDataFromBase()));
        return sb.toString();
    }

    private String publishUnknownType(String mimeType, int index) {
        return String.format(
                "<span style=\"color:red\">Attachment number %d, has unsupported mimetype '%s'.<br>File the bug <a href=\"https://github.com/damianszczepanik/cucumber-reporting/issues\">here</a> so support will be added!</span>",
                index, mimeType);
    }

    private static String getExpandAnchor(String contentId, String label, int index) {
        return String.format(
                "<a onclick=\"attachment=document.getElementById('%s'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">%s %d</a>",
                contentId, label, index + 1);
    }

    private String decodeDataFromBase() {
        return new String(Base64.decodeBase64(data.getBytes()));
    }

    private String generateNextUniqueId() {
        counter++;
        return "embedding-" + counter;
    }
}
