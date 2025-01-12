package codegen.spring_angular_auto_generator.springBootGenerator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectRequest;
import codegen.spring_angular_auto_generator.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringBootProjectGenerator {

    private final PropertiesGenerator propertiesGenerator;
    private final EntityGenerator entityGenerator;
    private final DtoGenerator dtoGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ControllerGenerator controllerGenerator;
    private final ConfigGenerator configGenerator;


    @Value("${spring.initializr.url}")
    private String initializrUrlTemplate;

    @Value("${generatedProjects.path}")
    private String generatedProjectsPath;

    public String getUniqueProjectName(Authentication connectedUser, String projectName) {
        User user = (User) connectedUser.getPrincipal();
        String uniqueProjectName = projectName;
        int maxSuffix = 0;

        // Check all existing folders in the user directory
        File userDir = new File(user.getDownloads());
        File[] files = userDir.listFiles((dir, name) ->
                name.startsWith(projectName) && (name.endsWith("_spring") || name.endsWith("_angular"))
        );


        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                String suffixPart = name.replace(projectName, "").replace("_spring", "").replace("_angular", "");
                try {
                    int suffix = suffixPart.isEmpty() ? 0 : Integer.parseInt(suffixPart.substring(1));
                    if (suffix > maxSuffix) {
                        maxSuffix = suffix;
                    }
                } catch (NumberFormatException e) {
                    // Ignore parsing errors in case of unexpected folder names
                }
            }
        }

        uniqueProjectName = projectName + "_" + (maxSuffix + 1);
        return uniqueProjectName;
    }


    public void generateProject(String projectName ,ProjectRequest projectRequest,
                                                     Authentication connectedUser) throws Exception {

        User user = (User) connectedUser.getPrincipal();

        String userDirPath = String.format("%s",user.getDownloads());
        String zipPath = String.format("%s/%s_generated.zip", userDirPath, projectName);
        String extractPath = String.format("%s/%s", userDirPath, projectName);

        File generatedProjectsDir = new File(userDirPath);
        if (!generatedProjectsDir.exists()) {
            boolean created = generatedProjectsDir.mkdirs(); // Create the directory
            if (!created) {
                throw new Exception("Failed to create the directory: " + generatedProjectsDir.getAbsolutePath());
            }
        }

        String initializrUrl = String.format(initializrUrlTemplate, projectRequest.getGroupId(), projectName, projectName);

        try {
            downloadFile(initializrUrl, zipPath);
            unzip(zipPath, extractPath);
            deleteFile(zipPath);

            String packagePath = projectRequest.getGroupId().replace(".", "/") + "/" + projectName;

            ProjectGenerationResponse projectGenerationResponse = ProjectGenerationResponse.builder()
                    .extractPath(extractPath)
                    .packagePath(packagePath)
                    .build();

            propertiesGenerator.generateDataBaseProperties(projectGenerationResponse.getExtractPath(), projectRequest.getDataBase());

            List<DiagramClass> diagramClasses = projectRequest.getDiagramClasses();
            entityGenerator.generateEntities(projectGenerationResponse, diagramClasses);
            dtoGenerator.generateDTOs(projectGenerationResponse, diagramClasses);
            repositoryGenerator.generateRepositories(projectGenerationResponse, diagramClasses);
            serviceGenerator.generateServices(projectGenerationResponse, diagramClasses);
            controllerGenerator.generateControllers(projectGenerationResponse, diagramClasses);
            configGenerator.generateConfig(projectGenerationResponse);

        } catch (Exception e) {
            throw new Exception("Failed to process the Spring Boot project: " + e.getMessage(), e);
        }
    }

    private static void downloadFile(String urlStr, String outputPath) throws Exception {
        URI uri = new URI(urlStr);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws Exception {
        File dir = new File(destDir);
        // Create the target directory if it does not exist
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new Exception("Failed to create the directory: " + destDir);
            }
        }

        try (ZipInputStream zipIn = new ZipInputStream(new java.io.FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            // Iterate through each entry in the ZIP
            while (entry != null) {
                String filePath = destDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // If it's a file, extract it
                    extractFile(zipIn, filePath);
                } else {
                    // If it's a directory, create it
                    File dirEntry = new File(filePath);
                    boolean dirCreated = dirEntry.mkdirs();
                    if (!dirCreated) {
                        throw new Exception("Failed to create the directory: " + filePath);
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                fos.write(bytesIn, 0, read);
            }
        }
    }

    private static void deleteFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new Exception("Unable to delete the file: " + filePath);
            }
        } else {
            throw new Exception("The file does not exist: " + filePath);
        }
    }
}
