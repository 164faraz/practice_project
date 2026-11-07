package com.StudentManagementApp.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "student")
public class Student {
    @Id @NotNull @Min(1) @Column(name = "roll_no") private Integer rollNo;
    @NotBlank @Size(min = 1) @Column(name = "first_name", nullable = false) private String firstName;
    @NotBlank @Size(min = 1) @Column(name = "last_name", nullable = false) private String lastName;
    @NotBlank @Email @Column(nullable = false) private String email;
    @NotBlank @Column(name = "phone_number", nullable = false) private String phoneNumber;
    @NotNull @Past @Column(name = "date_of_birth", nullable = false) private LocalDate dob;
    @NotBlank @Column(name = "college_name", nullable = false) private String collegeName;
    @NotBlank @Column(name = "course_name", nullable = false) private String courseName;
    @NotBlank @Column(name = "branch_name", nullable = false) private String branchName;
    @NotNull @Column(name = "admission_date", nullable = false) private LocalDate admissionDate;
    @NotBlank @Column(nullable = false, length = 1000) private String address;
    @NotBlank @Column(name = "previous_education", nullable = false, length = 1000) private String previousEducation;

    public Integer getRollNo() { return rollNo; } public void setRollNo(Integer v) { rollNo = v; }
    public String getFirstName() { return firstName; } public void setFirstName(String v) { firstName = v; }
    public String getLastName() { return lastName; } public void setLastName(String v) { lastName = v; }
    public String getName() { return (firstName == null ? "" : firstName) + (lastName == null ? "" : " " + lastName); }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public String getPhoneNumber() { return phoneNumber; } public void setPhoneNumber(String v) { phoneNumber = v; }
    public LocalDate getDob() { return dob; } public void setDob(LocalDate v) { dob = v; }
    public String getCollegeName() { return collegeName; } public void setCollegeName(String v) { collegeName = v; }
    public String getCourseName() { return courseName; } public void setCourseName(String v) { courseName = v; }
    public String getBranchName() { return branchName; } public void setBranchName(String v) { branchName = v; }
    public LocalDate getAdmissionDate() { return admissionDate; } public void setAdmissionDate(LocalDate v) { admissionDate = v; }
    public String getAddress() { return address; } public void setAddress(String v) { address = v; }
    public String getPreviousEducation() { return previousEducation; } public void setPreviousEducation(String v) { previousEducation = v; }
}
