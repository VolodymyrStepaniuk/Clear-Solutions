package com.stepaniuk.clear_solutions.exceptions;

import java.time.LocalDateTime;

public record ApiError(String path,
                       String message,
                       int statusCode,
                       LocalDateTime localDateTime) {
}