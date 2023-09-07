package com.optimised.backup.data.repositories;


import com.optimised.backup.data.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathsRepo extends JpaRepository<Path,Long> {
}
