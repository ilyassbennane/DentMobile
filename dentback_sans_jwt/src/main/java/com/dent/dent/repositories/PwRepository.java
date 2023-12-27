package com.dent.dent.repositories;

import com.dent.dent.entities.PW;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PwRepository extends JpaRepository<PW,Long> {
}