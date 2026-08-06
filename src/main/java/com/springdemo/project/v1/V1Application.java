package com.springdemo.project.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springdemo.project.v1.service.MyStudentList;
import com.springdemo.project.v1.entity.Student;

@SpringBootApplication
public class V1Application {

	public static void main(String[] args) {
		SpringApplication.run(V1Application.class, args);

        MyStudentList myStudentList = new MyStudentList(new Student(1, "Faraz Ahmad"));

        myStudentList.getFirstStudentDetails();
        myStudentList.getLastStudentDetails();
        myStudentList.getNumberOfStudents();

        System.out.println("\nLinked List:");
        myStudentList.printList();

        myStudentList.addNewStudent(new Student(2, "Jane Smith"));
        myStudentList.addNewStudent(new Student(3, "Bob Johnson"));

        System.out.println("\nLinked List:");
        myStudentList.printList();

        System.out.println(myStudentList.removeFirstStudent());
        System.out.println(myStudentList.removeLastStudent());


        myStudentList.addStudentAtFirstPlace(new Student(4, "Jane Foster"));
	}

}

