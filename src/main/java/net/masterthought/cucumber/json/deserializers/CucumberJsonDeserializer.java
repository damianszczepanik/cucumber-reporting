package net.masterthought.cucumber.json.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import net.masterthought.cucumber.Configuration;

/**
 * Abstract deserializer that extracts {@link Configuration} and passes to
 * {@link #deserialize(JsonNode, Configuration)}.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
abstract class CucumberJsonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        Configuration configuration = (Configuration) context.findInjectableValue(Configuration.class.getName(), null,
                null);
        JsonNode rootNode = parser.getCodec().readTree(parser);

        return deserialize(rootNode, configuration);
    }

    protected abstract T deserialize(JsonNode rootNode, Configuration configuration)
            throws IOException;
}
