package com.sheywesk.aop.example.logs.dto;

import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class NestedTest {
    @EncryptLogger
    public String nested;
}
