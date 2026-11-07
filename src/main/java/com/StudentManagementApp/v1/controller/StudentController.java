package com.StudentManagementApp.v1.controller;

import com.StudentManagementApp.v1.entity.Student;
import com.StudentManagementApp.v1.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{rollNo}")
    public Student getStudent(@PathVariable Integer rollNo) {
        return service.getStudentDetail(rollNo);
    }

    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        return service.saveNewEntryOfStudent(student);
    }

    @PutMapping("/{rollNo}")
    public Student updateStudent(@PathVariable Integer rollNo,
                                 @Valid @RequestBody Student student) {
        if (!rollNo.equals(student.getRollNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Path roll number must match request body roll number");
        }
        return service.updateStudentRecord(rollNo, student);
    }

    @DeleteMapping("/{rollNo}")
    public String delete(@PathVariable Integer rollNo) {
        service.deleteStudentRecordById(rollNo);
        return "Deleted";
    }
}
