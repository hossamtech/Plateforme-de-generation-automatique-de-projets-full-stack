package codegen.spring_angular_auto_generator.springBootGenerator.controller;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectRequest;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ZipFileDto;
import codegen.spring_angular_auto_generator.springBootGenerator.service.*;
import codegen.spring_angular_auto_generator.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/project-generator")
@AllArgsConstructor
public class ProjectGeneratorController {
    private final SpringBootProjectGenerator springBootProjectGenerator;
    private final PropertiesGenerator propertiesGenerator;
    private final EntityGenerator entityGenerator;
    private final DtoGenerator dtoGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ControllerGenerator controllerGenerator;
    private final ConfigGenerator configGenerator;
    private final AngularProjectGeneratorService angularProjectGenerator;
    private final ProjectFileService projectFileService;

    @PostMapping("/create")
    public ResponseEntity<ZipFileDto> generateFullStackProject(
            Authentication connectedUser,
           @Valid @RequestBody ProjectRequest projectRequest) {

        try {

            String ProjectName = springBootProjectGenerator.getUniqueProjectName(connectedUser,
                    projectRequest.getProjectName());

            springBootProjectGenerator.generateProject(
                    ProjectName + "_spring",
                    projectRequest, connectedUser);

            angularProjectGenerator.createAngularProject(
                    ProjectName + "_angular",
                    projectRequest, connectedUser);

            return ResponseEntity.ok(projectFileService.compressProjectFiles(connectedUser, ProjectName));

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadProject(Authentication connectedUser, @PathVariable String fileName) throws FileNotFoundException {
        User user = (User) connectedUser.getPrincipal();
        File zipFile = new File(user.getDownloads() + "/" + fileName);

        if (!zipFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName());

        Resource resource = new InputStreamResource(new FileInputStream(zipFile));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipFile.length())
                .body(resource);
    }


//    @PostMapping("/clear-session/{request}")
//    public ResponseEntity<Void> clearSessionFiles(@PathVariable String request) throws IOException {
//        if (request != null) {
//            projectFileService.deleteFilesForSession(request);
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }

    @GetMapping("/zip-files")
    public ResponseEntity<List<ZipFileDto>> getZipFiles(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        List<ZipFileDto> zipFiles = projectFileService.getZipFilesForSession(user.getDownloads());
        return ResponseEntity.ok(zipFiles);
    }

}
