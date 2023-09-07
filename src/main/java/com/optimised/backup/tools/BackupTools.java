package com.optimised.backup.tools;

import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.service.PathService;
import com.optimised.backup.data.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class BackupTools {
    @Autowired
    SiteService siteService;
    @Autowired
    PathService pathService;

    String zipFileName = "";
    public String Backup(Site site, String cylonPath, String workingDir, String backupDir, String backupOldDir, SiteService siteService){
        LocalDateTime backupTime = LocalDateTime.now();
        File theDir = new File(workingDir + "\\UnitronUC32");
        if (!theDir.exists()){
            theDir.mkdirs();
        } else {
            FileTools.deleteDirectory(theDir.getAbsolutePath());
            theDir.mkdirs();
        }
        String sourcePath = cylonPath + "\\" + site.getDirectory();
        String destinationDir = theDir.getAbsolutePath() + "\\" + site.getDirectory() ;

        FileTools.copyDirectory(sourcePath,destinationDir);
        try{

            FileWriter fw = new FileWriter(destinationDir + "\\CCBackUp.TXT");
            System.out.println(destinationDir + "\\CCBackUp.TXT");
            fw.append(site.getName() + "\n");
            fw.append(site.getDirectory() + "\n");
            if (site.getIDCode() != null) {fw.append(site.getIDCode() + "\n");} else {fw.append("Error\n");}
            if (site.getTelephone() != null) {fw.append(site.getTelephone() + "\n");} else {fw.append("Error\n");}
            fw.append(site.getRemote() + "\n");
            fw.append(site.getNetwork() + "\n");
            fw.append(backupTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            fw.append(site.getInternet() + "\n");
            if (site.getIpAddr() != null) {fw.append(site.getIpAddr() + "\n");} else {fw.append("Error\n");}
            fw.append(site.getPort() + "\n");
            fw.append(site.getBacNet()+ "\n");
            fw.append(site.getDefaultType() + "\n");
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(destinationDir + "\\SiteID.TXT");
            fw.append(site.getSiteNumber() + "\n");
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(destinationDir + "\\SiteName.TXT");
            fw.append(site.getName() + "\n");
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            zipFileName = FileTools.zipFolder(Path.of(workingDir),site.getName(),backupDir,backupOldDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Update the last backup time in the database.
        site.setBackupTime(backupTime);
        siteService.saveSite(site);

        return zipFileName;
    }

    public void AutoBackup(){
        List<Site> sites = siteService.findAllSites(null);
        String cylonPath = pathService.getCylonPath();
        String workingPath = pathService.getWorkingPath();
        String backupPath = pathService.getBackupPath();
        String backupOldPath = pathService.getBackupOldPath();

        sites.forEach(site -> {
            //We need to get the folder in the directory and then read the timestamp of the files in the directories.
            File file = new File("C:\\UnitronUC32\\" + site.getDirectory() + "\\" + "\\STRAT5");
            if (file.exists()) {
                List<File> dirs = Arrays.stream(file.listFiles()).filter(file1 -> file1.isDirectory()).toList();

                //Now get the files in the directories
                Boolean backup = false;
                for (int i = 0; i < dirs.size(); i++) {
                    File dirName = dirs.get(i);
                    List<File> fileList = Arrays.stream(dirName.listFiles()).filter(file1 -> file1.isFile() && file1.getName().contains("etg")).toList();
                    if (fileList != null) {
                        LocalDateTime dt = LocalDateTime.MIN;

                        for (int j = 0; j < fileList.size(); j++) {


                            LocalDateTime fileLM = Instant.ofEpochMilli(fileList.get(j).lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                            System.out.println(fileList.get(j).getName() + " " + fileLM);
                            if (dt.isBefore(fileLM)) {
                                dt = fileLM;
                            }
                        }
                        if (site.getBackupTime() == null) {
                            backup = true;
                        } else {
                            backup = site.getBackupTime().isBefore(dt);
                        }
                    }
                }
                if (backup){
                    Backup(site,cylonPath,workingPath,backupPath,backupOldPath,siteService);
                }
            }
        });
    }
    }

