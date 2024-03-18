package com.sheywesk.aop.example.logs.config.logger;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggerAOP {

    private final LoggerService loggerService;

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void services() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Around("services() || restController()")
    public Object aroundMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        try {
            Object result = joinPoint.proceed();

            loggerService.processLogs(result, joinPoint, start);
            return result;
        } catch (Exception ex) {
            loggerService.processLogs(ex, joinPoint, start);
            throw ex;
        }
    }
}
