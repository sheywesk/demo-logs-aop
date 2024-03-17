package com.sheywesk.aop.example.logs.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"application", "correlation_id", "start_time", "elapsed", "log_level", "class_name", "method", "request", "response", "stacktrace"})
class HistoryModel {
    private String application;

    @JsonProperty("log_level")
    private String logLevel;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("elapsed")
    private String elapsed;

    @JsonProperty("class_name")
    private String className;

    private String method;

    @JsonProperty("correlation_id")
    private String correlationId;

    private Object request;

    private Object response;

    private String stacktrace;
}
