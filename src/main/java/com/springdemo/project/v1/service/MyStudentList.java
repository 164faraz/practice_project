package com.springdemo.project.v1.service;

import com.springdemo.project.v1.entity.Student;
import java.util.ArrayList;
import java.util.List;

public class MyStudentList {

    private Node head;
    private Node tail;
    private int length;

    class Node {
        Student data;  // Object stored in node
        Node next;

        Node(Student data) {
            this.data = data;
        }
    }

    public MyStudentList(Student data) {
        Node newNode = new Node(data);
        head = newNode;
        tail = newNode;
        length = 1;
    }

    public void printList() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public void getFirstStudentDetails() {
        if (head == null) {
            System.out.println("Head: null");
        } else {
            System.out.println("Head: " + head.data);
        }
    }

    public void getLastStudentDetails() {
        if (head == null) {
            System.out.println("Tail: null");
        } else {
            System.out.println("Tail: " + tail.data);
        }
    }

    public void getNumberOfStudents() {
        System.out.println("Length: " + length);
    }
    
    public void emptyStudentList() {
        head = null;
        tail = null;
        length = 0;
    }

    public void addNewStudent(Student data) {
        Node newNode = new Node(data);
        if (length == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        length++;
    }

    public Node removeLastStudent() {
        if (length == 0) return null;
        Node temp = head;
        Node pre = head;
        while(temp.next != null) {
            pre = temp;
            temp = temp.next;
        }
        tail = pre;
        tail.next = null;
        length--;
        if (length == 0) {
            head = null;
            tail = null;
        }
        return temp;
    }

    public void addStudentAtFirstPlace(Student data) {
        Node newNode = new Node(data);
        if (length == 0) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        length++;
    }

    public Node removeFirstStudent() {
        if (length == 0) return null;
        Node temp = head;
        head = head.next;
        temp.next = null;
        length--;
        if (length == 0) {
            tail = null;
        }
        return temp;
    }

    // No-arg constructor to build an empty list
    public MyStudentList() {
        this.head = null;
        this.tail = null;
        this.length = 0;
    }

    // Convert linked list contents to a java.util.List<Student>
    public List<Student> toList() {
        List<Student> list = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            list.add(temp.data);
            temp = temp.next;
        }
        return list;
    }

    // Build a MyStudentList from a List<Student>
    public static MyStudentList fromList(List<Student> students) {
        if (students == null || students.isEmpty()) return new MyStudentList();
        MyStudentList ml = new MyStudentList(students.get(0));
        for (int i = 1; i < students.size(); i++) {
            ml.addNewStudent(students.get(i));
        }
        return ml;
    }

}
