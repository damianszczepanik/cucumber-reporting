package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.masterthought.cucumber.json.support.Resultsable;

public class Step implements Resultsable {

    // Start: attributes from JSON file report
    private String name = null;
    private final String keyword = null;
    private final Result result = null;
    private final Row[] rows = new Row[0];
    private final Match match = null;
    private final Embedding[] embeddings = new Embedding[0];
    private final Output output = null;
    @JsonProperty("doc_string")
    private final DocString docString = null;
    // End: attributes from JSON file report

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

    public Output getOutput() {
        return output;
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

    public long getDuration() {
        return result.getDuration();
    }

    public DocString getDocString() {
        return docString;
    }
}
