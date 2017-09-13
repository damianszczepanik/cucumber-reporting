package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.masterthought.cucumber.json.deserializers.OutputsDeserializer;
import net.masterthought.cucumber.json.support.Resultsable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Step implements Resultsable {

    private String id = null;
    // Start: attributes from JSON file report
    private String name = null;
    private final String keyword = null;
    // create empty Result for all cases where step has no result
    // - happens for old or different cucumber implementation of the library
    private final Result result = new Result();
    private final Row[] rows = new Row[0];
    private final Match match = null;
    private final Embedding[] embeddings = new Embedding[0];
    @JsonDeserialize(using = OutputsDeserializer.class)
    @JsonProperty("output")
    private final Output[] outputs = new Output[0];
    @JsonProperty("doc_string")
    private final DocString docString = null;
    private final Integer line = null;
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

    public Output[] getOutputs() {
        return outputs;
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

    public Integer getLine() { return line ;}

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    @Override
    public String generateId(Element parentElement) { return parentElement.getIndex() + "-step-" + getLine(); }

    @Override
    public String getResultableName() { return getName(); }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(id).
                append(name).
                append(keyword).
                append(result).
                append(rows).append(match).
                append(embeddings).
                append(outputs).
                append(docString).
                append(line).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Step)) { return false; }
        if(obj == this) { return true; }
        Step step = (Step) obj;
        return new EqualsBuilder().
                append(this.id, step.id).
                append(this.rows, step.rows).
                append(this.name, step.name).
                append(this.keyword, step.keyword).
                append(this.outputs, step.outputs).
                append(this.match, step.match).
                append(this.embeddings, step.embeddings).
                append(this.result, step.result).
                append(this.docString, step.docString).
                append(this.line, step.line).
                isEquals();
    }
}
