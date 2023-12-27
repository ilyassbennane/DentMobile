package com.dent.dent.controllers;


import com.dent.dent.entities.Student;
import com.dent.dent.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentsController {
    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> findAllstudents(){
        return service.findAll();
    }
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody Student student) {
  
        Student createdStudent = service.create(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("la student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(student);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatestudent(@PathVariable int id, @RequestBody Student newstudent){
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("le student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            newstudent.setId((long) id);
            return ResponseEntity.ok(service.update(newstudent));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Student loginRequest) {
        String enteredEmail = loginRequest.getLogin();
        String enteredPassword = loginRequest.getPassword();
        System.out.println("Received login request: " + enteredEmail + ", " + enteredPassword);
    
        // Assuming authenticateUser returns a Student object
        Student authenticatedStudent = service.authenticateUser(enteredEmail, enteredPassword);
    
        if (authenticatedStudent != null) {
            long studentId = authenticatedStudent.getId(); // Get the student ID from the authenticated student
            System.out.println("Authentication successful for: " + enteredEmail);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Login successful!", "id", studentId));
        } else {
            System.out.println("Authentication failed for: " + enteredEmail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }
    
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletestudent(@PathVariable int id){
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("le student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(service.delete(student));
        }
    }








}