package com.sheywesk.aop.example.logs.config.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
final class LoggerService {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAOP.class);
    private final ObjectMapper mapperWithEncrypt;
    private final ObjectMapper objectMapper;
    private final ContextApplicationHelper contextApplicationHelper;

    @Value("${spring.application.name}")
    private String applicationName;

    void processLogs(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start) {
        try {
            LoggerModel loggerModel = this.getLoggerApplicationFromContext();
            Object logObject;

            if (Utils.isController(proceedingJoinPoint)) {
                logObject = processControllerLog(response, proceedingJoinPoint, start, loggerModel);
            } else {
                logObject = createHistoryLog(response, proceedingJoinPoint, start, loggerModel);
            }

            if (logObject != null) {
                var json = objectMapper.writeValueAsString(logObject);
                logger.info(json);
            }
        } catch (Exception ex) {
            logger.error("Error processing logs: " + ex.getMessage());
        }
    }

    private Object processControllerLog(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) throws JsonProcessingException {

        updateControllerLog(response, proceedingJoinPoint, start, loggerModel);
        return loggerModel;
    }

    private Object createHistoryLog(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) throws JsonProcessingException {
        HistoryModel history = createHistory(response, proceedingJoinPoint, start);
        loggerModel.getHistoryModel().add(history);
        return history;
    }

    public void updateControllerLog(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) throws JsonProcessingException {
        loggerModel.update(applicationName,
                getStatusCodeFromRequest(),
                getHeadersFromRequest(),
                Utils.getLogLevel(response),
                start.toString(),
                Utils.calculateElapsed(start),
                Utils.getClassName(proceedingJoinPoint),
                Utils.getMethodName(proceedingJoinPoint),
                getCorrelationIdFromRequest(),
                Utils.sanitizeRequest(proceedingJoinPoint, mapperWithEncrypt),
                Utils.sanitizeResponse(response, mapperWithEncrypt),
                Utils.getStacktrace(response));
    }

    private HistoryModel createHistory(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start) throws JsonProcessingException {
        return HistoryModel.builder()
                .application(applicationName)
                .logLevel(Utils.getLogLevel(response))
                .startTime(start.toString())
                .elapsed(Utils.calculateElapsed(start))
                .className(Utils.getClassName(proceedingJoinPoint))
                .method(Utils.getMethodName(proceedingJoinPoint))
                .correlationId(getCorrelationIdFromRequest())
                .request(Utils.sanitizeRequest(proceedingJoinPoint, mapperWithEncrypt))
                .response(Utils.sanitizeResponse(response, mapperWithEncrypt))
                .stacktrace(Utils.getStacktrace(response))
                .build();
    }

    private Map<String, String> getHeadersFromRequest() {
        return contextApplicationHelper.getHeaders();
    }

    private String getStatusCodeFromRequest() {
        return contextApplicationHelper.getStatusCode();
    }

    private String getCorrelationIdFromRequest() {
        return contextApplicationHelper.getCorrelationId();
    }

    private LoggerModel getLoggerApplicationFromContext() {
        return contextApplicationHelper.getLoggerModelFromContext();
    }
}
