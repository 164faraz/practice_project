package com.springdemo.project.v1.repository;

import com.springdemo.project.v1.entity.StudentNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNodeRepository extends JpaRepository<StudentNode, Long> {
}
