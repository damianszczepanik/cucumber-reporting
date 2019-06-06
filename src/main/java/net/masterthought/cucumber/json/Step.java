package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.deserializers.OutputsDeserializer;
import net.masterthought.cucumber.json.support.Argument;
import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;

public class Step implements Resultsable {

    // Start: attributes from JSON file report
    private String name = null;
    private final String keyword = null;
    // create empty Result for all cases where step has no result
    // - happens for old or different cucumber implementation of the library
    private final Result result = new Result();
    private final Row[] rows = new Row[0];
    // protractor-cucumber-framework - mapping arguments to rows
    @JsonProperty("arguments")
    private final Argument[] arguments = new Argument[0];
    private final Match match = null;
    private final Embedding[] embeddings = new Embedding[0];

    @JsonDeserialize(using = OutputsDeserializer.class)
    @JsonProperty("output")
    private final Output[] outputs = new Output[0];

    @JsonProperty("doc_string")
    private final DocString docString = null;

    // hooks are supported since Cucumber-JVM 3.x.x
    private final Hook[] before = new Hook[0];
    private final Hook[] after = new Hook[0];
    // End: attributes from JSON file report

    private Status beforeStatus;
    private Status afterStatus;

    public Row[] getRows() {
        if (ArrayUtils.getLength(arguments) == 1) {
            return arguments[0].getRows();
        } else if (ArrayUtils.getLength(arguments) > 1) {
            // if this happens then proper support must be added
            throw new UnsupportedOperationException("'arguments' length should be equal to 1");
        } else {
            return rows;
        }
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        // step keywords have additional space at the end of string, while others don't
        return keyword.trim();
    }

    @Override
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

    public Hook[] getBefore() {
        return before;
    }
    
    public Hook[] getAfter() {
        return after;
    }
    
    public Status getBeforeStatus() {
        return beforeStatus;
    }

    public Status getAfterStatus() {
        return afterStatus;
    }

    public void setMetaData() {
        beforeStatus = new StatusCounter(before).getFinalStatus();
        afterStatus = new StatusCounter(after).getFinalStatus();
    }
}
