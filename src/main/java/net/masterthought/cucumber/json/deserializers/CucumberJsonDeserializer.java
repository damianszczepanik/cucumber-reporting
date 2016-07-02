package net.masterthought.cucumber.json.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import net.masterthought.cucumber.Configuration;

/**
 * Abstract deserializer that extracts {@link Configuration} and passes to
 * {@link #deserialize(JsonParser, Configuration)}.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
abstract class CucumberJsonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        Configuration configuration = (Configuration) context.findInjectableValue(Configuration.class.getName(), null,
                null);
        return deserialize(parser, configuration);
    }

    protected abstract T deserialize(JsonParser parser, Configuration configuration)
            throws IOException;
}
