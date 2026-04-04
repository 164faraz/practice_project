package com.springdemo.project.v1.repository;

import com.springdemo.project.v1.entity.Student; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}