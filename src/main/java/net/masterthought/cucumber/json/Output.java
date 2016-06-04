package net.masterthought.cucumber.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.masterthought.cucumber.json.deserializers.OutputDeserializer;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@JsonDeserialize(using = OutputDeserializer.class)
public class Output {

    private final String[] messages;

    public Output(String[] messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
