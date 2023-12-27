package com.dent.dent.services;


import com.dent.dent.IDao.IDao;
import com.dent.dent.entities.PW;
import com.dent.dent.repositories.PwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PwService implements IDao<PW> {
    @Autowired
    PwRepository repisitory;
    @Override
    public PW create(PW o) {
        return repisitory.save(o);
    }

    @Override
    public PW update(PW o) {
        return repisitory.save(o);
    }

    @Override
    public Boolean delete(PW o) {
        try {
            repisitory.delete(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public PW findById(int id) {
        return repisitory.findById((long) id).orElse(null);
    }

    @Override
    public List<PW> findAll() {
        return repisitory.findAll();
    }
}