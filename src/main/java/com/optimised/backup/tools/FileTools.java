package com.optimised.backup.tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileTools {
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) {
        try {
            Files.walk(Paths.get(sourceDirectoryLocation))
                    .forEach(source -> {
                        Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                                .substring(sourceDirectoryLocation.length()));
                        try {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(String directoryToBeDeleted) {
        try {
            FileUtils.deleteDirectory(new File(directoryToBeDeleted));
        } catch (IOException e) {

        }

    }


    public static String zipFolder(Path source, String siteName, String backupDir,String backupOldDir) throws IOException {

        // get folder name as zip file name
        String zipFileName = "C:\\Backup\\" + siteName + "_" + LocalDate.now() + ".zip";
        // Check if there is a site backup existing. If so move it to the old backup folder
       moveFileToOld(siteName,backupDir,backupOldDir);

        try (
                ZipOutputStream zos = new ZipOutputStream(
                        new FileOutputStream(zipFileName))
        ) {

            Files.walkFileTree(source, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes) {

                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    try (FileInputStream fis = new FileInputStream(file.toFile())) {

                        Path targetFile = source.relativize(file);
                        zos.putNextEntry(new ZipEntry(targetFile.toString()));

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        // if large file, throws out of memory
                        //byte[] bytes = Files.readAllBytes(file);
                        //zos.write(bytes, 0, bytes.length);

                        zos.closeEntry();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("Unable to zip : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return zipFileName;
    }
    public static void moveFileToOld(String fileName, String backupDir, String backupOldDir) {
        Path path = Paths.get(backupDir);
        try {
            List<Path> result = findByFileName(path, fileName);
            result.forEach(x -> {
                try {
                    Files.move(x, Paths.get(backupOldDir +"/"+ x.getFileName()), REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        List<Path> result;
        try (Stream<Path> pathStream = Files.find(path,
                Integer.MAX_VALUE,
                (p, basicFileAttributes) ->
                        p.getFileName().toString().contains(fileName))
        ) {
            result = pathStream.collect(Collectors.toList());
        }
        return result;
    }

}
