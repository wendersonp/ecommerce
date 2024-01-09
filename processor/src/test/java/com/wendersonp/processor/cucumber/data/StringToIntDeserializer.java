package com.wendersonp.processor.cucumber.data;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class StringToIntDeserializer extends StdDeserializer<Integer> {

    public StringToIntDeserializer() {
        this(null);
    }

    public StringToIntDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        String stringValue = node.asText();

        try {
            var removedDecimalsString = stringValue.replaceAll("\\.(\\d+)", "");
            return Integer.parseInt(removedDecimalsString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("It's not possible to convert string to int: " + stringValue, e);
        }
    }
}

