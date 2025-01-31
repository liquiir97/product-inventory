package com.product.inventory.domain.dto.validation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class StrictLongDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Check if the value is a number
        if (!node.isNumber()) {
            throw ctxt.weirdNumberException(node.numberValue(), Long.class, "Quantity must be a number, not a string.");
        }

        return node.asLong();
    }
}

