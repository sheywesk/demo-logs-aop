package com.sheywesk.aop.example.logs.config.logger.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;

import java.util.List;

public class EncryptSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

        for (BeanPropertyWriter writer : beanProperties) {
            if (writer.getAnnotation(EncryptLogger.class) != null) {
                writer.assignSerializer(new EncryptSerializer());
            }
        }
        return beanProperties;
    }
}
