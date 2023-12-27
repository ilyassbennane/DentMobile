package com.dent.dent.services;

import com.dent.dent.IDao.IDao;
import com.dent.dent.entities.Groupe;
import com.dent.dent.entities.StudentPW;
import com.dent.dent.repositories.StudentPWRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentPWService implements IDao<StudentPW> {
    @Autowired
    StudentPWRepository repisitory;
    @Override
    public StudentPW create(StudentPW o) {
        return repisitory.save(o);
    }

    @Override
    public StudentPW update(StudentPW o) {
        return repisitory.save(o);
    }

    @Override
    public Boolean delete(StudentPW o) {
        try {
            repisitory.delete(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public StudentPW findById(int id) {
        return repisitory.findById((long) id).orElse(null);
    }

    @Override
    public List<StudentPW> findAll() {
        return repisitory.findAll();
    }
    public List<StudentPW> findByStudentId(Long studentId) {
        return repisitory.findByStudentId(studentId);
    }
}