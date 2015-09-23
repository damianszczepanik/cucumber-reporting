package net.masterthought.cucumber.json;

import org.codehaus.plexus.util.Base64;

/**
 * @author Damian Szczepanik <damianszczepanik@github>
 */
public class Embedded {

    private final String mime_type = null;
    private final String data = null;

    public String render(int index) {
        StringBuilder sb = new StringBuilder();

        int contentId = data.hashCode();

        switch (mime_type) {
        case "image/png":
            sb.append(publishImg("png", contentId, index));
            break;
        case "image/bmp":
            sb.append(publishImg("bmp", contentId, index));
            break;
        case "text/plain":
            sb.append(publishPlainType(contentId, index));
            break;
        case "text/html":
            sb.append(publishHTMLType(contentId, index));
            break;
        default:
            sb.append(publishUnknownType(mime_type, index));
        }
        return sb.toString();
    }

    private static String getImg(int imageId, String mimeEncodedImage) {
        return String.format("<img id=\"%s\" src=\"%s\" style=\"max-width:250px; display:none;\"/>", imageId,
                mimeEncodedImage);
    }


    private String publishImg(String imgType, int imageId, int index) {
        String mimeEncodedImage = "data:image/" + imgType + ";base64," + data;

        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(imageId, "Screenshot", index));
        sb.append(String.format("<a href=\"%s\">", mimeEncodedImage));
        sb.append(getImg(imageId, mimeEncodedImage));
        sb.append("</a></br>");

        return sb.toString();
    }

    private String publishPlainType(int contentId, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(getExpandAnchor(contentId, "Plain text", index));
        sb.append(String.format("<pre id=\"%s\" style=\"max-width:250px; display:none;\">%s</pre><br>", contentId,
                decodeDataFromBase()));
        return sb.toString();
    }

    private String publishHTMLType(int contentId, int index) {
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

    private static String getExpandAnchor(int contentId, String label, int index) {
        return String.format(
                "<a onclick=\"attachment=document.getElementById('%s'); attachment.style.display = (attachment.style.display == 'none' ? 'block' : 'none');return false\">%s %s</a>",
                contentId, label, index);
    }

    private String decodeDataFromBase() {
        return new String(Base64.decodeBase64(data.getBytes()));
    }
}
