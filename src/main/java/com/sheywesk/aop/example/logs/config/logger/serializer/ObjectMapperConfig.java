package com.sheywesk.aop.example.logs.config.logger.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    @Qualifier("mapperWithEncrypt")
    public ObjectMapper mapperWithEncrypt() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new EncryptSerializerModifier());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
