package com.sheywesk.aop.example.logs.config.logger;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"application", "correlation_id", "status", "start_time", "elapsed", "log_level", "class_name", "method", "request", "response", "stacktrace", "history"})
public class LoggerModel {

    @JsonProperty("history")
    private final List<HistoryModel> historyModel;
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
    private String status;
    private String stacktrace;
    private Map<String, String> headers;

    public LoggerModel() {
        this.historyModel = new ArrayList<>();
    }

    public void update(String application, String status, Map<String, String> headers, String logLevel, String startTime, String elapsed, String className, String method, String correlationId, Object request, Object response, String stacktrace) {
        this.application = application;
        this.logLevel = logLevel;
        this.startTime = startTime;
        this.elapsed = elapsed;
        this.className = className;
        this.method = method;
        this.correlationId = correlationId;
        this.request = request;
        this.response = response;
        this.stacktrace = stacktrace;
        this.headers = headers;
        this.status = status;
    }

}
