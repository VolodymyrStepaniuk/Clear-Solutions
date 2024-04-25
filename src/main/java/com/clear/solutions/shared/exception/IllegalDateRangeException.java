package com.clear.solutions.shared.exception;

import lombok.Getter;

import java.time.LocalDate;

/**
 * Exception thrown when the date range is invalid.
 *
 * @see RuntimeException
 */
@Getter
public class IllegalDateRangeException extends RuntimeException {

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public IllegalDateRangeException(LocalDate fromDate, LocalDate toDate) {
        super("Illegal date range: " + fromDate + " - " + toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}