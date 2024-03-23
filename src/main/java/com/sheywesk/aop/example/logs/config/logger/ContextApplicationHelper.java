package com.sheywesk.aop.example.logs.config.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ContextApplicationHelper {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    protected Map<String, String> getHeaders() {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        request::getHeader
                ));
    }

    protected String getStatusCode() {
        return String.valueOf(response.getStatus());
    }

    protected String getCorrelationId() {
        return Optional.ofNullable(request.getHeader("correlation-id"))
                .map(Object::toString)
                .orElse(null);
    }

    protected  LoggerModel getLoggerModelFromContext() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(this::tryGetLoggerModel)
                .orElseThrow(() -> new IllegalStateException("RequestContextHolder.getRequestAttributes() retornou nulo"));
    }

    private  LoggerModel tryGetLoggerModel(RequestAttributes requestAttributes) {
        return Optional.ofNullable((LoggerModel) requestAttributes.getAttribute("logger", RequestAttributes.SCOPE_REQUEST))
                .orElseGet(() -> {
                    LoggerModel logger = new LoggerModel();
                    requestAttributes.setAttribute("logger", logger, RequestAttributes.SCOPE_REQUEST);
                    return logger;
                });
    }
}
