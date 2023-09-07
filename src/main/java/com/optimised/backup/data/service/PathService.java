package com.optimised.backup.data.service;

import com.optimised.backup.data.entity.Path;
import com.optimised.backup.data.repositories.PathsRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final PathsRepo pathsRepo;

    public PathService(PathsRepo pathsRepo) {
        this.pathsRepo = pathsRepo;
    }

    public void savePath(Path path){
        pathsRepo.save(path);
    }

    public List<Path> findAllPaths(){
        System.out.println(pathsRepo.findAll());
            return pathsRepo.findAll();
    }

    public String getCylonPath(){
        List<Path> paths = findAllPaths();
        Path defaultPath = new Path();
        defaultPath.setPath_value("C:\\UnitronUC32");
        Path path = paths.stream().
                filter(s -> s.getName().equals("CylonDirectory")).findFirst().orElse(defaultPath);
        return path.getPath_value();
    }

    public String getWorkingPath(){
        List<Path> paths = findAllPaths();
        Path defaultPath = new Path();
        defaultPath.setPath_value(".\\Working");
        Path path = paths.stream().
                filter(s -> s.getName().equals("WorkingDirectory")).findFirst().orElse(defaultPath);
        return path.getPath_value();
    }
    public String getBackupPath(){
        List<Path> paths = findAllPaths();
        Path defaultPath = new Path();
        defaultPath.setPath_value("C:\\Backup");
        Path path = paths.stream().
                filter(s -> s.getName().equals("BackupDirectory")).findFirst().orElse(defaultPath);
        return path.getPath_value();
    }
    public String getBackupOldPath(){
        List<Path> paths = findAllPaths();
        Path defaultPath = new Path();
        defaultPath.setPath_value("C:\\Backup\\Old");
        Path path = paths.stream().
                filter(s -> s.getName().equals("BackupOldDirectory")).findFirst().orElse(defaultPath);
        return path.getPath_value();
    }
}
