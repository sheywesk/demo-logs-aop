package com.sheywesk.aop.example.logs.config.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class LoggerService {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAOP.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private final HttpServeletHelper httpServeletHelper;

    @Value("${spring.application.name}")
    private String applicationName;

    private Boolean isController(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.isAnnotationPresent(RestController.class);
    }

    protected void processLogs(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start) {
        try {
            LoggerModel loggerModel = getLoggerApplicationFromContext();
            Object logObject;

            if (isController(proceedingJoinPoint)) {
                logObject = createControllerLog(response, proceedingJoinPoint, start, loggerModel);
            } else {
                logObject = createHistoryLog(response, proceedingJoinPoint, start, loggerModel);
            }

            if (logObject != null) {
                logger.info(mapper.writeValueAsString(logObject));
            }
        } catch (Exception ex) {
            logger.error("Error processing logs: " + ex.getMessage());
        }
    }

    private Object createControllerLog(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) {
        logControllerAction(response, proceedingJoinPoint, start, loggerModel);
        return loggerModel;
    }

    private Object createHistoryLog(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) {
        HistoryModel history = createHistory(response, proceedingJoinPoint, start);
        loggerModel.getHistoryModel().add(history);
        return history;
    }

    private LoggerModel getLoggerApplicationFromContext() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(this::tryGetLoggerApplication)
                .orElseThrow(() -> new IllegalStateException("RequestContextHolder.getRequestAttributes() retornou nulo"));
    }

    private LoggerModel tryGetLoggerApplication(RequestAttributes requestAttributes) {
        return Optional.ofNullable((LoggerModel) requestAttributes.getAttribute("logger", RequestAttributes.SCOPE_REQUEST))
                .orElseGet(() -> {
                    LoggerModel logger = new LoggerModel();
                    requestAttributes.setAttribute("logger", logger, RequestAttributes.SCOPE_REQUEST);
                    return logger;
                });
    }

    public void logControllerAction(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start, LoggerModel loggerModel) {
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String logLevel = Utils.getLogLevel(response);
        Object sanitizedResponse = Utils.sanitizeResponse(response);
        String stacktrace = Utils.getStacktrace(response);
        Map<String, String> headers = getHeadersFromRequest();
        String startTime = start.toString();
        String elapsed = Utils.calculateElapsed(start);
        Object[] request = proceedingJoinPoint.getArgs();
        String status = getStatusCodeFromRequest();
        String correlationId = getCorrelationIdFromRequest();
        loggerModel.update(applicationName, status, headers, logLevel, startTime, elapsed, className, methodName, correlationId, request, sanitizedResponse, stacktrace);
    }

    protected HistoryModel createHistory(Object response, ProceedingJoinPoint proceedingJoinPoint, Instant start) {
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object request = proceedingJoinPoint.getArgs();
        String correlationId = getCorrelationIdFromRequest();

        return HistoryModel.builder()
                .application(applicationName)
                .logLevel(Utils.getLogLevel(response))
                .startTime(start.toString())
                .elapsed(Utils.calculateElapsed(start))
                .className(className)
                .method(methodName)
                .correlationId(correlationId)
                .request(request)
                .response(Utils.sanitizeResponse(response))
                .stacktrace(Utils.getStacktrace(response))
                .build();
    }

    private Map<String, String> getHeadersFromRequest() {
        return httpServeletHelper.getHeaders();
    }

    private String getStatusCodeFromRequest() {
        return httpServeletHelper.getStatusCode();
    }

    private String getCorrelationIdFromRequest() {
        return httpServeletHelper.getCorrelationId();
    }
}
