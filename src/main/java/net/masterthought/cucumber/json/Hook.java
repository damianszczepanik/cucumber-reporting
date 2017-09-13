package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.Resultsable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Hook implements Resultsable {

    private String id = null;
    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;

    // foe Ruby reports
    private final Embedding[] embeddings = new Embedding[0];
    // End: attributes from JSON file report

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    public Embedding[] getEmbeddings() {
        return embeddings;
    }

    @Override
    public String getId() {return id;}

    @Override
    public void setId(String id) { this.id = id; }

    @Override
    public String generateId(Element parentElement) {
        // using hashCode() in absence of preferred class-member 'line' which is only present in Step class
        return parentElement.getIndex() + "-hook-" + Math.abs(hashCode());
    }

    @Override
    public String getResultableName() { return getMatch().getLocation(); }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(result).append(match).append(embeddings).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Hook)) { return false; }
        if(obj == this) { return true; }
        Hook hook = (Hook) obj;
        return new EqualsBuilder().
                append(this.result, hook.result).
                append(this.match, hook.match).
                append(this.embeddings, hook.embeddings).
                isEquals();
    }
}
