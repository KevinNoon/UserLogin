package com.optimised.backup.data.service;

import com.optimised.backup.data.entity.Settings;
import com.optimised.backup.data.repositories.SettingsRepo;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private final SettingsRepo settingsRepo;

    public SettingsService(SettingsRepo settingsRepo) {
        this.settingsRepo = settingsRepo;
    }

    public Settings getSettings(){
        return settingsRepo.findFirstBy();
    }

    public void saveSettings(Settings settings){
        settingsRepo.save(settings);
    }
}
