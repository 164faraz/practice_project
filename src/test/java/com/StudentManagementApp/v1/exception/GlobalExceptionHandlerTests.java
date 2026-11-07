package com.StudentManagementApp.v1.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void studentNotFoundReturns404AndMessage() {
        var response = handler.handleStudentNotFound(new StudentNotFoundException(42));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Student not found with roll number: 42", response.getBody().get("error"));
    }

    @Test
    void unexpectedExceptionReturnsGeneric500Message() {
        var response = handler.handleGeneric(new IllegalStateException("database password leaked"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().get("error"));
    }
}
