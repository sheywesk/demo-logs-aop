package com.sheywesk.aop.example.logs.dto;

import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;

public record ExampleDTO(@EncryptLogger String example) {
}
