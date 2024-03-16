package com.sheywesk.aop.example.logs.services;

import com.sheywesk.aop.example.logs.dto.ExampleDTO;
import org.springframework.stereotype.Service;

@Service
public class Example2Service {
    public ExampleDTO example2(ExampleDTO example) {
        return new ExampleDTO(example.example());
    }
}
