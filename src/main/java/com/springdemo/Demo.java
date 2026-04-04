package com.springdemo;

class Student {

	String name;
	int age;

	Student(String n, int a){
		name = n;
		age = a;
	}
	public void display() {
		System.out.println(name);
		System.out.println(age);
	}
}

public class Demo {
public static void main(String[] args) {
		
		Student s1 = new Student("Faraz", 28);
		s1.display();
	}
}
