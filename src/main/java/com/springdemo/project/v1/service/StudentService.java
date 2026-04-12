package com.springdemo.project.v1.service;

import com.springdemo.project.v1.entity.Student;
import com.springdemo.project.v1.exception.StudentNotFoundException;
import com.springdemo.project.v1.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student getStudentDetail(Integer rollNo) {
        return repo.findById(rollNo)
                .orElseThrow(() -> new StudentNotFoundException(rollNo));
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public Student saveNewEntryOfStudent(Student student) {
        return repo.save(student);
    }

    public Student updateStudentRecord(Integer rollNo, Student updatedStudent) {
        Student existingStudent = repo.findById(rollNo)
                .orElseThrow(() -> new StudentNotFoundException(rollNo));

        existingStudent.setName(updatedStudent.getName());
        return repo.save(existingStudent);
    }

    public void deleteStudentRecordById(Integer rollNo) {
        Student existingStudent = repo.findById(rollNo)
                .orElseThrow(() -> new StudentNotFoundException(rollNo));
        repo.delete(existingStudent);
    }
}