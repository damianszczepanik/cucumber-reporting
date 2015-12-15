package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.JsonElement;

import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;

public class Step implements ResultsWithMatch {

    // Start: attributes from JSON file report
    private String name = null;
    private final String keyword = null;
    private final String line = null;
    private final Result result = null;
    private final Row[] rows = new Row[0];
    private final Match match = null;
    private final Embedded[] embeddings = new Embedded[0];
    private final JsonElement[] output = new JsonElement[0];
    private final DocString doc_string = null;
    // End: attributes from JSON file report

    private static int labelCount = 0;
    private String attachments;
    private String[] outputs;
    private Status status;

    public Row[] getRows() {
        return rows;
    }

    public String[] getOutput() {
        return outputs;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Embedded[] getEmbeddings() {
        return embeddings;
    }

    public boolean hasRows() {
        return ArrayUtils.isNotEmpty(rows);
    }

    public Status getStatus() {
        return status;
    }

    public long getDuration() {
        return result == null ? 0L : result.getDuration();
    }

    public String getRawName() {
        return name;
    }

    public String getDetails() {
        Status status = getStatus();
        String errorMessage = null;

        switch (status) {
        case FAILED:
            errorMessage = result.getErrorMessage();
            return getStatusDetails(status, errorMessage);
        case MISSING:
            errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
            return getStatusDetails(status, errorMessage);
        default:
            return getStatusDetails(status, null);
        }
    }

    private String getStatusDetails(Status status, String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"").append(status.getName().toLowerCase()).append("\">");
        sb.append("<span class=\"step-keyword\">").append(keyword).append(" </span>");
        sb.append("<span class=\"step-name\">");
        // for keyword == Before|After attribute 'name' is not available
        if (StringUtils.isNotBlank(name)) {
            sb.append(StringEscapeUtils.escapeHtml(name));
        }
        sb.append("</span>");

        sb.append("<span class=\"step-duration\">");
        if (status != Status.MISSING) {
            sb.append(Util.formatDuration(result.getDuration()));
        }
        sb.append("</span>");

        if (StringUtils.isNotBlank(errorMessage)) {
            // if the result is not available take a hash of message reference - not perfect but still better than -1
            int id = result != null ? result.hashCode() : errorMessage.hashCode();
            sb.append(Util.formatErrorMessage(errorMessage, id));
        }
        sb.append("</div>");
        sb.append(getAttachments());

        return sb.toString();
    }

    /**
     * Returns a formatted doc-string section. This is formatted w.r.t the parent Step element. To preserve whitespace
     * in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocString() {
        if (doc_string == null || !doc_string.hasValue()) {
            return "";
        }
        
        return "<div class=\"" + getStatus().getName().toLowerCase() + "\">" + "<div class=\"doc-string\">"
                + doc_string.getEscapedValue() + "</div></div>";
    }

    @Override
    public String getAttachments() {
        return attachments;
    }

    public void setMedaData(Scenario scenario) {
        calculateAttachments();
        calculateOutputs();
        calculateStatus();
    }

    private void calculateAttachments() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < embeddings.length; i++) {
            sb.append(embeddings[i].render(i));
        }
        attachments = sb.toString();
    }

    private void calculateOutputs() {
        List<String> list = new ArrayList<>();
        for (JsonElement element : this.output) {
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String elementString = element.getAsString();
                list.add(StringEscapeUtils.escapeHtml(elementString));
            } else {
                String elementString = element.toString();
                list.add(StringEscapeUtils.escapeHtml(elementString));
            }
        }
        outputs = list.toArray(new String[list.size()]);
    }

    private void calculateStatus() {
        if (result == null) {
            status = Status.MISSING;
        } else {
            status = Status.valueOf(result.getStatus().toUpperCase());
        }
    }
}
