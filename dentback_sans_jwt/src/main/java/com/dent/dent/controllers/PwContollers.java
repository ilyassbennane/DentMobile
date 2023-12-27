package com.dent.dent.controllers;

import com.dent.dent.entities.PW;
import com.dent.dent.services.PwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/pws")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})

public class PwContollers {
    @Autowired
    private PwService service;

    @GetMapping
    public List<PW> findAllPWs(){
        return service.findAll();
    }
    @PostMapping
    public  ResponseEntity<Object>  createPW(@RequestBody PW PW){
        // PW.setId((long) 0);
        service.create(PW);
        return ResponseEntity.ok("PW created successfully");

    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        PW PW = service.findById(id);
        if (PW==null) {
            return new ResponseEntity<Object>("la PW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(PW);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePW(@PathVariable int id, @RequestBody PW newPW){
        PW PW = service.findById(id);
        if (PW==null) {
            return new ResponseEntity<Object>("le PW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            newPW.setId((long) id);
            return ResponseEntity.ok(service.update(newPW));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePW(@PathVariable int id){
        PW pw = service.findById(id);
        if (pw==null) {
            return new ResponseEntity<Object>("le PW avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(service.delete(pw));
        }
    }



}