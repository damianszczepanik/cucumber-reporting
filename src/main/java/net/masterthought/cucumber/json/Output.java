package net.masterthought.cucumber.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Output {

    private final String[] messages;

    public Output(String[] messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(messages).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Output)) { return false; }
        if(obj == this) { return true; }
        Output output = (Output) obj;
        return new EqualsBuilder().
                append(this.messages, output.messages).
                isEquals();
    }
}
