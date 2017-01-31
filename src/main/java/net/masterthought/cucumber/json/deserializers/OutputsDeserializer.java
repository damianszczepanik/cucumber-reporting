package net.masterthought.cucumber.json.deserializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Output;

public class OutputsDeserializer extends CucumberJsonDeserializer<Output[]> {

    @Override
    protected Output[] deserialize(JsonNode rootNode, Configuration configuration) throws IOException {
        List<Output> outputs = new ArrayList<>();
        if (rootNode.get(0).isArray()) {
            for (JsonNode outputNode : rootNode) {
                outputs.add(getOutput(outputNode));
            }
        } else {
            outputs.add(getOutput(rootNode));
        }

        return outputs.toArray(new Output[outputs.size()]);
    }

    private Output getOutput(JsonNode outputNode) {
        List<String> messages = new ArrayList<>();
        for (JsonNode messageNode : outputNode) {
            messages.add(messageNode.asText());
        }
        return new Output(messages.toArray(new String[messages.size()]));
    }
}
