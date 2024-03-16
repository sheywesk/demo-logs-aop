package com.sheywesk.aop.example.logs.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class HttpServeletHelper {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    /**
     * Transform the headers sent to the controller into a map.
     *
     * @return headers transformed into Map<String,String>
     */
    protected Map<String, String> getHeaders() {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        request::getHeader
                ));
    }

    /**
     * Retrieve the status code of the response from the controller
     * using the servlet API.
     *
     * @return string containing the status
     */
    protected String getStatusCode() {
        return String.valueOf(response.getStatus());
    }

    /**
     * Retrieve the correlation ID from the request received by the controller.
     *
     * @return String containing correlationID or null
     */
    protected String getCorrelationId() {
        return Optional.ofNullable(request.getHeader("correlation-id"))
                .map(Object::toString)
                .orElse(null);
    }
}
