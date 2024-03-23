package com.sheywesk.aop.example.logs.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sheywesk.aop.example.logs.dto.NestedTest;
import com.sheywesk.aop.example.logs.dto.Tester;
import com.sheywesk.aop.example.logs.dto.TesterClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EncryptSerializerTest {
    @Autowired
    private  ObjectMapper mapperWithEncrypt;

    @Test
    void deveDevolverObjetoSerializadoECriptografado() throws JsonProcessingException {
        var nested = new NestedTest("oi");
        var tester = new Tester("id","value",nested);
        var testerClass = new TesterClass(tester);
        ObjectMapper objectMapper = new ObjectMapper();
//        var serializer = withObscureProperty();
        var actualWithEncrypt = mapperWithEncrypt.writeValueAsString(testerClass);
        var actualWithoutEncrypt =  objectMapper.writeValueAsString(testerClass);

        System.out.println(actualWithEncrypt);
        System.out.println(actualWithoutEncrypt);

        assertTrue(actualWithEncrypt.contains("*****"));
        assertFalse(actualWithoutEncrypt.contains("*****"));
        assertEquals(((Tester)testerClass.tester()).value,"value");
    }
}
