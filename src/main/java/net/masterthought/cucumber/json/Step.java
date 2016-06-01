package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.JsonElement;

import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.Status;

public class Step implements ResultsWithMatch {

    // Start: attributes from JSON file report
    private String name = null;
    private final String keyword = null;
    private final String line = null;
    private final Result result = null;
    private final Row[] rows = new Row[0];
    private final Match match = null;
    private final Embedding[] embeddings = new Embedding[0];
    private final JsonElement[] output = new JsonElement[0];
    private final DocString doc_string = null;
    // End: attributes from JSON file report

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

    public Embedding[] getEmbeddings() {
        return embeddings;
    }

    @Override
    public Result getResult() {
        return result;
    }

    public boolean hasRows() {
        return ArrayUtils.isNotEmpty(rows);
    }
    public boolean hasDocString() {
        return doc_string!=null;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public long getDuration() {
        return result == null ? 0L : result.getDuration();
    }

    public String getErrorMessage() {
        return result == null ? null : result.getErrorMessage();
    }

    public DocString getDocString() {
        return doc_string;
    }

    public void setMedaData(Element element) {
        calculateOutputs();
        calculateStatus();
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
        convertedOutput = list.isEmpty() ? null : StringUtils.join(list, "\n");
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
