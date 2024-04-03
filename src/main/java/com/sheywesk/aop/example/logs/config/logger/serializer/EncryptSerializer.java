package com.sheywesk.aop.example.logs.config.logger.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sheywesk.aop.example.logs.config.logger.Utils;

import java.io.IOException;

public class EncryptSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(Utils.encrypt());
        } else {
            gen.writeNull();
        }
    }
}
