package com.dent.dent.controllers;

import com.dent.dent.entities.StudentPW;
import com.dent.dent.services.StudentPWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studentpws")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001","http://localhost:5173"})


public class StudentPWController {
    @Autowired
    private StudentPWService service;

    @GetMapping
    public List<StudentPW> findAllStudentPWs(){
        return service.findAll();
    }
    @PostMapping
    public StudentPW createStudentPW(@RequestBody StudentPW StudentPW){
        // StudentPW.setId((long) 0);
        return service.create(StudentPW);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        StudentPW StudentPW = service.findById(id);
        if (StudentPW==null) {
            return new ResponseEntity<Object>("la StudentPW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(StudentPW);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudentPW(@PathVariable int id, @RequestBody StudentPW newStudentPW){
        StudentPW StudentPW = service.findById(id);
        if (StudentPW==null) {
            return new ResponseEntity<Object>("le StudentPW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {

            return ResponseEntity.ok(service.update(newStudentPW));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudentPW(@PathVariable int id){
        StudentPW StudentPW = service.findById(id);
        if (StudentPW==null) {
            return new ResponseEntity<Object>("le StudentPW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(service.delete(StudentPW));
        }
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Object> findPWByStudentId(@PathVariable Long studentId) {
        List<StudentPW> studentPWs = service.findByStudentId(studentId);

        if (studentPWs.isEmpty()) {
            return new ResponseEntity<>("No PW found for student with id: " + studentId, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(studentPWs);
        }
    }



}