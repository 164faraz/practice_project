package com.springdemo.project.v1.service;

import com.springdemo.project.v1.entity.Student;
import com.springdemo.project.v1.entity.StudentNode;
import com.springdemo.project.v1.repository.StudentNodeRepository;
import com.springdemo.project.v1.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedListPersistenceService {

    private final StudentRepository studentRepository;
    private final StudentNodeRepository nodeRepository;

    public LinkedListPersistenceService(StudentRepository studentRepository, StudentNodeRepository nodeRepository) {
        this.studentRepository = studentRepository;
        this.nodeRepository = nodeRepository;
    }

    /**
     * Persist a MyStudentList into the DB as a chain of StudentNode rows.
     * Returns the id of the head StudentNode (or null if list empty).
     *
     * Strategy:
     * - Traverse the in-memory list and ensure each Student entity is persisted (save if needed).
     * - Create a StudentNode for each Student and save in sequence. After saving a node, update the previous node's "next" to point to it and save the previous node.
     */
    public Long saveList(MyStudentList list) {
        List<Student> students = list.toList();
        if (students.isEmpty()) return null;

        Long headId = null;
        StudentNode prevSaved = null;

        for (Student s : students) {
            // ensure student row exists
            Student persisted = studentRepository.findById(s.getRollNo()).orElseGet(() -> studentRepository.save(s));

            StudentNode node = new StudentNode();
            node.setStudent(persisted);
            node.setNext(null);

            // save node first so we have an id for linking
            StudentNode saved = nodeRepository.save(node);

            if (prevSaved != null) {
                prevSaved.setNext(saved);
                nodeRepository.save(prevSaved);
            } else {
                headId = saved.getId();
            }

            prevSaved = saved;
        }

        return headId;
    }

    /**
     * Load a MyStudentList starting from head StudentNode id. Returns an empty MyStudentList if headId is null or not found.
     */
    public MyStudentList loadList(Long headId) {
        if (headId == null) return new MyStudentList();
        StudentNode current = nodeRepository.findById(headId).orElse(null);
        if (current == null) return new MyStudentList();

        List<Student> students = new ArrayList<>();
        while (current != null) {
            students.add(current.getStudent());
            current = current.getNext();
        }
        return MyStudentList.fromList(students);
    }
}
