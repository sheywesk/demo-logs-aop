package com.sheywesk.aop.example.logs.controller;

import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;
import com.sheywesk.aop.example.logs.dto.ExampleDTO;
import com.sheywesk.aop.example.logs.services.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("/api/v1/example")
    public ResponseEntity<ExampleDTO> example(@RequestBody @EncryptLogger ExamplePayload body,@RequestHeader("teste") @EncryptLogger String teste) {

        try {
            var response = exampleService.example(body.payload());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ExampleDTO(ex.getMessage()));
        }
    }
}
