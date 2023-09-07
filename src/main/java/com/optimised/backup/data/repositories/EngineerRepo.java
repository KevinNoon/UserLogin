package com.optimised.backup.data.repositories;

import com.optimised.backup.data.entity.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineerRepo extends JpaRepository<Engineer,Long> {
}
