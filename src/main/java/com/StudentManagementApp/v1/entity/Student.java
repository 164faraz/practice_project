package com.StudentManagementApp.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @NotNull(message = "Roll number cannot be null")
    @Min(value = 1, message = "Roll number must be greater than 0")
    @Column(name = "roll_no")
    private Integer rollNo;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, message = "Name must have at least 2 characters")
    @Column(name = "student_name")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Course cannot be empty")
    @Column(name = "course")
    private String course;

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}
