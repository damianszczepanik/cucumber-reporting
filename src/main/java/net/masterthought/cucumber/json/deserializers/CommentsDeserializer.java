package net.masterthought.cucumber.json.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import net.masterthought.cucumber.Configuration;

import java.util.ArrayList;
import java.util.List;

public class CommentsDeserializer extends CucumberJsonDeserializer<List<String>> {

    @Override
    protected List<String> deserialize(JsonNode rootNode, Configuration configuration) {
        List<String> comments = new ArrayList<>();
        for (JsonNode commentNode : rootNode) {
            if (commentNode.isTextual()) {
                comments.add(commentNode.asText());
            }
            if (commentNode.isObject() && commentNode.has("value")) {
                comments.add(commentNode.get("value").asText());
            }
        }

        return comments;
    }
}
