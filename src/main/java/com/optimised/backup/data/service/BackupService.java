package com.optimised.backup.data.service;

import com.optimised.backup.tools.BackupTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Component
public class BackupService {
    @Autowired
    SettingsService settingsService;
    @Autowired
    BackupTools backupTools;


    @Scheduled (cron = "0 * * * * *",zone = "Europe/London")
    private void runBackup(){
        LocalTime backupTime = settingsService.getSettings().getBackupTime();
        LocalTime currentTime = LocalTime.now();

        Boolean backup = false;
        if (settingsService.getSettings().getAutoBackup()) {
            backup = (backupTime.getHour() == currentTime.getHour()) && (backupTime.getMinute() == currentTime.getMinute());
        }
        if (backup) {
            backupTools.AutoBackup();
        }
    }
}
