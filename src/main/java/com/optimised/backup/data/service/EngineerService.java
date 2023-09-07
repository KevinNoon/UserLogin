package com.optimised.backup.data.service;

import com.optimised.backup.data.entity.Engineer;
import com.optimised.backup.data.repositories.EngineerRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineerService {
    private final EngineerRepo engineerRepo;

    public EngineerService(EngineerRepo engineerRepo) {
        this.engineerRepo = engineerRepo;
    }

    public void saveEngineer(Engineer engineer){
        engineerRepo.save(engineer);
    }

    public List<Engineer> findAllEngineers(){
        System.out.println(engineerRepo.findAll());
            return engineerRepo.findAll();
    }
    public void delete(Long id) {
        engineerRepo.deleteById(id);
    }
}
