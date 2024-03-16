package com.sheywesk.aop.example.logs.config;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class Utils {

    /**
     * Determines the log level based on the type of response.
     *
     * @param response The response object to analyze.
     * @return The log level ("ERROR" for Throwable, "INFO" otherwise).
     */
    protected static String getLogLevel(Object response) {
        return response instanceof Throwable ? "ERROR" : "INFO";
    }

    /**
     * Sanitizes the response object, extracting the error message if it's a Throwable.
     *
     * @param response The response object to sanitize.
     * @return The sanitized response object (message for Throwable, response itself otherwise).
     */
    protected static Object sanitizeResponse(Object response) {
        return response instanceof Throwable ? ((Throwable) response).getMessage() : response;
    }

    /**
     * Generates a stack trace string if the response is a Throwable.
     *
     * @param response The response object to extract the stack trace from.
     * @return The stack trace as a string, or null if the response is not a Throwable.
     */
    protected static String getStacktrace(Object response) {
        return response instanceof Throwable ? Arrays.toString(((Throwable) response).getStackTrace()) : null;
    }

    /**
     * Calculates the elapsed time since the provided start time.
     *
     * @param start The start time of the operation.
     * @return The elapsed time formatted as milliseconds.
     */
    protected static String calculateElapsed(Instant start) {
        return Duration.between(start, Instant.now()).toMillis() + " ms";
    }
}
