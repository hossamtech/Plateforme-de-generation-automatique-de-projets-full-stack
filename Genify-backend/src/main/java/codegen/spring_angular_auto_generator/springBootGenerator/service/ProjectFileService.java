package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.ZipFileDto;
import codegen.spring_angular_auto_generator.user.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Getter
public class ProjectFileService {

    @Value("${generatedProjects.path}")
    private String generatedProjectsPath;

    public ZipFileDto  compressProjectFiles(Authentication connectedUser, String projectName) throws IOException {
        User user = (User) connectedUser.getPrincipal();
        String filePathUser = user.getDownloads();

        File sessionDir = new File(filePathUser);

        if (!sessionDir.exists() || !sessionDir.isDirectory()) {
            throw new IOException("Session directory does not exist: " + filePathUser);
        }

        File zipFile = new File(filePathUser + "/" + projectName + ".zip");
        File[] projectDirs = sessionDir.listFiles(file -> file.getName().contains(projectName));

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            if (projectDirs != null) {
                for (File projectDir : projectDirs) {
                    if (projectDir.isDirectory()) {
                        addFilesToZip(projectDir, projectDir.getName(), zipOut);
//                        deleteDirectoryRecursively(projectDir); // Delete after adding to ZIP
                    }
                }
            }
        }

        Instant lastModifiedDate = Files.getLastModifiedTime(zipFile.toPath()).toInstant();

        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(zipFile.getName())
                .toUriString();

        return ZipFileDto.builder()
                .name(zipFile.getName())
                .downloadUrl(downloadUrl)
                .lastModifiedDate(lastModifiedDate)
                .size(zipFile.length())
                .build();
    }


    private void addFilesToZip(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            // If directory, add it to the zip with a trailing slash and recursively add its contents
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zipOut.putNextEntry(new ZipEntry(fileName));
            zipOut.closeEntry();

            // Safely get the list of files and check for null
            File[] nestedFiles = fileToZip.listFiles();
            if (nestedFiles != null) {
                for (File nestedFile : nestedFiles) {
                    addFilesToZip(nestedFile, fileName + nestedFile.getName(), zipOut);
                }
            }
            return;
        }

        // Add the file to the zip
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
        }
    }

    public List<ZipFileDto> getZipFilesForSession(String filePathUser) {
        File sessionDir = new File(filePathUser);

        List<ZipFileDto> zipFiles = new ArrayList<>();

        // Ensure the session directory exists and is accessible
        if (sessionDir.exists() && sessionDir.isDirectory()) {
            File[] files = sessionDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Check if the file is a .zip file
                    if (file.isFile() && file.getName().endsWith(".zip")) {
                        // Generate the download URL for each .zip file
                        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path(filePathUser + "/")
                                .path(file.getName())
                                .toUriString();
                        zipFiles.add(
                                new ZipFileDto(file.getName(), file.length(), downloadUrl, getLastModifiedDate(file)));
                    }
                }
            }
        }
        return zipFiles;
    }

    private Instant getLastModifiedDate(File file) {
        try {
            return Files.getLastModifiedTime(file.toPath()).toInstant();
        } catch (IOException e) {
            return Instant.now();
        }
    }

    // Delete all files associated with a specific session token
    public void deleteFilesForSession(String sessionToken) throws IOException {
        File sessionDir = new File(generatedProjectsPath + "/" + sessionToken);
        deleteDirectoryRecursively(sessionDir);
    }

    // Helper method to delete directory recursively
    private void deleteDirectoryRecursively(File directory) throws IOException  {
        if (directory.exists()) {
            File[] allContents = directory.listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    deleteDirectoryRecursively(file);
                }
            }
            if (!directory.delete()) {
                throw new IOException("Failed to delete " + directory.getAbsolutePath());
            }
        }
    }
}
