package net.masterthought.cucumber.json;

import static com.googlecode.totallylazy.Option.option;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.Util;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.common.base.Joiner;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

public class Step implements ResultsWithMatch {

    private String name;
    private String keyword;
    private String line;
    private Result result;
    private Row[] rows;
    private Match match;
    private Object[] embeddings;
    private String[] output;
    private DocString doc_string;

    public Step() {
    }

    public DocString getDocString() {
        return doc_string;
    }

    public Row[] getRows() {
        return rows;
    }

    public String getOutput() {
        List<String> outputList = Sequences.sequence(option(output).getOrElse(new String[] {})).realise().toList();
        return Joiner.on("").skipNulls().join(outputList);
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public Result getResult() {
        return result;
    }

    public Object[] getEmbeddings() {
        return embeddings;
    }

    public boolean hasRows() {
        boolean result = false;
        if (rows != null) {
            if (rows.length > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * @return - Returns true if has a sub doc-string, and that doc-string has a value
     */
    public boolean hasDocString() {
        return doc_string != null && doc_string.hasValue();
    }

    public Status getStatus() {
        if (result == null) {
            System.out.println("[WARNING] Line " + line + " : " + "Step is missing Result: " + keyword + " : " + name);
            return Status.MISSING;
        } else {
            return Status.valueOf(result.getStatus().toUpperCase());
        }
    }

    public Long getDuration() {
        if (result == null) {
            return 1L;
        } else {
            return result.getDuration();
        }
    }

    public String getDataTableClass() {
        String content = "";
        Status status = getStatus();
        if (status == Status.FAILED || status == Status.PASSED || status == Status.SKIPPED) {
            content = status.getName();
        } else {
            // TODO: why this goes different
            content = "";
        }
        return content;
    }

    public String getRawName() {
        return name;
    }

    public String getName() {
        String content = "";
        Status status = getStatus();
        if (status == Status.FAILED) {
            String errorMessage = result.getErrorMessage();
            if (status == Status.SKIPPED) {
                errorMessage = "Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span>";
            }
            if (status == Status.UNDEFINED) {
                errorMessage = "Mode: Not Implemented causes Failure<br/><span class=\"undefined\">This step is not yet implemented</span>";
            }
            content =  getStatusDetails(status, errorMessage);
        } else if (status == Status.MISSING) {
            String errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            content = getStatusDetails(status, errorMessage);
        } else {
            content = getStatusDetails(status, null);
        }
        return content;
    }

    private String getStatusDetails(Status status, String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(status.toHtmlClass());
        sb.append("<span class=\"step-keyword\">").append(keyword).append(" </span>");
        sb.append("<span class=\"step-name\">").append(StringEscapeUtils.escapeHtml(name)).append("</span>");

        sb.append("<span class=\"step-duration\">");
        if (status != Status.MISSING) {
            sb.append(Util.formatDuration(result.getDuration()));
        }
        sb.append("</span>");

        if (status == Status.FAILED || status == Status.MISSING) {
            sb.append("<div class=\"step-error-message\"><pre class=\"step-error-message-content\">").append(formatError(errorMessage)).append("</pre></div>");

        }
        sb.append("</div>");
        sb.append(getImageTags());

        return sb.toString();
    }

    /**
     * Returns a formatted doc-string section. This is formatted w.r.t the parent Step element. To preserve whitespace
     * in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocStringOrNothing() {
        if (!hasDocString()) {
            return "";
        }
        return getStatus().toHtmlClass() +
                "<div class=\"doc-string\">" +
                getDocString().getEscapedValue() +
                "</div></div>";
    }

    private String formatError(String errorMessage) {
        String result = errorMessage;
        if (errorMessage != null && !errorMessage.isEmpty()) {
            result = errorMessage.replaceAll("\\\\n", "<br/>");
        }
        return result;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getImageTags() {
        if (noEmbeddedScreenshots()) {
            return EMPTY;
        }

        String links = EMPTY;
        int index = 1;
        for (Object image : embeddings) {
            if (image != null) {
                String mimeEncodedImage = mimeEncodeEmbededImage(image);
                String imageId = UUID.nameUUIDFromBytes(mimeEncodedImage.getBytes()).toString();
                links = links + String.format("<a onclick=\"img=document.getElementById('%s'); img.style.display = (img.style.display == 'none' ? 'block' : 'none');return false\">Screenshot %s</a>"
                    + "<a href=\"%s\" data-lightbox=\"image-1\" data-title=\"%s\">"
                    + "<img id=\"%s\"src=\"%s\" style='max-width: 250px;display:none;' alt=\"This is the title\"/>"
                    + "</a></br>",
                    imageId,index++,mimeEncodedImage,StringEscapeUtils.escapeHtml(name),imageId, mimeEncodedImage);
            }
        }
        return links;
    }

    private boolean noEmbeddedScreenshots() {
        return getEmbeddings() == null;
    }

    public static String mimeEncodeEmbededImage(Object image) {
        return "data:image/png;base64," + ((Map) image).get("data");

    }

    public static String uuidForImage(Object image) {
        return UUID.nameUUIDFromBytes(mimeEncodeEmbededImage(image).getBytes()).toString();
    }

    public static class functions {
        public static Function1<Step, Status> status() {
            return new Function1<Step, Status>() {
                @Override
                public Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }

    public static class predicates {

        public static LogicalPredicate<Step> hasStatus(final Status status) {
            return new LogicalPredicate<Step>() {
                @Override
                public boolean matches(Step step) {
                    return step.getStatus().equals(status);
                }
            };
        }

        public static Function1<Step, Status> status() {
            return new Function1<Step, Status>() {
                @Override
                public Status call(Step step) throws Exception {
                    return step.getStatus();
                }
            };
        }
    }

}
