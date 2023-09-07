package com.optimised.backup.data.repositories;


import com.optimised.backup.data.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepo extends JpaRepository<Settings,Long> {
    Settings findFirstBy();
}
