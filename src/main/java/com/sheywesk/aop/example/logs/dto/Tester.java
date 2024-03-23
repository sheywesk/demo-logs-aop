package com.sheywesk.aop.example.logs.dto;

import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class Tester {
    public String id;
    @EncryptLogger
    public String value;
    public NestedTest nestedTest;
}