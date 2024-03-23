package com.sheywesk.aop.example.logs.config.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sheywesk.aop.example.logs.config.logger.annotation.EncryptLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class Utils {

    protected static String getLogLevel(Object response) {
        return isException(response) ? "ERROR" : "INFO";
    }

    protected static Boolean isController(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.isAnnotationPresent(RestController.class);
    }

    protected static Object sanitizeResponse(Object response, ObjectMapper mapperWithEncypt) throws JsonProcessingException {
        if (isException(response)) {
            return ((Throwable) response).getMessage();
        }
        return fieldToJsonWithEncrypt(response, mapperWithEncypt);
    }

    private static Boolean isException(Object response) {
        return response instanceof Throwable;
    }

    protected static Object sanitizeRequest(ProceedingJoinPoint proceedingJoinPoint, ObjectMapper mapper) {

        Object[] fields = getFieldsFromParameter(proceedingJoinPoint);
        Parameter[] parameters = getParameters(proceedingJoinPoint);

        return IntStream.range(0, parameters.length)
                .mapToObj(index -> {
                    try {
                        if (hasParameterWithEncrypt(parameters[index])) {
                            return encrypt();
                        } else {
                            return fieldToJsonWithEncrypt(fields[index], mapper);
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    private static String fieldToJsonWithEncrypt(Object object, ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    private static Parameter[] getParameters(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        return signature.getMethod().getParameters();
    }

    private static Object[] getFieldsFromParameter(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.getArgs();
    }

    private static boolean hasEncryptAnnotation(Parameter arg) {
        return arg.isAnnotationPresent(EncryptLogger.class);
    }

    private static Boolean hasParameterWithEncrypt(Parameter parameter) {
        return !isNull(parameter) && hasEncryptAnnotation(parameter);
    }

    protected static String calculateElapsed(Instant start) {
        return Duration.between(start, Instant.now()).toMillis() + " ms";
    }

    protected static String getClassName(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.getTarget().getClass().getSimpleName();
    }

    protected static String getMethodName(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.getSignature().getName();
    }

    protected static String getStacktrace(Object response) {
        return response instanceof Throwable ? Arrays.toString(((Throwable) response).getStackTrace()) : null;
    }

    protected static String encrypt() {
        return "\"*****\"";
    }
}
