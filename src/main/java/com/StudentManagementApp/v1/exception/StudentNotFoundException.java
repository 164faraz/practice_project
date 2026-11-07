package com.StudentManagementApp.v1.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Integer rollNo) {
        super("Student not found with roll number: " + rollNo);
    }
}
