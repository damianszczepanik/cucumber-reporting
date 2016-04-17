package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public String getName() {
        return name;
    }

    public String getKeyword() {
        // step keywords have additional space at the end of string, while others don't
        return keyword.trim();
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

    @Override
    public Status getStatus() {
        return status;
    }

    public long getDuration() {
        return result == null ? 0L : result.getDuration();
    }

    public String getErrorMessage() {
        String errorMessage = result == null ? null : result.getErrorMessage();

        if (StringUtils.isNotBlank(errorMessage)) {
            // if the result is not available take a hash of message reference - not perfect but still better than -1
            int id = result != null ? result.hashCode() : errorMessage.hashCode();
            final String contentId = "errormessage_" + id;
            return Util.formatMessage("Error message", errorMessage, contentId);
        }

        return StringUtils.EMPTY;
    }

    public DocString getDocString() {
        return doc_string;
    }

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
        attachments = sb.toString();
    }

    private void calculateOutputs() {
        List<String> list = new ArrayList<>();
        for (JsonElement element : output) {
            // process two dimensional array
            if (element.isJsonArray()) {
                Iterator<JsonElement> iterator = element.getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    list.add(elementToString(iterator.next()));
                }
            }
            else            {
                list.add(elementToString(element));
            }
        }

        final String contentId = "output_" + hashCode();
        convertedOutput = Util.formatMessage("Output:", StringUtils.join(list, "\n"), contentId);
    }

    private static String elementToString(JsonElement element) {
        // if primitive string, then wrapping "" should be deleted
        if (element.isJsonPrimitive() && !element.getAsJsonPrimitive().isString()) {
            return element.toString();
        } else {
            return element.getAsString();
        }
    }

    private void calculateStatus() {
        if (result == null) {
            status = Status.MISSING;
        } else {
            status = Status.toStatus(result.getStatus());
        }
    }
}
