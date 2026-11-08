package com.StudentManagementApp.v1.service;

import com.StudentManagementApp.v1.entity.Student;
import com.StudentManagementApp.v1.exception.StudentNotFoundException;
import com.StudentManagementApp.v1.repository.StudentRepository;
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
        return repo.saveAndFlush(student);
    }

    public Student updateStudentRecord(Integer rollNo, Student updatedStudent) {
        Student existingStudent = repo.findById(rollNo)
                .orElseThrow(() -> new StudentNotFoundException(rollNo));

        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
        existingStudent.setDob(updatedStudent.getDob());
        existingStudent.setCollegeName(updatedStudent.getCollegeName());
        existingStudent.setCourseName(updatedStudent.getCourseName());
        existingStudent.setBranchName(updatedStudent.getBranchName());
        existingStudent.setAdmissionDate(updatedStudent.getAdmissionDate());
        existingStudent.setAddress(updatedStudent.getAddress());
        existingStudent.setPreviousEducation(updatedStudent.getPreviousEducation());
        return repo.save(existingStudent);
    }

    public void deleteStudentRecordById(Integer rollNo) {
        Student existingStudent = repo.findById(rollNo)
                .orElseThrow(() -> new StudentNotFoundException(rollNo));
        repo.delete(existingStudent);
    }
}
