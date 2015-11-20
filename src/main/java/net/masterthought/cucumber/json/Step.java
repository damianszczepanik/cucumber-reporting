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

    public DocString getDocString() {
        return doc_string;
    }

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

    public Object[] getEmbeddings() {
        return embeddings;
    }

    public boolean hasRows() {
        return ArrayUtils.isNotEmpty(rows);
    }

    public Status getStatus() {
        if (result == null) {
            return Status.MISSING;
        } else {
            return Status.valueOf(result.getStatus().toUpperCase());
        }
    }

    public long getDuration() {
        if (result == null) {
            return 0L;
        } else {
            return result.getDuration();
        }
    }

    public String getDataTableClass() {
        String content = "";
        Status status = getStatus();
        if (status == Status.FAILED || status == Status.PASSED || status == Status.SKIPPED) {
            content = status.getName();
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
        sb.append("<div class=\"").append(status.getName().toLowerCase()).append("\">");
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
        sb.append(getAttachments());

        return sb.toString();
    }

    /**
     * Returns a formatted doc-string section. This is formatted w.r.t the parent Step element. To preserve whitespace
     * in example, line breaks and whitespace are preserved
     *
     * @return string of html
     */
    public String getDocStringOrNothing() {
        if (doc_string == null || !doc_string.hasValue()) {
            return "";
        }
        
        return "<div class=\"" + getStatus().getName().toLowerCase() + "\">" +
                "<div class=\"doc-string\">" +
                getDocString().getEscapedValue() +
                "</div></div>";
    }
    
    private String formatError(String errorMessage) {
        String result = errorMessage;
        if (errorMessage != null && !errorMessage.isEmpty()) {
            if (errorMessage.startsWith( "<" )) {
                result = errorMessage.replaceAll( "\\\\n", "<br/>" );
            } else {
                int id = ++labelCount;
                String[] headLineAndTrace = StringUtils.split( errorMessage , "\n", 2 );
                result = String.format( "<input class=\"collapse\" id=\"_%d\" type=\"checkbox\"><label for=\"_%d\">%s</label><div>%s</div>", id, id, headLineAndTrace[0], headLineAndTrace[1].replaceAll( "\n", "<br/>" ) );
            }
        }
        return result;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setMedaData(Scenario scenario) {
        calculateAttachments();
        calculateOutputs();
    }

    private void calculateAttachments() {
        StringBuilder sb = new StringBuilder();
        if (embeddings != null) {
            for (int i = 0; i < embeddings.length; i++) {
                sb.append(embeddings[i].render(i));
            }
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
}
