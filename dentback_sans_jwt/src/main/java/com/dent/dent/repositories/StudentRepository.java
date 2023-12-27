package com.dent.dent.repositories;

import com.dent.dent.entities.Groupe;
import com.dent.dent.entities.Student;
import com.dent.dent.entities.StudentPW;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByLogin(String login);

}

