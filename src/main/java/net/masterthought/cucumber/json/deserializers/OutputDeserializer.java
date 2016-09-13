package net.masterthought.cucumber.json.deserializers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.Output;

/**
 * Deserialize output messages depends supporting single and bi-dimensional arrays (JVM and Ruby implementations).
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OutputDeserializer extends CucumberJsonDeserializer<Output> {

    @Override
    public Output deserialize(JsonNode rootNode, Configuration configuration) {
        List<String> list = parseOutput(rootNode);

        return new Output(list.toArray(new String[list.size()]));
    }

    /**
     * Converts passed node to list of strings. It supports single node and an array working as recursive method.
     *
     * @param node node that should be parsed
     * @return extracted strings
     */
    private List<String> parseOutput(JsonNode node) {
        List<String> list = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode innerNode : node) {
                list.addAll(parseOutput(innerNode));
            }
        } else {
            list.add(node.asText());
        }

        return list;
    }
}
