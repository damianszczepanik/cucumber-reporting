package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.masterthought.cucumber.json.deserializers.OutputsDeserializer;
import net.masterthought.cucumber.json.support.Embedded;
import net.masterthought.cucumber.json.support.Resultsable;
import org.apache.commons.lang.StringUtils;

public class Hook implements Resultsable, Embedded {

    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;

    @JsonDeserialize(using = OutputsDeserializer.class)
    @JsonProperty("output")
    private final Output[] outputs = new Output[0];

    // foe Ruby reports
    private Embedding[] embeddings = new Embedding[0];
    // End: attributes from JSON file report

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public Output[] getOutputs() {
        return outputs;
    }

    @Override
    public Embedding[] getEmbeddings() {
        return embeddings;
    }

    @Override
    public void setEmbeddings(Embedding[] embeddings) {
        this.embeddings = embeddings;
    }

    /**
     * Checks if the hook has content meaning as it has at least attachment or result with error message.
     *
     * @return <code>true</code> if the hook has content otherwise <code>false</code>
     */
    public boolean hasContent() {
        if (hasEmbeddings()) {
            // assuming that if the embedding exists then it is not empty
            return true;
        }
        if (StringUtils.isNotBlank(result.getErrorMessage())) {
            return true;
        }
        // TODO: hook with 'output' should be treated as empty or not?
        return false;
    }

    @Override
    public boolean hasEmbeddings() {
        return embeddings.length > 0;
    }
}
