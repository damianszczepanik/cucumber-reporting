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

    private String attachments;
    private String convertedOutput;
    private Status status;

    public Row[] getRows() {
        return rows;
    }

    public String getOutput() {
        return convertedOutput;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public Result getResult() {
        return result;
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

    public String getDetails() {
        String errorMessage = result == null ? null : result.getErrorMessage();
        if (status == Status.MISSING) {
            errorMessage = "<span class=\"missing\">Result was missing for this step</span>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"").append(status.getRawName()).append("\">");
        sb.append("<span class=\"step-keyword\">").append(keyword).append(" </span>");
        sb.append("<span class=\"step-name\">");
        // for keyword == Before|After attribute 'name' is not available
        if (StringUtils.isNotBlank(name)) {
            sb.append(StringEscapeUtils.escapeHtml(name));
        }
        sb.append("</span>");

        sb.append("<span class=\"step-duration\">");
        if (status != Status.MISSING && result != null) {
            sb.append(Util.formatDuration(result.getDuration()));
        }
        sb.append("</span>");

        if (StringUtils.isNotBlank(errorMessage)) {
            // if the result is not available take a hash of message reference - not perfect but still better than -1
            int id = result != null ? result.hashCode() : errorMessage.hashCode();
            sb.append(Util.formatMessage(errorMessage, id));
        }
        sb.append("</div>");

        return sb.toString();
    }

    /**
     * Returns a formatted doc-string section. This is formatted w.r.t the parent Step element. To preserve whitespace
     * in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocString() {
        if (doc_string == null || StringUtils.isBlank(doc_string.getValue())) {
            return StringUtils.EMPTY;
        }

        return "<div class=\""
                + status.getRawName() + "\">" + "<div class=\"doc-string\">" + StringEscapeUtils
                        .escapeHtml(doc_string.getValue()).replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;")
                + "</div></div>";
    }

    @Override
    public String getAttachments() {
        return attachments;
    }

    public void setMedaData(Element element) {
        calculateAttachments();
        calculateOutputs();
        calculateStatus();
    }

    private void calculateAttachments() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < embeddings.length; i++) {
            sb.append(embeddings[i].render(i));
        }
        if (embeddings.length > 0) {
            sb.append("<br>");
        }
        attachments = sb.toString();
    }

    private void calculateOutputs() {
        List<String> list = new ArrayList<>();
        for (int i=0; i< output.length; i++) {
            JsonElement element = this.output[i];
            // check if passed output is one or two dimensions array
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                list.add(element.getAsString());
            } else {
                list.add(element.toString());
            }
        }
        convertedOutput = Util.formatMessage(StringUtils.join(list, "\n"), hashCode());
    }

    private void calculateStatus() {
        if (result == null) {
            status = Status.MISSING;
        } else {
            status = Status.toStatus(result.getStatus());
        }
    }
}
