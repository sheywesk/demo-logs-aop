package com.sheywesk.aop.example.logs.services;

import com.sheywesk.aop.example.logs.dto.ExampleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final Example2Service service;

    public ExampleDTO example(String example) {
        var request = new ExampleDTO(example);
        return service.example2(request);
    }
}
